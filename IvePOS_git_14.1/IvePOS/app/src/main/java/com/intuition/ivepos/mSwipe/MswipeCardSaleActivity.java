package com.intuition.ivepos.mSwipe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.intuition.ivepos.R;
import com.mswipetech.wisepad.sdk.MSWisepadController;
import com.mswipetech.wisepad.sdk.data.CardSaleResponseData;
import com.mswipetech.wisepad.sdk.data.LoginResponseData;
import com.mswipetech.wisepad.sdk.data.MSDataStore;
import com.mswipetech.wisepad.sdk.device.MSWisepadDeviceController;
import com.mswipetech.wisepad.sdk.device.MSWisepadDeviceControllerResponseListener;
import com.mswipetech.wisepad.sdk.device.WisePadConnection;
import com.mswipetech.wisepad.sdk.device.WisePadTransactionState;
import com.mswipetech.wisepad.sdk.listeners.MSWisepadControllerResponseListener;

public class MswipeCardSaleActivity extends MswipeWisepadView {

    /**
     * The mswipe controller instance used for calling up the api's
     */

    private String mUserId;
    private String mPassword;
    private String mReferenceId;
    private String mSessionToken;
    private MSWisepadController mMSWisepadController = null;
    /*
     *  parameters for cardsale request
     * */

    public String mAmount = "";
    public String mPhoneNo = "";
    public String mReceipt = "";
    public String mNotes = "";
    public String mAmexSecurityCode = "";
    public boolean mProduction = true;
    private int mProcessType = 0;


    private String mExtraNote1= "";
    private String mExtraNote2 = "";
    private String mExtraNote3 = "";
    private String mExtraNote4 = "";
    private String mExtraNote5 = "";
    private String mExtraNote6 = "";
    private String mExtraNote7 = "";
    private String mExtraNote8 = "";
    private String mExtraNote9 = "";
    private String mExtraNote10 = "";

    private String mStrLatitude = "";
    private String mStrLongitude = "";

    public EMVProcessTask mEMVProcessTask = null;

    public enum EMVPPROCESSTASTTYPE{
        NO_TASK, STOP_BLUETOOTH, BACK_BUTTON,
        ONLINE_SUBMIT, SHOW_SIGNATURE}

    public EMVPPROCESSTASTTYPE mEMVPPROCESSTASTTYPE = EMVPPROCESSTASTTYPE.NO_TASK;

    /**
     * the wise pad has to be disconnected if its not required and also when the application closes
     * the wise pad has to be disconnected,
     * this will see to that the dis-connecting to the wisepad does has execute multiple times
     */

    public boolean onDoneWithCreditSaleCalled = false;
    public boolean mIsIgnoreBackDevicekeyOnCardProcess = false;

    /**
     * in certain scenarios the wise pad where in a likely case becomes non responsive, at that precise
     * moment a task is called and with in the elapsed time if their no communication back to the app this task will
     * un-initiate the communication link from the wisepad
     */

    boolean isPinBypassed = false;
    private long mBackPressed=0;

    TextView lblAmtMsg = null;
    TextView txtProgMsg = null;
    LinearLayout mLINCancel = null;

    boolean isSaleWithCash = false;
    boolean isPreAuth = false;

    LoginResponseData mLoginResponseData = null;
    CardSaleResponseData mCardSaleResponseData = null;
    Intent intent = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mswipe_creditsale_view);
        initViews();

        mEMVPPROCESSTASTTYPE = EMVPPROCESSTASTTYPE.NO_TASK;
        new RequestTask().execute();
    }

    private void initViews()
    {

        isSaleWithCash = getIntent().getBooleanExtra("salewithcash", false);
        isPreAuth = getIntent().getBooleanExtra("preauthsale", false);

        String amount = (getIntent().getStringExtra("amount") == null ? "" : getIntent().getStringExtra("amount"));

        lblAmtMsg = (TextView) findViewById(R.id.creditsale_totalamountview_LBL_totalamount);
        lblAmtMsg.setText(amount);

        TextView txtHeading = ((TextView) findViewById(R.id.topbar_LBL_heading));

        if(isPreAuth){
            txtHeading.setText(getResources().getString(R.string.preauth));
        }
        else if (isSaleWithCash){
            txtHeading.setText(getResources().getString(R.string.cash_at_pos));
        }
        else {
            txtHeading.setText(getResources().getString(R.string.card_sale));
        }

        mLINCancel = (LinearLayout) findViewById(R.id.topbar_LNR_topbar_cancel);
        mLINCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cancelTransaction();
            }
        });

        txtProgMsg = (TextView) findViewById(R.id.creditsale_swiperview_EDT_swipe_progmsg);
        Button mBtnSwipe = (Button) findViewById(R.id.creditsale_swiperview_BTN_swipe);
        mBtnSwipe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                setWisepadStatusMsg(getString(R.string.connecting));
                connect();

            }
        });

        setWisepadStatusMsg(getString(R.string.connecting));
    }

    //@Override
    public void setWisepadStatusMsg(String msg)
    {
        txtProgMsg.setText(msg);

    }


    @Override
    protected void onStop()
    {
        /*if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName, " ", true, true);*/

        /*stops the gateway connection */

        if (!onDoneWithCreditSaleCalled)
        {

            /*if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName,  "onDoneWithCreditSaleCalled " + onDoneWithCreditSaleCalled, true, true);*/

            doneWithCreditSale(EMVPPROCESSTASTTYPE.STOP_BLUETOOTH);
            super.onStop();

        } else {
            super.onStop();
        }

        mProgressDialog = null;
    }

    class RequestTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
           /* try {
                int ictr = 0;
                while (ictr < 2) {
                    Thread.sleep(500);
                    ictr++;
                }

            } catch (Exception ex) {
            }*/
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            mAmount = (getIntent().getStringExtra("amount") == null ? "" : getIntent().getStringExtra("amount"));
            mPhoneNo = (getIntent().getStringExtra("mobileno") == null ? "" : getIntent().getStringExtra("mobileno"));
            mReceipt = (getIntent().getStringExtra("receiptno") == null ? "" : getIntent().getStringExtra("receiptno"));
            mNotes = (getIntent().getStringExtra("notes") == null ? "" : getIntent().getStringExtra("notes"));
            mProduction = getIntent().getBooleanExtra("production", true);

            mExtraNote1 = (getIntent().getStringExtra("extra1") == null ? "" : getIntent().getStringExtra("extra1"));
            mExtraNote2 = (getIntent().getStringExtra("extra2") == null ? "" : getIntent().getStringExtra("extra2"));
            mExtraNote3 = (getIntent().getStringExtra("extra3") == null ? "" : getIntent().getStringExtra("extra3"));
            mExtraNote4 = (getIntent().getStringExtra("extra4") == null ? "" : getIntent().getStringExtra("extra4"));
            mExtraNote5 = (getIntent().getStringExtra("extra5") == null ? "" : getIntent().getStringExtra("extra5"));
            mExtraNote6 = (getIntent().getStringExtra("extra6") == null ? "" : getIntent().getStringExtra("extra6"));
            mExtraNote7 = (getIntent().getStringExtra("extra7") == null ? "" : getIntent().getStringExtra("extra7"));
            mExtraNote8 = (getIntent().getStringExtra("extra8") == null ? "" : getIntent().getStringExtra("extra8"));
            mExtraNote9 = (getIntent().getStringExtra("extra9") == null ? "" : getIntent().getStringExtra("extra9"));
            mExtraNote10 = (getIntent().getStringExtra("extra10") == null ? "" : getIntent().getStringExtra("extra10"));

            mStrLatitude = (getIntent().getStringExtra("latitude") == null ? "" : getIntent().getStringExtra("latitude"));
            mStrLongitude = (getIntent().getStringExtra("longitude") == null ? "" : getIntent().getStringExtra("longitude"));

            setWisepadAmount();

            MSWisepadController.GATEWAY_ENVIRONMENT gateway = MSWisepadController.GATEWAY_ENVIRONMENT.LABS;

            if (mProduction){
                gateway = MSWisepadController.GATEWAY_ENVIRONMENT.PRODUCTION;
            }

            AppSharedPrefrences.getAppSharedPrefrencesInstace().setGatewayEnvironment(gateway);

            mMSWisepadController = MSWisepadController.getSharedMSWisepadController(MswipeCardSaleActivity.this,
                    AppSharedPrefrences.getAppSharedPrefrencesInstace().getGatewayEnvironment(),
                    MSWisepadController.NETWORK_SOURCE.WIFI,
                    null);

            mUserId = (getIntent().getStringExtra("username") == null ? "" : getIntent().getStringExtra("username"));
            mPassword = (getIntent().getStringExtra("password") == null ? "" : getIntent().getStringExtra("password"));

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName, "username " + mUserId, true, true);

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName, "mPassword " + mPassword, true, true);

            mReferenceId = AppSharedPrefrences.getAppSharedPrefrencesInstace().getReferenceId();
            mSessionToken = AppSharedPrefrences.getAppSharedPrefrencesInstace().getSessionToken();


            /* this checks are for supporting the old sdk since there the app sends reference id and session tokenizer*/
            if (mReferenceId.length() > 0 && mSessionToken.length() > 0) {

                if (mUserId.length() < 10) {

                    Intent intent = new Intent();
                    intent.putExtra("status", false);
                    intent.putExtra("statusMessage", "minimum length userid should be 10 characters");
                    setResult(RESULT_CANCELED, intent);
                    finish();

                } else if (mUserId.startsWith("0")) {

                    Intent intent = new Intent();
                    intent.putExtra("status", false);
                    intent.putExtra("statusMessage", "user id cannot start with 0");
                    setResult(RESULT_CANCELED, intent);
                    finish();

                } else if (mPassword.length() == 0 || mPassword.length() < 6) {

                    Intent intent = new Intent();
                    intent.putExtra("status", false);
                    intent.putExtra("statusMessage", "minimum length password should be 6 characters");
                    setResult(RESULT_CANCELED, intent);
                    finish();

                } else {

                    String userId = AppSharedPrefrences.getAppSharedPrefrencesInstace().getUserId();
                    String password = AppSharedPrefrences.getAppSharedPrefrencesInstace().getUserPassword();

                    if (mReferenceId.length() == 0
                            || mSessionToken.length() == 0
                            || userId.length() == 0
                            || password.length() == 0) {

                        processLoginRequest();
                    }
                    else if (userId.equalsIgnoreCase(mUserId)
                            && password.equalsIgnoreCase(mPassword)) {
                        mAutoConnect = true;
                        initiateWisepadConnection();
                    }
                    else {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MswipeCardSaleActivity.this, R.style.MswipeDialogTheme);
                        builder1.setMessage(getString(R.string.setmessage34));
                        builder1.setCancelable(false);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        processLoginRequest();


                                    }
                                });

                        builder1.setNegativeButton(
                                "Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                        Intent intent = new Intent();
                                        intent.putExtra("status", false);
                                        intent.putExtra("statusMessage", getString(R.string.transaction_cancel));
                                        setResult(RESULT_CANCELED, intent);
                                        finish();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                }
            }
            else{

                if (mUserId.length() > 0 && mPassword.length() > 0) {

                    processLoginRequest();
                }
                else{

                    Intent intent = new Intent();
                    intent.putExtra("status", false);
                    intent.putExtra("statusMessage", "invalid username or password");
                    setResult(RESULT_CANCELED, intent);
                    finish();
                }
            }
        }
    }


    private void processLoginRequest() {

        try {

            showProgressDialog();

            mMSWisepadController.authenticateMerchant(
                    mUserId,
                    mPassword,
                    new MSWisepadControllerResponseListenerObserver());


        } catch (Exception ex) {

        } finally {
        }
    }


    @Override
    public void processCardSaleOnline()
    {
        showProgressDialog();

        if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName, "ReferenceId " + AppSharedPrefrences.getAppSharedPrefrencesInstace().getReferenceId(), true, true);

        if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName, "SessionToken " +  AppSharedPrefrences.getAppSharedPrefrencesInstace().getSessionToken(), true, true);

        MSWisepadController.getSharedMSWisepadController(this,
                AppSharedPrefrences.getAppSharedPrefrencesInstace().getGatewayEnvironment(),
                AppSharedPrefrences.getAppSharedPrefrencesInstace().getNetworkSource(),
                null).processCardSaleOnline(
                AppSharedPrefrences.getAppSharedPrefrencesInstace().getReferenceId(),
                AppSharedPrefrences.getAppSharedPrefrencesInstace().getSessionToken(),
                removeChar(mAmount,','),
                "0.00",
                "+91" + mPhoneNo,
                mReceipt,
                "",
                mNotes,
                false,
                false,
                mAmexSecurityCode,
                0,
                0,
                "",
                mExtraNote1,
                mExtraNote2,
                mExtraNote3,
                mExtraNote4,
                mExtraNote5,
                mExtraNote6,
                mExtraNote7,
                mExtraNote8,
                mExtraNote9,
                mExtraNote10,
                mStrLatitude,
                mStrLongitude,
                new MSWisepadControllerResponseListenerObserver());

    }

    /**
     * MSWisepadControllerResponseListenerObserver
     * The mswipe overridden class  observer which listens to the responses for the mswipe sdk function requests
     */
    class MSWisepadControllerResponseListenerObserver implements MSWisepadControllerResponseListener {

        /**
         * onReponseData
         * The response data notified back to the call back function
         *
         * @param aMSDataStore the generic mswipe data store, this instance is refers to Receipt information, so this
         *                     need be converted back to CardSaleReceiptResponseData to access the receipt response data
         * @return
         */
        public void onReponseData(MSDataStore aMSDataStore) {


            if(aMSDataStore instanceof LoginResponseData){

                dismissProgressDialog();

                mLoginResponseData = (LoginResponseData) aMSDataStore;

                if (mLoginResponseData.getResponseStatus()) {

                    mReferenceId = mLoginResponseData.getReferenceId();
                    mSessionToken = mLoginResponseData.getSessionTokeniser();

                    saveLoginDetails();
                    mAutoConnect = true;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            initiateWisepadConnection();
                        }
                    }, 3000);

                } else {

                    Intent intent = new Intent();
                    intent.putExtra("status", false);
                    intent.putExtra("statusMessage", mLoginResponseData.getResponseFailureReason());
                    setResult(RESULT_CANCELED, intent);
                    finish();

                }
            }
            else if(aMSDataStore instanceof CardSaleResponseData) {

                mCardSaleResponseData = (CardSaleResponseData) aMSDataStore;

                if (ApplicationData.IS_DEBUGGING_ON)
                    Logs.v(ApplicationData.packName, " mProcessType " + mProcessType, true, true);

                if(mProcessType == 1){
                    dismissProgressDialog();

                    Intent intent = new Intent();
                    intent.putExtra("statusMessage", "transaction is pending for approval");

                    setResult(RESULT_OK, intent);
                    doneWithCreditSale(EMVPPROCESSTASTTYPE.ONLINE_SUBMIT);

                }
                else {

                    if (mCardSaleResponseData.getResponseStatus()) {

                        doUnbindMswipeWisepadDeviceService();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                finishCardSale();

                            }
                        },5000);
                    }
                    else {

                        dismissProgressDialog();

                        Intent intent = new Intent();
                        intent.putExtra("status", false);
                        intent.putExtra("statusMessage", mCardSaleResponseData.getResponseFailureReason());
                        intent.putExtra("cardSaleResponseData", mCardSaleResponseData);
                        setResult(RESULT_CANCELED, intent);
                        finish();
                        //  showSignature();

                    }
                }
            }
        }
    }


    public void finishCardSale()
    {
        dismissProgressDialog();

        doneWithCreditSale(EMVPPROCESSTASTTYPE.STOP_BLUETOOTH);

        playSound(100, R.raw.approved);

        Intent intent = new Intent();
        intent.putExtra("status", true);
        intent.putExtra("statusMessage", mCardSaleResponseData.getResponseSuccessMessage());
        intent.putExtra("errMsg", mCardSaleResponseData.getResponseFailureReason());
        intent.putExtra("RRNo", mCardSaleResponseData.getRRNO());
        intent.putExtra("AuthCode", mCardSaleResponseData.getAuthCode());
        intent.putExtra("TVR", mCardSaleResponseData.getTVR());
        intent.putExtra("TSI", mCardSaleResponseData.getTSI());
        intent.putExtra("cardSaleResponseData", mCardSaleResponseData);
        intent.putExtra("receiptDetail", mCardSaleResponseData.getStrReceiptData());
        setResult(RESULT_OK, intent);
        finish();

    }

    public void saveLoginDetails() {

        try{
            AppSharedPrefrences.getAppSharedPrefrencesInstace().setUserId(mUserId);
            AppSharedPrefrences.getAppSharedPrefrencesInstace().setUserPassword(mPassword);
            AppSharedPrefrences.getAppSharedPrefrencesInstace().setSessionToken(mSessionToken);
            AppSharedPrefrences.getAppSharedPrefrencesInstace().setReferenceId(mReferenceId);

        }catch(Exception e){

        }
    }

    public String removeChar(String s, char c) {

        String r = "";

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != c)
                r += s.charAt(i);
        }

        return r;
    }


    /*************************************************************/
    /*************************CARDSALE****************************/
    /*************************************************************/

    public void connect(){

        processDeviceConnetionWithAutoInitiation(AutoInitiationDevice.CHECKCARD);

    }

    public void cancelTransaction(){

        doneWithCreditSale(EMVPPROCESSTASTTYPE.STOP_BLUETOOTH);

    }

    int pinCount = 0;
    /**setWisePadConnectionState
     *	set the views to the connection state
     * @param
     * wisePadConnection
     * 		The state of the wise pad connection
     * @return
     */
    @Override
    public void setWisePadConnectionStateResult(WisePadConnection wisePadConnection)
    {

        if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName, " wisePadConnection " + wisePadConnection, true, true);

        if(wisePadConnection == WisePadConnection.WisePadConnection_CONNECTING)
        {
            setWisepadStatusMsg(getString(R.string.connecting_device));
            setIgnoreBackDevicekeyOnCardProcess(false);

        }
        else if(wisePadConnection == WisePadConnection.WisePadConnection_NOT_CONNECTED){

            if (mEMVPPROCESSTASTTYPE == EMVPPROCESSTASTTYPE.BACK_BUTTON
                    || mEMVPPROCESSTASTTYPE == EMVPPROCESSTASTTYPE.ONLINE_SUBMIT) {
                if (mEMVProcessTask != null && mEMVProcessTask.getStatus() != AsyncTask.Status.FINISHED)
                    mEMVProcessTask.cancel(true);
            }


            setIgnoreBackDevicekeyOnCardProcess(false);

        }
        else if(wisePadConnection == WisePadConnection.WisePadConnection_CONNECTED){


            setWisepadStatusMsg(getString(R.string.device_connected));

            setIgnoreBackDevicekeyOnCardProcess( false);

        }
        else if(wisePadConnection == WisePadConnection.WisePadConnection_DEVICE_NOTFOUND){

            setWisepadStatusMsg(getString(R.string.device_not_found));

            if (mEMVPPROCESSTASTTYPE == EMVPPROCESSTASTTYPE.BACK_BUTTON
                    || mEMVPPROCESSTASTTYPE == EMVPPROCESSTASTTYPE.ONLINE_SUBMIT) {
                if (mEMVProcessTask != null && mEMVProcessTask.getStatus() != AsyncTask.Status.FINISHED)
                    mEMVProcessTask.cancel(true);
            }

            setIgnoreBackDevicekeyOnCardProcess( false);

        }
        else if(wisePadConnection == WisePadConnection.WisePadConnection_DIS_CONNECTED){

            setWisepadStatusMsg(getString(R.string.device_disconnected));


            if (mEMVPPROCESSTASTTYPE == EMVPPROCESSTASTTYPE.BACK_BUTTON
                    || mEMVPPROCESSTASTTYPE == EMVPPROCESSTASTTYPE.ONLINE_SUBMIT
                    || mEMVPPROCESSTASTTYPE == EMVPPROCESSTASTTYPE.STOP_BLUETOOTH) {
                if (mEMVProcessTask != null && mEMVProcessTask.getStatus() != AsyncTask.Status.FINISHED)
                    mEMVProcessTask.cancel(true);
            }


            setIgnoreBackDevicekeyOnCardProcess( false);

        }
        else if(wisePadConnection == WisePadConnection.WisePadConnection_NO_PAIRED_DEVICES_FOUND){

            if (mEMVPPROCESSTASTTYPE == EMVPPROCESSTASTTYPE.BACK_BUTTON
                    || mEMVPPROCESSTASTTYPE == EMVPPROCESSTASTTYPE.ONLINE_SUBMIT
                    || mEMVPPROCESSTASTTYPE == EMVPPROCESSTASTTYPE.STOP_BLUETOOTH) {
                if (mEMVProcessTask != null && mEMVProcessTask.getStatus() != AsyncTask.Status.FINISHED)
                    mEMVProcessTask.cancel(true);
            }

            setIgnoreBackDevicekeyOnCardProcess( false);

            AlertDialog.Builder builder1 = new AlertDialog.Builder(MswipeCardSaleActivity.this, R.style.MswipeDialogTheme);
            builder1.setMessage(getResources().getString(R.string.no_paired_wisePad_found_please_pair_the_wisePad_from_your_phones_bluetooth_settings_and_try_again));
            builder1.setCancelable(false);

            builder1.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            doneWithCreditSale(EMVPPROCESSTASTTYPE.STOP_BLUETOOTH);

                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

        }
        else if(wisePadConnection == WisePadConnection.WisePadConnection_DEVICE_DETECTED){

            setWisepadStatusMsg(getString(R.string.device_detected));
        }
        else if(wisePadConnection == WisePadConnection.WisePadConnection_FAIL_TO_START_BT){

            setWisepadStatusMsg(getString(R.string.fail_to_start_bluetooth_v2));
        }
        else if(wisePadConnection == WisePadConnection.WisePadConnection_BLUETOOTH_DISABLED){

            setWisepadStatusMsg(getString(R.string.creditsaleswiperfragment_enable_bluetooth));
        }
        else if(wisePadConnection == WisePadConnection.WisePadConnection_MULTIPLE_PAIRED_DEVCIES_FOUND)
        {

        }

    }

    @Override
    public void setWisePadStateInfo(String message){

        if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName, "message: " + message, true, true);

        if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName, "getWisePadTranscationState: " + getWisePadTranscationState(), true, true);

        if (getWisePadTranscationState() == WisePadTransactionState.WisePadTransactionState_WaitingForCard) {

            isPinBypassed = false;
            setWisepadStatusMsg(message);
        }
        else if (getWisePadTranscationState() == WisePadTransactionState.WisePadTransactionState_Pin_Entry_Request) {

            isPinBypassed = false;

            if(message.equalsIgnoreCase(getString(R.string.bypass)))
                setWisepadStatusMsg(message);
            else
                setWisepadStatusMsg(getString(R.string.creditsale_swiperview_lable_enter_pin));

            showPinpad();

        }
        else if (getWisePadTranscationState() == WisePadTransactionState.WisePadTransactionState_Pin_Entry_Results) {

            isPinBypassed = false;
            pinCount = 0;
            setWisepadStatusMsg(message);

        }
        else if (getWisePadTranscationState() == WisePadTransactionState.WisePadTransactionState_ICC_SetAmount) {

            setWisepadStatusMsg(message);

        }
        else if (getWisePadTranscationState() == WisePadTransactionState.WisePadTransactionState_CheckCard) {

            isPinBypassed = false;
            setWisepadStatusMsg(message);

        }
        else if (getWisePadTranscationState() == WisePadTransactionState.WisePadTransactionState_CancelCheckCard) {


            setWisepadStatusMsg(message);

        }
        else if (getWisePadTranscationState() == WisePadTransactionState.WisePadTransactionState_Ready) {


            setWisepadStatusMsg(message);
        }
        else if(getWisePadTranscationState() == WisePadTransactionState.WisePadTransactionState_Pin_Entry_Asterisk){

            txtProgMsg.setVisibility(View.VISIBLE);

            int astricIndex = 0;
            try {
                astricIndex = Integer.parseInt(message);
            }catch (Exception e){

            }

            if(astricIndex == 0){

                pinCount = 0;
            }
            else {

                pinCount = pinCount+1;
            }

            if(pinCount == 0) {
                setWisepadStatusMsg(getString(R.string.creditsale_swiperview_lable_enter_pin));
            }
            else {

                String strPin = "";
                String delimiter = "";
                for (int i = 1; i <= pinCount && i <= 6; i++) {

                    strPin = strPin + delimiter + "*";
                    delimiter = " ";
                }

                setWisepadStatusMsg(strPin);
            }

        }
        else {

            txtProgMsg.setVisibility(View.VISIBLE);
            setWisepadStatusMsg(message);
        }

    }

    @Override
    public void setWisePadStateErrorInfo(String message)
    {

        pinCount = 0;
        if (getWisePadTranscationState() == WisePadTransactionState.WisePadTransactionState_Pin_Entry_Results) {

            isPinBypassed = false;
            setWisepadStatusMsg(message);

        }
        else if (getWisePadTranscationState() == WisePadTransactionState.WisePadTransactionState_Ready) {

            if(message.equalsIgnoreCase(getString(R.string.device_busy)))
                setWisepadStatusMsg(getString(R.string.creditsaleswiperfragment_status)+": "+ message);
            else
                setWisepadStatusMsg(message);
        }
        else {

            setWisepadStatusMsg(message);

        }
    }


//    public void setWisepadStatusMsg(String message){}
    /**
     * this will get called only in the total amount and when the total amount will bot be shown i.w in the amount screen
     */
    @Override
    public void setWisepadAmount()
    {

        /* To start the gateway connection*/

        if (mMSWisepadDeviceController != null)
            mMSWisepadDeviceController.setAmount(mAmount, MSWisepadDeviceControllerResponseListener.TransactionType.GOODS);

    }


    @Override
    public void setWisepadWaitingForCardResult() {

        setIgnoreBackDevicekeyOnCardProcess(true);
    }
    @Override
    public void showCardDetails()
    {
        // TODO Auto-generated method stub

        if (getWisePadTranscationState() == WisePadTransactionState.WisePadTransactionState_ICC_Online_Process
                || getWisePadTranscationState() == WisePadTransactionState.WisePadTransactionState_MAG_Online_Process) {

            // TODO Auto-generated method stub


            processCardSaleOnline();

        }
    }
    /**
     * We are finishing all running processes.
     * @param processTaskType
     * @return
     */
    public void doneWithCreditSale(EMVPPROCESSTASTTYPE processTaskType)
    {
        if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,  "doneWithCreditSale  processTaskType " + processTaskType, true, true);

        if (!onDoneWithCreditSaleCalled)
        {

            if (mEMVProcessTask != null && mEMVProcessTask.getStatus() != AsyncTask.Status.FINISHED)
                mEMVProcessTask.cancel(true);

            mEMVPPROCESSTASTTYPE = processTaskType;
            mEMVProcessTask = new EMVProcessTask(); //every time create new object, as AsynTask will only be executed one time.
            mEMVProcessTask.execute();

            onDoneWithCreditSaleCalled = true;
        }
    }

    class EMVProcessTask extends AsyncTask<Void, Integer, Void>
    {
        @Override
        protected void onCancelled()
        {

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName,  "onCancelled mEMVPPROCESSTASTTYPE " + mEMVPPROCESSTASTTYPE, true, true);

            //when the back tab is pressed
            if (mEMVPPROCESSTASTTYPE == EMVPPROCESSTASTTYPE.BACK_BUTTON)
            {
                Intent intent = new Intent();
                intent.putExtra("status", false);
                intent.putExtra("statusMessage", getString(R.string.setmessage6));
                setResult(RESULT_CANCELED, intent);
                finish();
            }
            else if (mEMVPPROCESSTASTTYPE == EMVPPROCESSTASTTYPE.ONLINE_SUBMIT)
            {
                finish();
            }
            else if (mEMVPPROCESSTASTTYPE == EMVPPROCESSTASTTYPE.STOP_BLUETOOTH)
            {

                Intent intent = new Intent();
                intent.putExtra("status", false);
                intent.putExtra("statusMessage", getString(R.string.setmessage6));
                setResult(RESULT_CANCELED, intent);
                finish();
            }

            mEMVPPROCESSTASTTYPE = EMVPPROCESSTASTTYPE.NO_TASK;

        }

        @Override
        protected Void doInBackground(Void... unused) {

            //calling after this statement and canceling task will no meaning if you do some update database kind of operation
            //so be wise to choose correct place to put this condition
            //you can also put this condition in for loop, if you are doing iterative task
            //you should only check this condition in doInBackground() method, otherwise there is no logical meaning

            // if the task is not cancelled by calling LoginTask.mswipe_cancel(true), then make the thread wait for 10 sec and then
            //quit it self


            int isec = 4;

            if (mEMVPPROCESSTASTTYPE == EMVPPROCESSTASTTYPE.STOP_BLUETOOTH)
                isec = 2;

            if (getWisePadConnectionState() == WisePadConnection.WisePadConnection_NO_PAIRED_DEVICES_FOUND)
                isec = 0;

            int ictr = 0;


            //it will wait for 15 sec or till the task is cancelled by the mSwiper routines.
            while (!isCancelled() & ictr < isec) {
                try {
                    Thread.sleep(500);
                } catch (Exception ex) {
                }
                ictr++;
            }

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName,  "EmvOnlinePorcessTask  end doInBackground", true, true);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused)
        {
            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName,  "onPostExecute mEMVPPROCESSTASTTYPE " + mEMVPPROCESSTASTTYPE, true, true);

            //when the back tab is pressed
            if (mEMVPPROCESSTASTTYPE == EMVPPROCESSTASTTYPE.BACK_BUTTON)
            {

                Intent intent = new Intent();
                intent.putExtra("status", false);
                intent.putExtra("statusMessage", getString(R.string.setmessage6));
                setResult(RESULT_CANCELED, intent);
                finish();
            }
            else if (mEMVPPROCESSTASTTYPE == EMVPPROCESSTASTTYPE.ONLINE_SUBMIT)
            {
                finish();
            }
            else if (mEMVPPROCESSTASTTYPE == EMVPPROCESSTASTTYPE.STOP_BLUETOOTH)
            {
                Intent intent = new Intent();
                intent.putExtra("status", false);
                intent.putExtra("statusMessage", getString(R.string.setmessage6));
                setResult(RESULT_CANCELED, intent);
                finish();
            }else if (mEMVPPROCESSTASTTYPE == EMVPPROCESSTASTTYPE.SHOW_SIGNATURE)
            {
                finish();
                startActivity(intent);
            }



            mEMVPPROCESSTASTTYPE = EMVPPROCESSTASTTYPE.NO_TASK;
        }
    }

    /**
     * @description
     *     We are handling backbutton manually based on screen position.
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName,  "onKeyDown  keyCode " + keyCode, true, true);

            // if the state of the devcie connection is no
            // DEVCIE_NO_PAIRED_DEVICES then from the amount screen it will take
            // to steps screen,
            // from here the below will restrict moving back.

            if(isIgnoreBackDevicekeyOnCardProcess())
            {
                if (mBackPressed + 2000 > System.currentTimeMillis()) {

                    doneWithCreditSale(EMVPPROCESSTASTTYPE.STOP_BLUETOOTH);

                }
                else {
                    Toast.makeText(this, getResources().getString(R.string.processing_card_in_progress_press_back_key_twice_in_succession_to_terminate_the_transaction), Toast.LENGTH_SHORT).show();
                    mBackPressed = System.currentTimeMillis();
                }
            }
            else{
                doneWithCreditSale(EMVPPROCESSTASTTYPE.STOP_BLUETOOTH);

            }


            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public boolean isIgnoreBackDevicekeyOnCardProcess() {
        return mIsIgnoreBackDevicekeyOnCardProcess;
    }

    public void setIgnoreBackDevicekeyOnCardProcess(boolean ignoreBackDevicekeyOnCardProcess) {
        this.mIsIgnoreBackDevicekeyOnCardProcess = ignoreBackDevicekeyOnCardProcess;
    }

    public void showPinpad()
    {
        if(mMSWisepadDeviceController != null)
        {
            mMSWisepadDeviceController.showPinPad(MSWisepadDeviceController.Orientation.POTRAIT,this);
        }
    }

    public void showSignature() {

        if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName, "", true, true);


        if(mCardSaleResponseData.getResponseStatus())
        {
            intent = new Intent(MswipeCardSaleActivity.this, MswipeSignatureActivity.class);
        }
        else {
            intent = new Intent(MswipeCardSaleActivity.this, MswipeDeclineActivity.class);
        }

        intent.putExtra("Title", getResources().getString(R.string.card_sale));
        intent.putExtra("cardSaleResponseData", mCardSaleResponseData);

        doneWithCreditSale(EMVPPROCESSTASTTYPE.SHOW_SIGNATURE);

    }

    private void playSound(long delay, int soundfile) {

        new playBeepTask(soundfile).execute(delay);
        if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName, "", true, true);
    }

    private class playBeepTask extends AsyncTask<Long, Void, Void> {

        int soundfile;

        playBeepTask(int soundfile) {
            this.soundfile = soundfile;
        }

        @Override
        protected Void doInBackground(Long... params) {
            // TODO Auto-generated method stub
            try {
                Thread.sleep(params[0]);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            MediaPlayer mp;
            mp = MediaPlayer.create(MswipeCardSaleActivity.this, soundfile);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    mp.reset();
                    mp.release();
                    mp = null;
                }
            });
            mp.start();
            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName, "", true, true);
        }

    }
}
