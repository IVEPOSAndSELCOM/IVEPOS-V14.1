package com.intuition.ivepos.mSwipe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bbpos.simplyprint.SimplyPrintController;
import com.intuition.ivepos.BeveragesMenuFragment_Dine;
import com.intuition.ivepos.BeveragesMenuFragment_Dine_l;
import com.intuition.ivepos.BeveragesMenuFragment_Qsr;
import com.intuition.ivepos.BeveragesMenuFragment_Retail;
import com.intuition.ivepos.R;
import com.intuition.ivepos.SplashScreenActivity;
import com.socsi.smartposapi.printer.Printer2;
import com.mswipetech.sdk.network.MSGatewayConnectionListener;
import com.mswipetech.wisepad.sdk.MSWisepadController;
import com.mswipetech.wisepad.sdk.component.CardSaleReceiptView;
import com.mswipetech.wisepad.sdk.data.CardSaleReceiptResponseData;
import com.mswipetech.wisepad.sdk.data.CardSaleResponseData;
import com.mswipetech.wisepad.sdk.data.MSDataStore;
import com.mswipetech.wisepad.sdk.data.ReceiptData;
import com.mswipetech.wisepad.sdk.device.MSWisepadDeviceController;
import com.mswipetech.wisepad.sdk.device.MSWisepadDeviceControllerResponseListener;
import com.mswipetech.wisepad.sdk.device.WisePadConnection;
import com.mswipetech.wisepad.sdk.listeners.MSWisepadControllerResponseListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;

public class MswipeSignatureActivity extends Activity {

    public SQLiteDatabase db = null;
    public final static String log_tab = "CreditSaleSignatureView=>";

    private ProgressDialog mProgressDialog =null;
    private EMVProcessTask mEMVProcessTask = null;
    private String mstrEMVProcessTaskType = "";
    private String title = "";

    private boolean mIsTxtSuccess = false;

    private MswipePrinterService mPrinterConnectionService;

    ArrayList<Printer2> wppreceipts;
    private boolean isPrintingProcess = false;


    boolean isCustomerCopy = false;
    private boolean isBound = false;
    private boolean isPrintRequestInQue = false;


    private ReceiptData reciptDataModel = new ReceiptData();
    private ArrayList<byte[]> receipts;
    private ArrayList<BluetoothDevice> pairedDevicesFound = new ArrayList<BluetoothDevice>();
    private static final int REQUEST_ENABLE_BT = 0;

    //for handling submit button,when print in progress.
    private boolean isPrinterStarted = false;

 	/*if the pinexists for the card then in the signature view area just show up the text as pin verified
 	and do not upload any signature data to the gateway*/

    private boolean misSignatureRequired = false;
    private boolean isEmvSwiper = false;
    private ApplicationData applicationData = null;

    //private ReceiptData mReceiptData = new ReceiptData();

    private ImageView imgHostConnectionStatus;

    //  end connect to bluetooth printer varabiles
    private RelativeLayout mRLTPrint;
    public boolean status = false;

    private boolean isUploadingSignature = false;

    private ImageButton mBtnSubmit = null;


    /**
     * The mswipe controller observes all the responses from the mswipe gateway
     */

    private MSWisepadControllerResponseListenerObserver mMSWisepadControllerResponseListenerObserver = null;

    /**
     * The mswipe controller instance used for calling up the api's
     */

    private MSWisepadController mMSWisepadController = null;

    /**
     * the custom receipt widget accepts the signature from the user, and then defines the callback about the
     * actions that can be observed to take a necessary steps for processing the receipt
     */

    private CardSaleReceiptView mCardSaleReceiptView = null;

    /**
     * Stores the card sale response data, which received from the sdk
     */
    private CardSaleResponseData cardSaleResponseData;
    private boolean isNFCCardTransaction = false;

    String WebserviceUrl;
    String account_selection;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            return false;
        }else{
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mswipe_creditsale_singnatureview);
        applicationData = ApplicationData.getApplicationDataSharedInstance();

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE|Context.MODE_NO_LOCALIZED_COLLATORS,null);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity.getDefaultSharedPreferencesMultiProcess(MswipeSignatureActivity.this);
        account_selection= sharedpreferences_select.getString("account_selection", null);

        if (account_selection.toString().equals(getString(R.string.dine_nospace))) {
            WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
        }else {
            if (account_selection.toString().equals(getString(R.string.qsr_nospace))) {
                WebserviceUrl = "https://theandroidpos.com/IVEPOS_NEW/";
            }else {
                WebserviceUrl = "https://theandroidpos.com/IVEPOSRETAIL_NEW/";
            }
        }

        if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(getPackageName(), log_tab + " isPrinterSupportRequired "  +AppSharedPrefrences.getAppSharedPrefrencesInstace().isPrinterSupportRequired(), true, true);


        if(AppSharedPrefrences.getAppSharedPrefrencesInstace().isPrinterSupportRequired()) {

            doBindService();
        }

        mMSWisepadController = MSWisepadController.
                getSharedMSWisepadController(MswipeSignatureActivity.this,
                        AppSharedPrefrences.getAppSharedPrefrencesInstace().getGatewayEnvironment(),
                        AppSharedPrefrences.getAppSharedPrefrencesInstace().getNetworkSource(),
                        new MSGatewayConncetionObserver());

        if(title == null)
            title = "Card sale";



        try {

            title = (String)getIntent().getStringExtra("Title");

            String  receiptDetail = getIntent().getStringExtra("receiptDetail");

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + " receiptDetail "  +receiptDetail, true, true);


            cardSaleResponseData = (CardSaleResponseData) getIntent().getSerializableExtra("cardSaleResponseData");

            if(cardSaleResponseData.getReceiptData().trxType.equalsIgnoreCase("CLess")){
                isNFCCardTransaction = true;
            }

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + " cardSaleResponseData "  + cardSaleResponseData, true, true);

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + " isNFCCardTransaction "  + isNFCCardTransaction, true, true);


            cardSaleResponseData.setEmvCardExpdate("xx/xx");
            cardSaleResponseData.setExpiryDate("xx/xx");
            misSignatureRequired = cardSaleResponseData.isSignatureRequired();
            mIsTxtSuccess = cardSaleResponseData.getResponseStatus();

            if(cardSaleResponseData.getCardSchemeResults() == MSWisepadDeviceControllerResponseListener.CARDSCHEMERRESULTS.ICC_CARD)
                isEmvSwiper = true;

            reciptDataModel = cardSaleResponseData.getReceiptData();
            /* updateing the certificate tag which received in onReturnBatchData from the wisepad */
            reciptDataModel.certif = cardSaleResponseData.getCertif();

            if(cardSaleResponseData.getCardSchemeResults() == MSWisepadDeviceControllerResponseListener.CARDSCHEMERRESULTS.MAG_AMEXCARD)
                reciptDataModel.isPinVarifed = "false";

        }
        catch (Exception e){
            // TODO: handle exception
        }

        isPrintRequestInQue = !misSignatureRequired;
        initViews();

    }


    public void initViews()
    {

        mMSWisepadControllerResponseListenerObserver = new MSWisepadControllerResponseListenerObserver();
        mCardSaleReceiptView = (CardSaleReceiptView)findViewById(R.id.cardsalereceiptview_CSRV_receipt);
        // set the card sale data, returned by the online card sale transaction
        mCardSaleReceiptView.setCardSaleResponseData(cardSaleResponseData, AppSharedPrefrences.getAppSharedPrefrencesInstace().getCurrencyCode());

        if(isEmvSwiper)
            ((TextView) findViewById(R.id.creditsale_TXT_removecard)).setText(getResources().getString(R.string.creditsalesignatureactivity_please_remove_the_card));
        else
            ((TextView) findViewById(R.id.creditsale_TXT_removecard)).setText(getResources().getString(R.string.creditsalesignatureactivity_card_swiped_successfully));

        ((LinearLayout) findViewById(R.id.topbar_LNR_topbar_menu)).setVisibility(View.GONE);
        ((LinearLayout) findViewById(R.id.topbar_LNR_topbar_cancel)).setVisibility(View.GONE);


//        ((ImageView)findViewById(R.id.topbar_IMG_cancel)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//
//                finish();
//                Intent intent = new Intent(MswipeSignatureActivity.this, MenuView.class);
//                startActivity(intent);
//            }
//        });

        ((TextView)findViewById(R.id.topbar_LBL_heading)).setText(title);
        ((RelativeLayout)findViewById(R.id.top_bar_REL_content)).setBackgroundColor(getResources().getColor(R.color.green));

        mBtnSubmit = (ImageButton) findViewById(R.id.creditsale_BTN_submitsignature);
        mBtnSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                if(!misSignatureRequired || mCardSaleReceiptView.isSignatureDrawn() || isNFCCardTransaction) {


                    mMSWisepadController.processCardSaleReceipt(
                            AppSharedPrefrences.getAppSharedPrefrencesInstace().getReferenceId(),
                            AppSharedPrefrences.getAppSharedPrefrencesInstace().getSessionToken(),
                            cardSaleResponseData,
                            mCardSaleReceiptView,
                            mMSWisepadControllerResponseListenerObserver);


                    mProgressDialog = new ProgressDialog(MswipeSignatureActivity.this);
                    mProgressDialog.setMessage(getString(R.string.processing));
                    mProgressDialog.show();
                }
                else{

                    showDialog(getString(R.string.creditsalesignatureactivity_please_sign_the_receipt_to_proceed));
                }
            }
        });

        ImageButton btnClear = (ImageButton) findViewById(R.id.creditsale_BTN_clear);
        btnClear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {

                mCardSaleReceiptView.clearSignature();

            }
        });

        if(!misSignatureRequired)
            btnClear.setVisibility(View.GONE);


        mRLTPrint = (RelativeLayout) findViewById(R.id.creditsale_REL_print_action);
        mRLTPrint.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View arg0) {

                if (AppSharedPrefrences.getAppSharedPrefrencesInstace().isPrintSignatureRequired()) {
                    if (misSignatureRequired)
                    {
                        if (mCardSaleReceiptView.isSignatureDrawn())
                        {
                            if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_PLUS)
                            {
                                printWppReceipt(mCardSaleReceiptView);
                            }else
                            {
                                printReceipt();
                            }
                        }
                        else{
                            showDialog( getString(R.string.creditsalesignatureactivity_please_sign_the_receipt_to_proceed));
                        }
                    }
                    else{
                        if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_NEO)
                        {
                            printNeoReceipt();
                        }
                        else if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_PLUS)
                        {
                            printWppReceipt(mCardSaleReceiptView);
                        }else
                        {
                            printReceipt();
                        }
                    }

                }
                else {
                    if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_NEO)
                    {
                        printNeoReceipt();
                    }
                    else if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_PLUS)
                    {
                        printWppReceipt(mCardSaleReceiptView);
                    }else
                    {
                        printReceipt();
                    }
                }
            }
        });

        if(!AppSharedPrefrences.getAppSharedPrefrencesInstace().isPrintSignatureRequired() || !misSignatureRequired)
        {
            mRLTPrint.setEnabled(false);
            mRLTPrint.setVisibility(View.GONE);
        }


        imgHostConnectionStatus = (ImageView) findViewById(R.id.topbar_IMG_position2);
        imgHostConnectionStatus.setVisibility(View.INVISIBLE);


    }


    /**
     * MSWisepadControllerResponseListenerObserver
     * The mswipe overridden class  observer which listens to the responses for the mswipe sdk function requests
     */
    class MSWisepadControllerResponseListenerObserver implements MSWisepadControllerResponseListener
    {

        /**
         * onReponseData
         * The response data notified back to the call back function
         * @param
         * aMSDataStore
         * 		the generic mswipe data store, this instance is refers to Receipt information, so this
         * need be converted back to CardSaleReceiptResponseData to access the receipt response data
         * @return
         */
        public void onReponseData(MSDataStore aMSDataStore)
        {

            if(mProgressDialog != null)
                mProgressDialog.dismiss();

            CardSaleReceiptResponseData uploadSignatureResponseData = (CardSaleReceiptResponseData) aMSDataStore;
            boolean responseStatus = uploadSignatureResponseData.getResponseStatus();
            String responseMessage = "";

            if (responseStatus)
                responseMessage = uploadSignatureResponseData.getResponseSuccessMessage();
            else
                responseMessage = uploadSignatureResponseData.getResponseFailureReason();

		/*	final Dialog dialog = Constants.showActivityDialog(MswipeSignatureActivity.this, "", responseMessage,
					Constants.CUSTOM_DLG_TYPE.CUSTOM_DLG_TYPE_SHOW_DLG_INFO, getString(R.string.yes), getString(R.string.no));

			RelativeLayout yes = (RelativeLayout) dialog.findViewById(R.id.customdlg_sdk_RLT_yes);
			yes.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					doneWithCreditSale();
					dialog.dismiss();

				}
			});*/


            AlertDialog.Builder builder1 = new AlertDialog.Builder(MswipeSignatureActivity.this);
            builder1.setMessage(responseMessage);
            builder1.setCancelable(false);

            builder1.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            doneWithCreditSale();

                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();


			/*final Dialog dlg = Constants.showDialog(MswipeSignatureActivity.this, title, responseMessage,
					Constants.CUSTOM_DLG_TYPE.CUSTOM_DLG_TYPE_RETURN_DLG_INFO);
			Button btnOk = (Button) dlg.findViewById(R.id.customdlg_BTN_yes);
			btnOk.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dlg.dismiss();

					doneWithCreditSale();

				}
			});
			dlg.show();*/

        }
    }

    /**
     * MSGatewayConncetionObserver
     * The mswipe overridden class  observer which observers the mswipe gateway network connections states

     */
    class MSGatewayConncetionObserver implements MSGatewayConnectionListener {

        @Override
        public void Connecting(String string) {
            // TODO Auto-generated method stub

        }

        @Override
        public void Connected(String string) {
            // TODO Auto-generated method stub

        }

        @Override
        public void disConnect(String string) {
            // TODO Auto-generated method stub

        }
    }



    void doBindService()
    {

        if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(getPackageName(), log_tab + "  " , true, true);


        if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_NEO) {

            bindService(new Intent(MswipeSignatureActivity.this, MSWisepadDeviceController.class), mMSWisepadDeviceControllerService, Context.BIND_AUTO_CREATE);

        }
        else {
            bindService(new Intent(this, MswipePrinterService.class), mConnection, Context.BIND_AUTO_CREATE);
        }
        isBound = true;
    }

    void doUnbindService()
    {
        if (isBound)
        {
            // If we have received the service, and hence registered with it,
            // then now is the time to unregister.
            // Detach our existing connection.
            if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_NEO) {
                unbindService(mMSWisepadDeviceControllerService);
            }
            else {
                unbindService(mConnection);
            }
            isBound = false;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection()
    {

        public void onServiceConnected(ComponentName className, IBinder service)
        {

            try
            {

                MswipePrinterService.LocalBinder localBinder = (MswipePrinterService.LocalBinder) service;
                mPrinterConnectionService =  localBinder.getService();
                mPrinterConnectionService.setPrinterListner(new PrinterListner());

            } catch (Exception e) {
                // In this case the service has crashed before we could even do
                // anything with it
            }

        }

        public void onServiceDisconnected(ComponentName className)
        {
            // This is called when the connection with the service has been
            // unexpectedly topbar_img_host_inactive - process crashed.
            mPrinterConnectionService = null;
        }
    };


    public MSWisepadDeviceController mMSWisepadDeviceController = null;


    private ServiceConnection mMSWisepadDeviceControllerService = new ServiceConnection()
    {
        public void onServiceConnected(ComponentName className, IBinder service)
        {
            try
            {
                MSWisepadDeviceController.LocalBinder localBinder = (MSWisepadDeviceController.LocalBinder) service;
                mMSWisepadDeviceController = localBinder.getService();

                if(mMSWisepadDeviceController != null) {

                    mMSWisepadDeviceController.initMswipeWisepadDeviceController(new MSWisepadDeviceObserver(),
                            true, false, false,
                            false, null);

                }
            }
            catch (Exception e) {

                if (ApplicationData.IS_DEBUGGING_ON)
                    Logs.v(ApplicationData.packName, "exception."+e.toString(), true, true);
            }
        }

        public void onServiceDisconnected(ComponentName className)
        {
            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName, "Wisepad servcie un-binded and wisepad is disconnected...", true, true);
            /**
             * This is called when the connection with the service has been
             * unexpectedly disconnected - process crashed.
             *
             */

            mMSWisepadDeviceController = null;

        }
    };

    class MSWisepadDeviceObserver implements MSWisepadDeviceControllerResponseListener
    {

        @Override
        public void onReturnWisepadConnection(WisePadConnection wisePadConntection, BluetoothDevice bluetoothDevice) {

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName,  "wisePadConntection " + wisePadConntection, true, true);

            String msg = "";

            if(wisePadConntection == WisePadConnection.WisePadConnection_CONNECTED)
            {
                isPrinterStarted =false;

                mRLTPrint.setVisibility(View.VISIBLE);
                mRLTPrint.setEnabled(true);

                if (applicationData.IS_DEBUGGING_ON)
                    Logs.v(getPackageName(), log_tab + "onBTv2Connected", true, true);

                if (mstrEMVProcessTaskType.equalsIgnoreCase("backbutton") || mstrEMVProcessTaskType.equalsIgnoreCase("onlinesubmit")) {

                    if (mEMVProcessTask != null && mEMVProcessTask.getStatus() != AsyncTask.Status.FINISHED)
                        mEMVProcessTask.cancel(true);
                }


                new Thread() {
                    public void run() {
                        try {
                            //sleep(1000);
                            if (AppSharedPrefrences.getAppSharedPrefrencesInstace().isPrintSignatureRequired()) {
                                if (isPrintRequestInQue) {
                                    if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_NEO)
                                    {
                                        printNeoReceipt();
                                    }
                                    else if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_PLUS)
                                    {
                                        printWppReceipt(mCardSaleReceiptView);
                                    }else
                                    {
                                        printReceipt();
                                    }
                                    isPrintRequestInQue = false;
                                }
                            }
                            else {

                                if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_NEO)
                                {
                                    printNeoReceipt();
                                }
                                else if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_PLUS)
                                {
                                    printWppReceipt(mCardSaleReceiptView);
                                }else
                                {
                                    printReceipt();
                                }
                                isPrintRequestInQue = false;
                            }


                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    };
                }.start();


            }
            else if(wisePadConntection == WisePadConnection.WisePadConnection_CONNECTING){

            }
            else if(wisePadConntection == WisePadConnection.WisePadConnection_NOT_CONNECTED){

                msg = getString(R.string.device_not_connected);
            }
            else if(wisePadConntection == WisePadConnection.WisePadConnection_DIS_CONNECTED){

                msg = getString(R.string.device_disconnected);
            }

            if (msg.length()>0) {
                showPrinterDialog(msg);
            }
        }

        @Override
        public void onRequestWisePadCheckCardProcess(CheckCardProcess checkCardProcess, ArrayList<String> dataList) {

        }

        @Override
        public void onReturnWisePadOfflineCardTransactionResults(CheckCardProcessResults checkCardResults, Hashtable<String, Object> paramHashtable) {

        }

        @Override
        public void onError(Error error, String errorMsg) {

            isPrinterStarted =false;

            String msg = "";

            if (mstrEMVProcessTaskType.equalsIgnoreCase("backbutton") ||
                    mstrEMVProcessTaskType.equalsIgnoreCase("onlinesubmit")) {

                if (mEMVProcessTask != null && mEMVProcessTask.getStatus() != AsyncTask.Status.FINISHED)
                    mEMVProcessTask.cancel(true);

            }
            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName,  "error " + error, true, true);
            if (msg.length()>0) {
                showPrinterDialog(msg);
            }
        }

        @Override
        public void onRequestDisplayWispadStatusInfo(DisplayText msg) {

        }

        @Override
        public void onReturnDeviceInfo(Hashtable<String, String> paramHashtable) {

        }

        @Override
        public void onReturnFunctionKeyResult(FunctionKeyResult keyType) {

        }

        @Override
        public void onReturnWispadNetwrokSettingInfo(WispadNetwrokSetting wispadNetwrokSetting, boolean status, Hashtable<String, Object> netwrokSettingInfo) {

        }

        @Override
        public void onReturnNfcDetectCardResult(NfcDetectCardResult nfcDetectCardResult, Hashtable<String, Object> hashtable) {

        }

        @Override
        public void onReturnNfcDataExchangeResult(boolean isSuccess, Hashtable<String, String> data) {

        }

        @Override
        public void onPrintResult(PrintResult printResult) {


            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName,  "printResult " + printResult, true, true);

            isPrinterStarted =false;

            if (mstrEMVProcessTaskType.equalsIgnoreCase("backbutton") ||
                    mstrEMVProcessTaskType.equalsIgnoreCase("onlinesubmit")) {

                if (mEMVProcessTask != null && mEMVProcessTask.getStatus() != AsyncTask.Status.FINISHED)
                    mEMVProcessTask.cancel(true);

            }

            String msg = "";

            if(printResult == PrintResult.PRINT_SUCCESS)
            {
                msg = getString(R.string.printer_command_success);

            }
            else if(printResult == PrintResult.PRINT_NO_PAPER_OR_COVER_OPENED)
            {
                msg = getString(R.string.no_paper);

            }
            else if(printResult == PrintResult.PRINT_WRONG_PRINTER_COMMAND)
            {
                msg = getString(R.string.printer_command_not_available);

            }
            else if(printResult == PrintResult.PRINT_OVERHEAT)
            {
                msg = getString(R.string.printer_overheat);

            }
            else if(printResult == PrintResult.PRINT_PRINTER_ERROR)
            {
                msg = getString(R.string.printer_command_not_available);

            }

            if (msg.length()>0) {
                showPrinterDialog(msg);
            }
        }
    }

	/*private void dismissPrinterDialog()
	{

		try
		{
			if(mDialogPrinter != null)
				mDialogPrinter.dismiss();
			mDialogPrinter = null;
		}
		catch(Exception ex){}
	}*/

    private void showPrinterDialog(String msg)
    {
        if(!isUploadingSignature)
        {
            try
            {


                AlertDialog.Builder builder1 = new AlertDialog.Builder(MswipeSignatureActivity.this);
                builder1.setMessage(msg);
                builder1.setCancelable(false);

                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();

                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();


				/*final Dialog dialog = Constants.showActivityDialog(MswipeSignatureActivity.this, getResources().getString(R.string.mswipe),
                        msg, Constants.CUSTOM_DLG_TYPE.CUSTOM_DLG_TYPE_SHOW_DLG_INFO, getString(R.string.yes), getString(R.string.no));

                RelativeLayout yes = (RelativeLayout) dialog.findViewById(R.id.customdlg_sdk_RLT_yes);
                yes.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });*/
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }


    public void onStart()
    {
        //Log.v(ApplicationData.packName, log_tab + " onStart Signature screen try to connect in case if the connection is not forwarded to the signature screen...");
        mMSWisepadController.startMSGatewayConnection(this);
        super.onStart();
    }


    @Override
    protected void onStop()
    {
        // TODO Auto-generated method stub

        try
        {
            doUnbindService();
        }
        catch (Exception ex)
        {
        }
        //Log.v(ApplicationData.packName, log_tab + " onStop Signature screen closing the connection...");
        mMSWisepadController.stopMSGatewayConnection();
        super.onStop();
    }


    public void printReceipt()
    {
        if(isBound){
            String noOfEmi = "";
            try
            {
                noOfEmi = reciptDataModel.noOfEmi;
            }
            catch (Exception e)
            {

                noOfEmi = "";
            }
            ReceiptUtility receiptUtility = new ReceiptUtility(this);

            if(noOfEmi.length() > 0)
            {

                isPrinterStarted =true;

                if (ApplicationData.IS_DEBUGGING_ON)
                    Logs.v(getPackageName(), log_tab + " genEmiSaleReceipt " , true, true);

                receipts = new ArrayList<byte[]>();
                receipts.add(receiptUtility.printReceipt(reciptDataModel, mCardSaleReceiptView.getSignature(), AppSharedPrefrences.getAppSharedPrefrencesInstace().isPrintSignatureRequired(), ReceiptUtility.TYPE.EMI));


            } else{

                isPrinterStarted =true;

                if (ApplicationData.IS_DEBUGGING_ON)
                    Logs.v(getPackageName(), log_tab + " genCardSaleReceipt " , true, true);

                receipts = new ArrayList<byte[]>();
                receipts.add(receiptUtility.printReceipt(reciptDataModel, mCardSaleReceiptView.getSignature(), AppSharedPrefrences.getAppSharedPrefrencesInstace().isPrintSignatureRequired(), ReceiptUtility.TYPE.CARD));

            }

            isPrintRequestInQue = true;

            if(receipts.size() > 0)
            {
                mPrinterConnectionService.printReceipt(receipts.get(0));
            }

        }else{
            isPrintRequestInQue = true;
            doBindService();
        }


    }



    public void printNeoReceipt()
    {
        if(isBound){

            String noOfEmi = "";
            try
            {
                noOfEmi = reciptDataModel.noOfEmi;
            }
            catch (Exception e)
            {

                noOfEmi = "";
            }

            ReceiptUtilityNeo receiptUtility = new ReceiptUtilityNeo(this);

            if(noOfEmi.length() > 0)
            {

                isPrinterStarted =true;

                if (ApplicationData.IS_DEBUGGING_ON)
                    Logs.v(getPackageName(), log_tab + " genEmiSaleReceipt " , true, true);

                receipts = new ArrayList<byte[]>();
                receipts.add(receiptUtility.genEMIReceipt4(reciptDataModel, mCardSaleReceiptView.getSignature(), AppSharedPrefrences.getAppSharedPrefrencesInstace().isPrintSignatureRequired(), ReceiptUtilityNeo.TYPE.EMI, false));


            } else{

                isPrinterStarted =true;

                if (ApplicationData.IS_DEBUGGING_ON)
                    Logs.v(getPackageName(), log_tab + " genCardSaleReceipt " , true, true);

                receipts = new ArrayList<byte[]>();
                receipts.add(receiptUtility.genReceipt4(reciptDataModel, mCardSaleReceiptView.getSignature(), AppSharedPrefrences.getAppSharedPrefrencesInstace().isPrintSignatureRequired(), ReceiptUtilityNeo.TYPE.CARD, false));

            }

            isPrintRequestInQue = true;

            if(receipts.size() > 0)
            {
                mMSWisepadDeviceController.print(receipts);
            }

        }else{
            isPrintRequestInQue = true;
            doBindService();
        }


    }


    private void showToast(String msg){

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    class PrinterListner extends MswipePrinterListner {


        @Override
        public void onRegisterd()
        {
            // TODO Auto-generated method stub
            super.onRegisterd();

        }

        @Override
        public void onUnRegisterd()
        {
            // TODO Auto-generated method stub
            super.onUnRegisterd();
        }


        @Override
        public void onPrinterStateChanged(PRINTER_STATE state)
        {
            // TODO Auto-generated method stub
            super.onPrinterStateChanged(state);
            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), "printer state "+state, true, true);

            if (state == PRINTER_STATE.WAITINGFORCONNECTION)
            {

                isPrinterStarted =false;

                mRLTPrint.setVisibility(View.VISIBLE);
                mRLTPrint.setEnabled(true);

                showDialog(getString(R.string.connecting_to_printer_if_its_taking_longer_than_usual_please_restart_the_printer_and_try_reconnecting));

			/*	final Dialog dialog = Constants.showDialog(MswipeSignatureActivity.this, title,
						getString(R.string.connecting_to_printer_if_its_taking_longer_than_usual_please_restart_the_printer_and_try_reconnecting), Constants.CUSTOM_DLG_TYPE.CUSTOM_DLG_TYPE_RETURN_DLG_INFO);

				Button yes = (Button) dialog.findViewById(R.id.customdlg_BTN_yes);
				yes.setOnClickListener(new OnClickListener()
				{

					public void onClick(View v)
					{
						dialog.dismiss();

					}
				});*/


            }else if (state == PRINTER_STATE.CONNECTED)
            {

                isPrinterStarted =false;

                mRLTPrint.setVisibility(View.VISIBLE);
                mRLTPrint.setEnabled(true);

                if (applicationData.IS_DEBUGGING_ON)
                    Logs.v(getPackageName(), log_tab + "onBTv2Connected", true, true);

                if (mstrEMVProcessTaskType.equalsIgnoreCase("backbutton") || mstrEMVProcessTaskType.equalsIgnoreCase("onlinesubmit")) {

                    if (mEMVProcessTask != null && mEMVProcessTask.getStatus() != AsyncTask.Status.FINISHED)
                        mEMVProcessTask.cancel(true);
                }


                new Thread() {
                    public void run() {
                        try {
                            //sleep(1000);
                            if (AppSharedPrefrences.getAppSharedPrefrencesInstace().isPrintSignatureRequired()) {
                                if (isPrintRequestInQue) {
                                    if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_NEO)
                                    {
                                        printNeoReceipt();
                                    }
                                    else if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_PLUS)
                                    {
                                        printWppReceipt(mCardSaleReceiptView);
                                    }else
                                    {
                                        printReceipt();
                                    }
                                    isPrintRequestInQue = false;
                                }
                            }
                            else {

                                if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_NEO)
                                {
                                    printNeoReceipt();
                                }
                                else if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_PLUS)
                                {
                                    printWppReceipt(mCardSaleReceiptView);
                                }else
                                {
                                    printReceipt();
                                }
                                isPrintRequestInQue = false;
                            }


                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    };
                }.start();



            }else if (state == PRINTER_STATE.DISCONNECTED) {


                isPrinterStarted =false;

                mRLTPrint.setVisibility(View.VISIBLE);
                mRLTPrint.setEnabled(true);

                if (applicationData.IS_DEBUGGING_ON)
                    Logs.v(getPackageName(), log_tab + "onBTv2Disconnected  ", true, true);

                if (mstrEMVProcessTaskType.equalsIgnoreCase("backbutton") ||
                        mstrEMVProcessTaskType.equalsIgnoreCase("onlinesubmit") ||
                        mstrEMVProcessTaskType.equalsIgnoreCase("stopbluetooth")) {

                    if (mEMVProcessTask != null && mEMVProcessTask.getStatus() != AsyncTask.Status.FINISHED)
                        mEMVProcessTask.cancel(true);
                }

            }else if (state == PRINTER_STATE.PRINTING) {


            }
        }


        @Override
        public void onPrinterConnectionError(PRINTER_CONNECTION_ERROR state, ArrayList<BluetoothDevice> devices) {
            // TODO Auto-generated method stub
            super.onPrinterConnectionError(state, devices);

            isPrinterStarted =false;


            String msg = "";
            mRLTPrint.setVisibility(View.VISIBLE);
            mRLTPrint.setEnabled(true);


            if(state == PRINTER_CONNECTION_ERROR.NO_PAIRED_DEVICE_FOUND){

                msg = getString(R.string.no_paired_printer_found_please_pair_the_printer_from_your_phones_bluetooth_settings_and_try_again);

            }
            else if(state == PRINTER_CONNECTION_ERROR.MULTIPLE_PAIRED_DEVICE){

                pairedDevicesFound = devices;

                TaskShowMultiplePairedDevices pairedtask = new TaskShowMultiplePairedDevices();
                pairedtask.execute();

                mRLTPrint.setEnabled(false);
                return;

            }else if (state == PRINTER_CONNECTION_ERROR.BLUETOOTH_OFF) {

                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

            }else if (state == PRINTER_CONNECTION_ERROR.UNKNOWN) {

                msg = getString(R.string.unable_to_connect_to_the_bluetooth_printer);

            }else if (state == PRINTER_CONNECTION_ERROR.CONNECTING){
                msg = getString(R.string.connecting_to_printer_if_its_taking_longer_than_usual_please_restart_the_printer_and_try_reconnecting);
            }else if (state == PRINTER_CONNECTION_ERROR.WISEPAD_SWITCHED_OFF){

                AlertDialog.Builder builder1 = new AlertDialog.Builder(MswipeSignatureActivity.this);
                builder1.setMessage(getString(R.string.printer_not_connected_please_make_sure_that_the_printer_is_switched_on));
                builder1.setCancelable(false);

                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                mPrinterConnectionService.reconnectToDevice();


                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();



				/*final Dialog dialog = Constants.showActivityDialog(MswipeSignatureActivity.this, getResources().getString(R.string.mswipe),
						getString(R.string.printer_not_connected_please_make_sure_that_the_printer_is_switched_on),
						Constants.CUSTOM_DLG_TYPE.CUSTOM_DLG_TYPE_SHOW_DLG_INFO, getString(R.string.connect), getString(R.string.close));

				RelativeLayout yes = (RelativeLayout) dialog.findViewById(R.id.customdlg_sdk_RLT_yes);
				yes.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						dialog.dismiss();
						mPrinterConnectionService.reconnectToDevice();

					}
				});

				return;*/
            }else if(state == PRINTER_CONNECTION_ERROR.PRINTING_IN_PROGRESS){



                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MswipeSignatureActivity.this, getString(R.string.printing_in_process), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName,log_tab+ "makeConnnection msg "+ msg,true, true);

            if (msg.length()>0) {
                showPrinterDialog(msg);
            }
        }


        @Override
        public void onReturnDeviceInfo(Hashtable<String, String> deviceInfoTable) {

            String msg = "";

            String productId = deviceInfoTable.get("productId");
            String firmwareVersion = deviceInfoTable.get("firmwareVersion");
            String bootloaderVersion = deviceInfoTable.get("bootloaderVersion");
            String hardwareVersion = deviceInfoTable.get("hardwareVersion");
            String isUsbConnected = deviceInfoTable.get("isUsbConnected");
            String isCharging = deviceInfoTable.get("isCharging");
            String batteryLevel = deviceInfoTable.get("batteryLevel");

            String content = "";
            content += getString(R.string.product_id) + productId + "\n";
            content += getString(R.string.firmware_version) + firmwareVersion + "\n";
            content += getString(R.string.bootloader_version) + bootloaderVersion + "\n";
            content += getString(R.string.hardware_version) + hardwareVersion + "\n";
            content += getString(R.string.usb) + isUsbConnected + "\n";
            content += getString(R.string.charge) + isCharging + "\n";
            content += getString(R.string.battery_level) + batteryLevel + "\n";

            msg =  content;

            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "onReturnDeviceInfo =>  msg " + msg, true, true);
        }

        @Override
        public void onReturnPrinterResult(SimplyPrintController.PrinterResult printerResult) {

            isPrinterStarted =false;

            String msg = "";

            mRLTPrint.setVisibility(View.VISIBLE);
            mRLTPrint.setEnabled(true);

            if(printerResult == SimplyPrintController.PrinterResult.SUCCESS) {
                msg =  getString(R.string.printer_command_success);
            } else if(printerResult == SimplyPrintController.PrinterResult.NO_PAPER) {
                msg =  getString(R.string.no_paper);
            } else if(printerResult == SimplyPrintController.PrinterResult.WRONG_CMD) {
                msg =  getString(R.string.wrong_printer_cmd);
            } else if(printerResult == SimplyPrintController.PrinterResult.OVERHEAT) {
                msg =  getString(R.string.printer_overheat);
            }

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + " onReturnPrinterResult PrinterResult "+ msg, true, true);


            showPrinterDialog(msg);
        }

        @Override
        public void onPrinterOperationEnd() {

            isPrinterStarted =false;

        }

        @Override
        public void onBatteryLow(SimplyPrintController.BatteryStatus batteryStatus) {

            String msg = "";

            if(batteryStatus == SimplyPrintController.BatteryStatus.LOW) {
                msg =  getString(R.string.printer_battery_low);
            } else if(batteryStatus == SimplyPrintController.BatteryStatus.CRITICALLY_LOW) {
                msg =  getString(R.string.printer_battery_critically_low);
            }

            showPrinterDialog(msg);

        }



    }

    public void printWppReceipt(CardSaleReceiptView mCardSaleReceiptView) {

        if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(getPackageName(), log_tab + "isBound " + isBound, true, true);

        if (isBound)
        {
            String noOfEmi;
            try {
                noOfEmi = reciptDataModel.noOfEmi;
            } catch (Exception e) {

                noOfEmi = "";
            }
            ReceiptUtilityWPP receiptUtility = new ReceiptUtilityWPP(this);

            if (noOfEmi.length() > 0) {

                if (ApplicationData.IS_DEBUGGING_ON)
                    Logs.v(getPackageName(), log_tab + " genEmiSaleReceipt ", true, true);

                wppreceipts = new ArrayList<>();
                wppreceipts.add(receiptUtility.printReceipt(reciptDataModel,
                        mCardSaleReceiptView.getSignature(),
                        AppSharedPrefrences.getAppSharedPrefrencesInstace().isPrintSignatureRequired(),
                        ReceiptUtilityWPP.TYPE.EMI, isCustomerCopy));

            } else {

                if (ApplicationData.IS_DEBUGGING_ON)
                    Logs.v(getPackageName(), log_tab + " genCardSaleReceipt ", true, true);

                wppreceipts = new ArrayList<>();
                wppreceipts.add(receiptUtility.printReceipt(reciptDataModel,
                        mCardSaleReceiptView.getSignature(),
                        AppSharedPrefrences.getAppSharedPrefrencesInstace().isPrintSignatureRequired(),
                        ReceiptUtilityWPP.TYPE.CARD, isCustomerCopy));
            }

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + " isBound " + isBound, true, true);

            if (!isBound){
                doBindService();
            }


            isPrintRequestInQue = true;

            if(wppreceipts.size() > 0)
            {
                mPrinterConnectionService.printReceipt(wppreceipts.get(0));
            }
        }
    }

/*
	private class AsyncTaskRunner extends AsyncTask<String, String, String> {


		@Override
		protected String doInBackground(String... params) {

			if (ApplicationData.IS_DEBUGGING_ON)
				Logs.v(getPackageName(), log_tab + "  ", true, true);

			isPrintingProcess = true;
			printresponse = mPrinterConnectionServiceWPP.printReceipt(wppreceipts.get(0));
			return "";
		}


		@Override
		protected void onPostExecute(String result) {

			if (ApplicationData.IS_DEBUGGING_ON)
				Logs.v(getPackageName(), log_tab + "  ", true, true);

			isPrintingProcess = false;

			if(printresponse.name().equals(PrintRespCode.Printer_PaperLack.name()))
			{
				Toast.makeText(getApplicationContext(),"out of paper" , Toast.LENGTH_LONG).show();
			}
			else if(printresponse.name().equals(PrintRespCode.Print_Success.name()))
			{
				if(!isCustomerCopy){

					showCustomerCopyDialog();
				}
				else{
					isCustomerCopy = false;
				}
			}
		}
		@Override
		protected void onPreExecute() { }
		@Override
		protected void onProgressUpdate(String... text) { }
	}
*/

    private void showCustomerCopyDialog()
    {
        try
        {
            final Dialog mDialogPrinter =  new Dialog(MswipeSignatureActivity.this, R.style.StyleCustomDlg_white);
            mDialogPrinter.setContentView(R.layout.activity_customdlg);
            mDialogPrinter.setCanceledOnTouchOutside(false);

            mDialogPrinter.setCancelable(true);

            // set the title
            TextView txttitle = (TextView) mDialogPrinter.findViewById(R.id.customdlg_sdk_TXT_title);
            txttitle.setText(title);

            // to set the message
            TextView txtMessage = (TextView) mDialogPrinter.findViewById(R.id.customdlg_sdk_TXT_Info);
            txtMessage.setText("Do you want customer copy?");

            RelativeLayout yes = (RelativeLayout) mDialogPrinter.findViewById(R.id.customdlg_sdk_RLT_yes);
            RelativeLayout no = (RelativeLayout) mDialogPrinter.findViewById(R.id.customdlg_sdk_RLT_no);

            ((Button) mDialogPrinter.findViewById(R.id.customdlg_sdk_BTN_yes)).setText(getResources().getString(R.string.ok));
            ((Button) mDialogPrinter.findViewById(R.id.customdlg_sdk_BTN_no)).setText(getResources().getString(R.string.cancel));

            yes.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    mDialogPrinter.dismiss();

                    if(isPrintingProcess) {
                        Toast.makeText(getApplicationContext(),getString(R.string.printing_in_process) , Toast.LENGTH_SHORT).show();
                    }else {
                        printWppReceipt(mCardSaleReceiptView);
                    }
                }
            });

            no.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    isCustomerCopy = false;
                    mDialogPrinter.dismiss();

                }
            });

            mDialogPrinter.show();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static byte[] hexToByteArray(String s) {
        if(s == null) {
            s = "";
        }
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        for(int i = 0; i < s.length() - 1; i += 2) {
            String data = s.substring(i, i + 2);
            bout.write(Integer.parseInt(data, 16));
        }
        return bout.toByteArray();
    }

    private static String toHexString(byte[] b) {
        if(b == null) {
            return "null";
        }
        String result = "";
        for (int i=0; i < b.length; i++) {
            result += Integer.toString( ( b[i] & 0xFF ) + 0x100, 16).substring( 1 );
        }
        return result;
    }


    class EMVProcessTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onCancelled() {

            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + " EmvOnlinePorcessTask onCancelled Task", true, true);


            //when the back tab is pressed
            if (mstrEMVProcessTaskType.equalsIgnoreCase("backbutton")) {

            } else if (mstrEMVProcessTaskType.equalsIgnoreCase("onlinesubmit")) {
                //dismissProgressDialog();
            } else if (mstrEMVProcessTaskType.equalsIgnoreCase("stopbluetooth")) {

                try {
                    this.notify();
                } catch (Exception ex) {
                }
                finish();
            }

            mstrEMVProcessTaskType = "";

        }

        @Override
        protected Void doInBackground(Void... unused) {

            //calling after this statement and canceling task will no meaning if you do some update database kind of operation
            //so be wise to choose correct place to put this condition
            //you can also put this condition in for loop, if you are doing iterative task
            //you should only check this condition in doInBackground() method, otherwise there is no logical meaning

            // if the task is not cancelled by calling LoginTask.clear(true), then make the thread wait for 10 sec and then
            //quit it self
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "EmvOnlinePorcessTask start doInBackground", true, true);

            int isec = 15;

            if (mstrEMVProcessTaskType.equalsIgnoreCase("stopbluetooth"))
                isec = 6;

            int ictr = 0;
            //it will wait for 15 sec or till the task is cancelled by the mSwiper routines.
            while (!isCancelled() & ictr < isec) {
                try {
                    Thread.sleep(500);
                } catch (Exception ex) {
                }
                ictr++;
            }
            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "EmvOnlinePorcessTask  end doInBackground", true, true);
            return null;
        }


        @Override
        protected void onPostExecute(Void unused) {

            if (applicationData.IS_DEBUGGING_ON)
                Logs.v(getPackageName(), log_tab + "EmvOnlinePorcessTask onPostExecute", true, true);

            //when the back tab is pressed
            if (mstrEMVProcessTaskType.equalsIgnoreCase("backbutton")) {

            } else if (mstrEMVProcessTaskType.equalsIgnoreCase("onlinesubmit")) {

            } else if (mstrEMVProcessTaskType.equalsIgnoreCase("stopbluetooth")) {
                finish();
            }

            mstrEMVProcessTaskType = "";

        }
    }


    private class TaskShowMultiplePairedDevices extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return "";
        }

        @Override
        protected void onPostExecute(String result) {


            final Dialog dlgPairedDevices = showAppCustomDialog(MswipeSignatureActivity.this, getResources().getString(R.string.mswisepadview_select_wisepad));
            dlgPairedDevices.setCancelable(true);

            final ArrayList<String> deviceNameList = new ArrayList<String>();

            for (int i = 0; i < pairedDevicesFound.size(); ++i)
            {
                deviceNameList.add(pairedDevicesFound.get(i).getName());
            }


            final ListView appListView = (ListView) dlgPairedDevices.findViewById(R.id.customapplicationdlg_LST_applications);

            appListView.setAdapter(new ListAdapter(MswipeSignatureActivity.this, deviceNameList));
            appListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    mRLTPrint.setEnabled(false);

                    if (mPrinterConnectionService != null)
                        mPrinterConnectionService.connectToWisePad(pairedDevicesFound.get(position));

                    dlgPairedDevices.dismiss();
                }

            });

            dlgPairedDevices.findViewById(R.id.customapplicationdlg_BTN_cancel).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    dlgPairedDevices.dismiss();
                }
            });

            dlgPairedDevices.show();

        }
    }


    public void doneWithCreditSale()
    {
        finish();
//        Intent intent = new Intent(MswipeSignatureActivity.this , BeveragesMenuFragment_Dine.class);
//        startActivity(intent);
        if (account_selection.toString().equals(getString(R.string.dine_nospace))) {
            Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
            if (cursor3.moveToFirst()) {
                String lite_pro = cursor3.getString(1);

                TextView tv = new TextView(MswipeSignatureActivity.this);
                tv.setText(lite_pro);

                if (tv.getText().toString().equals("Lite")) {
                    Intent intent = new Intent(MswipeSignatureActivity.this , BeveragesMenuFragment_Dine_l.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(MswipeSignatureActivity.this , BeveragesMenuFragment_Dine.class);
                    startActivity(intent);
                }
            }else {
                Intent intent = new Intent(MswipeSignatureActivity.this , BeveragesMenuFragment_Dine_l.class);
                startActivity(intent);
            }
//            Intent intent = new Intent(MswipeSignatureActivity.this , BeveragesMenuFragment_Dine.class);
//            startActivity(intent);
        }else {
            if (account_selection.toString().equals(getString(R.string.qsr_nospace))) {
                Intent intent = new Intent(MswipeSignatureActivity.this , BeveragesMenuFragment_Qsr.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(MswipeSignatureActivity.this , BeveragesMenuFragment_Retail.class);
                startActivity(intent);
            }
        }

    }

    private void showDialog(String message){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(message);
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();


    }

    public static Dialog showAppCustomDialog(Context context, String title) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.customappdlg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    return true;
                }

                return false;
            }
        });

        dialog.setCancelable(true);
        TextView txttitle = (TextView) dialog.findViewById(R.id.customapplicationdlg_TXT_title);
        txttitle.setText(title);

        return dialog;

    }


}
