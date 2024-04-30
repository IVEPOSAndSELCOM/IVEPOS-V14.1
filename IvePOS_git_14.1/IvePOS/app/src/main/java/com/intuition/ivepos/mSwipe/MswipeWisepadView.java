package com.intuition.ivepos.mSwipe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.InputType;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.mswipetech.wisepad.sdk.data.CardData;
import com.mswipetech.wisepad.sdk.device.MSWisepadDeviceController;
import com.mswipetech.wisepad.sdk.device.MSWisepadDeviceController.LocalBinder;
import com.mswipetech.wisepad.sdk.device.MSWisepadDeviceController.WisepadCheckCardMode;
import com.mswipetech.wisepad.sdk.device.MSWisepadDeviceControllerResponseListener;
import com.mswipetech.wisepad.sdk.device.WisePadConnection;
import com.mswipetech.wisepad.sdk.device.WisePadTransactionState;

import java.util.ArrayList;
import java.util.Hashtable;
import com.intuition.ivepos.R;

public class MswipeWisepadView extends Activity {

    /**
     * the instance to communicate with the device service, this get initializes when the service successfully binds to
     * the activity.
     */

    public MSWisepadDeviceController mMSWisepadDeviceController = null;

    /*the callback listener with observes all the communications from the wise pad controller*/

    public MSWisepadDeviceObserver mMSWisepadDeviceObserver = null;
    public boolean mIsMSWisepasConnectionServiceBound;

    ProgressDialog mProgressDialog = null;

    /**
     * the details of the card used for the transaction returned my the mswipe wisepad device controller
     * through the listener object
     */

    public CardData mCardData = new CardData();

    /**
     * progress for all the transaction which involve chip card, since for the EMV card's the wise pad need to
     * communicate back the information from the user to the card, this will display the progress for this activities.
     *
     */


    /**
     * The amex card security code located at the back for the card has to be punched in when the mswipe wisepad
     * detects the card been used is Amex card through the callback function onReturnWisePadOfflineCardTransactionResultsCARDSCHEMERRESULTS.MCR_AMEXCARD
     */

    public String mCardSaleDlgTitle = "";


    public boolean mAutoConnect = false;
    public boolean mCallCheckCardAfterConnection = true;
    public boolean mShowMultiDeviceList = true;

    /*this will initialize the firm ware upate sdk*/
    public boolean mAllowFirmwareUpdatge = false;
    /*using this will be able to know whether  merchant is pressed amount next button for swipe or not
     based on this we are setting the amount*/
    protected boolean isConnetCalled = false;

    /*storing the laste topbar_img_host_active wisepad id so that for next time it will connect automatically to this devise*/
    protected String mBlueToothMcId = "";

    /* when the flag from the login response for pin bypass is set to true then the trx should
     * be allowed to online if not then a bypassed should be displayed to the user
     * this should be re-initialzed in insert or checkcard or start emv since for the chcekc crad and
     * mag card straint online mag card would be called and not delegate to set this back wouldn't
     * be called
     *  */
    public boolean mIsPinBypassed = false;

    public enum AutoInitiationDevice{CHECKCARD,START,DEVICE_INFO}

    public String mAmexSecurityCode = "";

    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        }catch(Exception e){}

        mMSWisepadDeviceObserver = new MSWisepadDeviceObserver();

    }

    public void showProgressDialog()
    {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.processing));
        mProgressDialog.show();
    }

    public void dismissProgressDialog()
    {
        if(mProgressDialog != null)
            mProgressDialog.dismiss();
    }

    @Override
    protected void onStart()
    {

        if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,  " *********** onStart ", true, true);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                /**
                 * binding the service will start the service, and notifies back to the bound objects and this will
                 * unable the interactions with the service
                 *
                 */

                try
                {
                    doBindMswipeWisepadDeviceService();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }, 1000);

        super.onStart();
    }

    @Override
    protected void onStop()
    {
        if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName, "onStop", true, true);
        //unbinding the service , when the app no longer requires the connection to the wisepad,
        //this will disconnect the connection to the wise pad
        try
        {
            doUnbindMswipeWisepadDeviceService();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        super.onStop();
    }


    /**
     * @description
     *        Initiates the wisepad device service which run in the back ground and controls the connections
     *        and disconnection and the application interactions independently of the application.
     */

    void doBindMswipeWisepadDeviceService()
    {
        bindService(new Intent(this, MSWisepadDeviceController.class), mMSWisepadDeviceControllerService, Context.BIND_AUTO_CREATE);
    }

    /**
     * @description
     *        Stops the wisepad service this need to be called when the wisepad is no more required, and
     *        this function should be called in onStop, this instance will be called when the app moves to
     *        back ground
     */

    public void doUnbindMswipeWisepadDeviceService()
    {

        if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,  "wisePadConnection ", true, true);


        if (mIsMSWisepasConnectionServiceBound)
        {
            unbindService(mMSWisepadDeviceControllerService);

            mIsMSWisepasConnectionServiceBound = false;
        }
    }


    /**
     * @description
     *       The wisepad service callback listener, when the service is stopped or started
     *       the service connection object will be notified
     */

    private ServiceConnection mMSWisepadDeviceControllerService = new ServiceConnection()
    {
        public void onServiceConnected(ComponentName className, IBinder service)
        {
            try
            {
                LocalBinder localBinder = (LocalBinder) service;
                mMSWisepadDeviceController = localBinder.getService();
                mIsMSWisepasConnectionServiceBound = true;

                /**
                 * start the connection to the wise pad asynchronously, and call backs the listeners object
                 * with the status of the connection
                 */

                if (ApplicationData.IS_DEBUGGING_ON)
                    Logs.v(ApplicationData.packName, "Wisepad servcie binded staring the connection... " + mAllowFirmwareUpdatge, true, true);

                if(mMSWisepadDeviceController != null) {
                    if(MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_PLUS ||
                            MSWisepadDeviceController.getDeviceType() == MSWisepadDeviceController.DeviceType.WISEPOS_NEO){

                        mMSWisepadDeviceController.initMswipeWisepadDeviceController(mMSWisepadDeviceObserver,
                                mAutoConnect, false, mCallCheckCardAfterConnection,
                                mAllowFirmwareUpdatge, WisepadCheckCardMode.SWIPE_OR_INSERT_OR_TAP);
                    }else
                    {
                        mMSWisepadDeviceController.initMswipeWisepadDeviceController(mMSWisepadDeviceObserver,
                                mAutoConnect, false, mCallCheckCardAfterConnection,
                                mAllowFirmwareUpdatge, WisepadCheckCardMode.SWIPE_OR_INSERT);

                        mMSWisepadDeviceController.setUSBConnectionMode(AppSharedPrefrences.getAppSharedPrefrencesInstace().isWisepadUSBMode());

                    }
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
            mIsMSWisepasConnectionServiceBound = false;
            mMSWisepadDeviceController = null;

        }
    };


    public void initiateWisepadConnection()
    {

        if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName,  "initiateWisepadConnection", true, true);

        if(!isConnetCalled)

        {
            if (mMSWisepadDeviceController != null)
            {
                if (ApplicationData.IS_DEBUGGING_ON)
                    Logs.v(ApplicationData.packName, "connecttion request", true, true);

                mMSWisepadDeviceController.connect();

            }
            else{
                if (ApplicationData.IS_DEBUGGING_ON)
                    Logs.v(ApplicationData.packName, "device service not initialized" , true, true);
            }
            isConnetCalled = true;
        }
    }

    /*
     * getWisePadTranscationState to get the transaction sate with the wisepad
     *
     */

    public WisePadConnection getWisePadConnectionState()
    {
        if(mMSWisepadDeviceController != null)
            return mMSWisepadDeviceController.getWisepadConnectionState();

        return  WisePadConnection.WisePadConnection_NOT_CONNECTED;
    }

    /*
     * getWisePadTranscationState to get the transaction sate with the wisepad
     *
     */
    public WisePadTransactionState getWisePadTranscationState()
    {
        if(mMSWisepadDeviceController != null)
            return mMSWisepadDeviceController.getWisePadTransactionState();

        return  WisePadTransactionState.WisePadTransactionState_Ready;
    }

    public void setWisepadWaitingForCardResult() {}

    /*
     * setWisePadConnectionState overridden function to display the connection state of the device
     *
     */
    public void setWisepadAmount(){}

    public void setWisePadConnectionStateResult(WisePadConnection wisePadConnection){}

    public void setWisePadStateInfo(String msg){}

    public void setWisePadStateErrorInfo(String msg){}

    public void setWisepadDeviceInfo(Hashtable<String, String> paramHashtable){}

    /* when the details of the card are notified back to the user,  override this function to display the details to the user
     *
     */
    public void processCardSaleOnline(){}

    public void showCardDetails(){}

    public void requestCheckCard(){


        if (mMSWisepadDeviceController != null)
        {
            mIsPinBypassed = false;
            mMSWisepadDeviceController.checkCard(WisepadCheckCardMode.SWIPE_OR_INSERT);
        }

    }

    public void requestCancelOnlineProcess(){

        if (mMSWisepadDeviceController != null)
            mMSWisepadDeviceController.cancelOnlineProcess();

    }

    public void requestDeviceInfo(){

        if (mMSWisepadDeviceController != null)
            mMSWisepadDeviceController.getDeviceInfo();
    }

    public void requestDisconnect(){

        if (mMSWisepadDeviceController != null)
            mMSWisepadDeviceController.disconnect();
    }

    /*Card Sale transaction */

    public void processDeviceConnetionWithAutoInitiation(AutoInitiationDevice aAutoInitiationDevice)
    {

        if (mMSWisepadDeviceController != null &&
                mMSWisepadDeviceController.getWisepadConnectionState() != WisePadConnection.WisePadConnection_CONNECTING)
        {
            if (mMSWisepadDeviceController != null &&
                    !mMSWisepadDeviceController.isDevicePresent())
            {
                String msg = getString(R.string.mswisepadview_wisepad_not_connected_please_make_sure_that_the_wisepad_is_switched_on);

                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage(msg);
                builder1.setCancelable(false);

                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                                if(mMSWisepadDeviceController != null)
                                {
                                    mMSWisepadDeviceController.connect();
                                }
                            }
                        });

                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

                return;
            }
            else {

                if (mMSWisepadDeviceController != null) {

                    if(aAutoInitiationDevice == AutoInitiationDevice.CHECKCARD)
                        mMSWisepadDeviceController.checkCard(WisepadCheckCardMode.SWIPE_OR_INSERT);
                    else
                        mMSWisepadDeviceController.getDeviceInfo();
                }
            }
        }
        else { // if the device is connecting in the back ground


            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage(getResources().getString(R.string.mswisepadview_connecting_to_wisepad_if_its_taking_longer_than_usual_please_restart_the_wisepad_and_try_reconnecting));
            builder1.setCancelable(false);

            builder1.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    public void showAmexPinEntry(){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.title35));
        builder.setCancelable(false);

        // Set up the input
        final EditText input = new EditText(this);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setHint("amex card security pin");
        input.setHintTextColor(Color.LTGRAY);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(input.getText().toString().trim().length() != 4){

                    Toast.makeText(MswipeWisepadView.this, getResources().getString(R.string.invalid_pin_input_should_be_4_digits_in_length), Toast.LENGTH_LONG).show();

                }
                else {

                    dialog.dismiss();
                    mAmexSecurityCode = input.getText().toString().trim();
                    processCardSaleOnline();
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }



    /**
     * MSWisepadDeviceObserver
     * 		The mswipe device controller class  observer which listens to the responses of the wisepad delegated function
     * based on the device notification, appropriate steps  need to be considered or request should be sent back to the
     * wisepad this will ensure a smooth communications back and forth between the wisepad device and the application
     */

    class MSWisepadDeviceObserver implements MSWisepadDeviceControllerResponseListener
    {
        /*the bluetooth connection channel states callback function between the device and application,
         */
        public void onReturnWisepadConnection(WisePadConnection wisePadConntection, BluetoothDevice bluetoothDevice)
        {
            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName, "" + wisePadConntection, true, true);

            if(wisePadConntection == WisePadConnection.WisePadConnection_CONNECTED)
            {
				/*
				Store the last connected device to mswipelastconnecteddevice, so that the sdk will
				pickup this device to connected
				*/

                try
                {
                    mBlueToothMcId = bluetoothDevice.getName();

                } catch (Exception e) {
                    // TODO: handle exception
                }


                setWisePadConnectionStateResult(WisePadConnection.WisePadConnection_CONNECTED);

            }
            else if(wisePadConntection == WisePadConnection.WisePadConnection_CONNECTING){

                setWisePadConnectionStateResult(WisePadConnection.WisePadConnection_CONNECTING);
            }
            else if(wisePadConntection == WisePadConnection.WisePadConnection_DEVICE_DETECTED){

                setWisePadConnectionStateResult(WisePadConnection.WisePadConnection_DEVICE_DETECTED);
            }
            else if(wisePadConntection == WisePadConnection.WisePadConnection_DEVICE_NOTFOUND){

                setWisePadConnectionStateResult(WisePadConnection.WisePadConnection_DEVICE_NOTFOUND);
            }
            else if(wisePadConntection == WisePadConnection.WisePadConnection_NOT_CONNECTED){

                setWisePadConnectionStateResult(WisePadConnection.WisePadConnection_NOT_CONNECTED);
            }
            else if(wisePadConntection == WisePadConnection.WisePadConnection_DIS_CONNECTED){

                setWisePadConnectionStateResult(WisePadConnection.WisePadConnection_DIS_CONNECTED);

            }
            else if(wisePadConntection == WisePadConnection.WisePadConnection_FAIL_TO_START_BT){

                setWisePadConnectionStateResult(WisePadConnection.WisePadConnection_FAIL_TO_START_BT);
            }
            else if(wisePadConntection == WisePadConnection.WisePadConnection_BLUETOOTH_DISABLED){

                setWisePadConnectionStateResult(WisePadConnection.WisePadConnection_BLUETOOTH_DISABLED);
            }
            else if(wisePadConntection == WisePadConnection.WisePadConnection_BLUETOOTH_SWITCHEDOFF){

                setWisePadConnectionStateResult(WisePadConnection.WisePadConnection_BLUETOOTH_SWITCHEDOFF);

                bluetoothReq();

            }
            else if(wisePadConntection == WisePadConnection.WisePadConnection_MULTIPLE_PAIRED_DEVCIES_FOUND){

                setWisePadConnectionStateResult(WisePadConnection.WisePadConnection_MULTIPLE_PAIRED_DEVCIES_FOUND);

                if(mShowMultiDeviceList)
                    showDeviceListDialog();

            }
            else if(wisePadConntection == WisePadConnection.WisePadConnection_NO_PAIRED_DEVICES_FOUND){

                setWisePadConnectionStateResult(WisePadConnection.WisePadConnection_NO_PAIRED_DEVICES_FOUND);
            }
        }

        /*
        when the a request is sent to the the wise pad to check for the card used and hence when it detects the card, the device,
        callback with the information related to the card used or callbacks with information requesting information to be sent back to the wisepad,
        */
        @Override
        public void onRequestWisePadCheckCardProcess(CheckCardProcess checkCardProcess, ArrayList<String> dataList)
        {
            // TODO Auto-generated method stub
            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName, "checkCardProcess: " + checkCardProcess, true, true);

            String msg = "";

            if(checkCardProcess == CheckCardProcess.CheckCardProcess_WAITING_FOR_CARD)
            {
                mIsPinBypassed = false;
                setWisepadWaitingForCardResult();

                if (ApplicationData.IS_DEBUGGING_ON)
                    Logs.v(ApplicationData.packName, "" + mMSWisepadDeviceController.getCheckCardProcess(), true, true);

                if (mMSWisepadDeviceController.getCheckCardProcess() == CheckCardProcess.CheckCardProcess_WAITING_FOR_SWIPE) {
                    setWisePadStateInfo("please swipe card");
                } else if (mMSWisepadDeviceController.getCheckCardProcess() == CheckCardProcess.CheckCardProcess_WAITING_FOR_INSERT) {
                    setWisePadStateInfo("please insert card");
                } else if (mMSWisepadDeviceController.getCheckCardProcess() == CheckCardProcess.CheckCardProcess_WAITING_FOR_TAP) {
                    setWisePadStateInfo("please tap card");
                } else if (mMSWisepadDeviceController.getCheckCardProcess() == CheckCardProcess.CheckCardProcess_WAITING_FOR_SWIPE_OR_INSERT) {
                    setWisePadStateInfo("please swipe or insert card");
                } else if (mMSWisepadDeviceController.getCheckCardProcess() == CheckCardProcess.CheckCardProcess_WAITING_FOR_SWIPE_OR_TAP) {
                    setWisePadStateInfo("please swipe or tap card");
                } else if (mMSWisepadDeviceController.getCheckCardProcess() == CheckCardProcess.CheckCardProcess_WAITING_FOR_INSERT_OR_TAP) {
                    setWisePadStateInfo("please insert or tap card");
                } else if (mMSWisepadDeviceController.getCheckCardProcess() == CheckCardProcess.CheckCardProcess_WAITING_FOR_SWIPE_OR_INSERT_OR_TAP) {
                    setWisePadStateInfo("please swipe or insert or tap card");
                }
            }
            else if(checkCardProcess == CheckCardProcess.CheckCardProcess_SET_AMOUNT){

                setWisepadAmount();
                setWisePadStateInfo(MswipeWisepadView.this.getString(R.string.processing));
            }
            else if(checkCardProcess == CheckCardProcess.CheckCardProcess_PIN_ENTRY_MAG_CARD){

                setWisePadStateInfo(MswipeWisepadView.this.getString(R.string.please_enter_pin_on_wisepad_or_press_enter_green_key_to_bypass_pin));
            }
            else if(checkCardProcess == CheckCardProcess.CheckCardProcess_PIN_ENTRY_ICC_CARD){

                setWisePadStateInfo(MswipeWisepadView.this.getString(R.string.please_enter_pin_on_wisepad_or_press_enter_green_key_to_bypass_pin));
            }
            else if(checkCardProcess == CheckCardProcess.CheckCardProcess_SELECT_APPLICATION){

                setWisePadStateInfo(MswipeWisepadView.this.getString(R.string.please_select_app));

                if (ApplicationData.IS_DEBUGGING_ON)
                    Logs.v(ApplicationData.packName, "onRequestSelectApplication " + dataList.size(), true, true);

                if (dataList.size() == 1) {

                    if(mMSWisepadDeviceController != null)
                        mMSWisepadDeviceController.selectApplication(0);

                }
                else {

                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(MswipeWisepadView.this);
                    builderSingle.setIcon(R.drawable.mswipe_icon);
                    builderSingle.setTitle(getResources().getString(R.string.please_select_app));

                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MswipeWisepadView.this, R.layout.mswipe_simple_list_item);

                    for (int i = 0; i < dataList.size(); ++i) {
                        arrayAdapter.add(dataList.get(i));
                    }

                    builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int position) {

                            if(mMSWisepadDeviceController != null)
                                mMSWisepadDeviceController.selectApplication(position);

                            dialog.dismiss();

                        }
                    });
                    builderSingle.show();

                }
            }
        }

        /*once the card is detected and then when the proper request had been collected from the user the devcie
        callback the details of the card, and this off-line data can be used to post to the gateway for further validation
        */
        @Override
        public void onReturnWisePadOfflineCardTransactionResults(CheckCardProcessResults checkCardResults,
                                                                 Hashtable<String, Object> paramHashtable)
        {
            // TODO Auto-generated method stub
            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName, "" + checkCardResults, true, true);


            if(checkCardResults == CheckCardProcessResults.ON_REQUEST_ONLINEPROCESS)
            {


                mCardData = (CardData)paramHashtable.get("cardData");

                if (ApplicationData.IS_DEBUGGING_ON)
                    Logs.v(ApplicationData.packName, "mCardData.getCardSchemeResults() " + mCardData.getCardSchemeResults(), true, true);

                if (mCardData.getCardSchemeResults() == CARDSCHEMERRESULTS.ICC_CARD)
                {
                    //processTransactionsOnline();

                    showCardDetails();
                }
                else if (mCardData.getCardSchemeResults() == CARDSCHEMERRESULTS.MAG_CARD) {

                    //processTransactionsOnline();
                    showCardDetails();
                }
                else if (mCardData.getCardSchemeResults() == CARDSCHEMERRESULTS.MAG_AMEXCARD) {

                    showAmexPinEntry();

                }
            }
            else if(checkCardResults == CheckCardProcessResults.NO_CARD){

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.no_card_detected));
            }
            else if(checkCardResults == CheckCardProcessResults.NOT_ICC){

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.card_inserted));
            }
            else if(checkCardResults == CheckCardProcessResults.BAD_SWIPE){

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.bad_swipe));
            }
            else if(checkCardResults == CheckCardProcessResults.MAG_HEAD_FAIL){

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.mag_head_fail));
            }
            else if(checkCardResults == CheckCardProcessResults.USE_ICC_CARD){

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.please_use_chip_card));
            }
            else if(checkCardResults == CheckCardProcessResults.PIN_ENTERED) {

                setWisePadStateInfo(MswipeWisepadView.this.getString(R.string.pin_entered));
            }
            else if(checkCardResults == CheckCardProcessResults.PIN_BYPASS) {

                mIsPinBypassed = true;
                setWisePadStateInfo(MswipeWisepadView.this.getString(R.string.bypass));

            }
            else if(checkCardResults == CheckCardProcessResults.PIN_CANCEL) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.pin_canceled));
            }
            else if (checkCardResults == CheckCardProcessResults.PIN_TIMEOUT) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.pin_timeout));
            }
            else if (checkCardResults == CheckCardProcessResults.PIN_UNKNOWN_ERROR) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.key_error));
            }
            else if (checkCardResults == CheckCardProcessResults.PIN_Asterisk) {

                String astreik = (String)paramHashtable.get("key");
                setWisePadStateInfo(astreik);
            }
            else if (checkCardResults == CheckCardProcessResults.PIN_WRONG_PIN_LENGTH) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.pin_wrong_pin_length));
            }
            else if (checkCardResults == CheckCardProcessResults.PIN_INCORRECT_PIN) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.no_pin));
            }
            else if (checkCardResults == CheckCardProcessResults.TRANSACTION_TERMINATED) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.transaction_terminated));
            }
            else if (checkCardResults == CheckCardProcessResults.TRANSACTION_DECLINED) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.transaction_declined));
            }
            else if (checkCardResults == CheckCardProcessResults.TRANSACTION_CANCELED_OR_TIMEOUT) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.transaction_cancel));
            }
            else if (checkCardResults == CheckCardProcessResults.TRANSACTION_CAPK_FAIL) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.transaction_capk_fail));
            }
            else if (checkCardResults == CheckCardProcessResults.TRANSACTION_NOT_ICC) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.transaction_not_icc));
            }
            else if (checkCardResults == CheckCardProcessResults.TRANSACTION_SELECT_APP_FAIL) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.transaction_app_fail));
            }
            else if (checkCardResults == CheckCardProcessResults.TRANSACTION_DEVICE_ERROR) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.transaction_device_error));
            }
            else if(checkCardResults == CheckCardProcessResults.TRANSACTION_APPLICATION_BLOCKED) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.transaction_application_blocked));
            }
            else if(checkCardResults == CheckCardProcessResults.TRANSACTION_ICC_CARD_REMOVED) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.transaction_icc_card_removed));
            }
            else if(checkCardResults == CheckCardProcessResults.TRANSACTION_CARD_BLOCKED) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.transaction_card_blocked));
            }
            else if(checkCardResults == CheckCardProcessResults.TRANSACTION_CARD_NOT_SUPPORTED) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.transaction_card_not_supported));
            }
            else if(checkCardResults == CheckCardProcessResults.TRANSACTION_CONDITION_NOT_SATISFIED) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.transaction_condition_not_satisfied));
            }
            else if(checkCardResults == CheckCardProcessResults.TRANSACTION_INVALID_ICC_DATA) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.transaction_invalid_icc_data));
            }
            else if(checkCardResults == CheckCardProcessResults.TRANSACTION_MISSING_MANDATORY_DATA) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.transaction_missing_mandatory_data));
            }
            else if(checkCardResults == CheckCardProcessResults.TRANSACTION_NO_EMV_APPS) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.transaction_no_emv_apps));
            }
            else if(checkCardResults == CheckCardProcessResults.CANCEL_CHECK_CARD) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.canceled_please_try_again));
            }
            else if(checkCardResults == CheckCardProcessResults.TRANSACTION_FAILED) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.transaction_failed));
            }
            else{
                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.unknown_error));
            }
        }

        /*during the process of detecting the card and collecting the data from the user when the device encounters any issues it callback
        through this delegate to notify the application with the specifics of the error.
        */
        @Override
        public void onError(Error errorState, String errorMsg)
        {

            String msg = "";

            if (errorState == Error.UNKNOWN)
            {
                msg = (MswipeWisepadView.this.getString(R.string.unknown_error));
            }
            else if (errorState == Error.CMD_NOT_AVAILABLE)
            {
                msg = (MswipeWisepadView.this.getString(R.string.command_not_available));
            }
            else if (errorState == Error.TIMEOUT) {
                msg = (MswipeWisepadView.this.getString(R.string.device_no_response));
            }
            else if (errorState == Error.DEVICE_BUSY) {
                msg = (MswipeWisepadView.this.getString(R.string.device_busy));
            }
            else if (errorState == Error.INPUT_OUT_OF_RANGE) {
                msg = (MswipeWisepadView.this.getString(R.string.out_of_range));
            }
            else if (errorState == Error.INPUT_INVALID_FORMAT) {
                msg = (MswipeWisepadView.this.getString(R.string.invalid_format));
            }
            else if (errorState == Error.INPUT_INVALID) {
                msg = (MswipeWisepadView.this.getString(R.string.input_invalid));
            }
            else if (errorState == Error.CASHBACK_NOT_SUPPORTED) {
                msg = (MswipeWisepadView.this.getString(R.string.cashback_not_supported));
            }
            else if (errorState == Error.CRC_ERROR) {
                msg = (MswipeWisepadView.this.getString(R.string.crc_error));
            }
            else if (errorState == Error.COMM_ERROR) {
                msg = (MswipeWisepadView.this.getString(R.string.comm_error));
            }
            else if(errorState == Error.FAIL_TO_START_BT) {
                msg = (MswipeWisepadView.this.getString(R.string.fail_to_start_bluetooth_v2));
            }

            else if(errorState == Error.FAIL_TO_START_AUDIO) {
                msg = (MswipeWisepadView.this.getString(R.string.fail_to_start_audio));
            }
            else if (errorState == Error.COMM_LINK_UNINITIALIZED) {
                msg = (MswipeWisepadView.this.getString(R.string.comm_link_uninitialized));
            }
            else  {
                msg = (MswipeWisepadView.this.getString(R.string.unknown_error));
            }

            setWisePadStateErrorInfo(msg);

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName, "onError  the error state is " + errorState, true, true);

        }

        /*during the process of detecting the card and collecting the data from the user the device callback's
         *through this delegate to notify the application about the information about the wise pad current processing state, this
         *information can be displayed back to the user and this information presented is just a text and the
         * wise pad does not expect any action further actions from the user, this as to be used only for presenting the state of the wisepad to the user.
         */
        @Override
        public void onRequestDisplayWispadStatusInfo(DisplayText displayText)
        {
            // TODO Auto-generated method stub

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName, "the displayText is " + displayText, true, true);

            if (displayText == DisplayText.APPROVED) {

                setWisePadStateInfo(MswipeWisepadView.this.getString(R.string.approved));
            }
            else if (displayText == DisplayText.CALL_YOUR_BANK) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.call_your_bank));
            }
            else if (displayText == DisplayText.DECLINED) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.decline));
            }
            else if (displayText == DisplayText.ENTER_PIN_BYPASS) {


                setWisePadStateInfo(MswipeWisepadView.this.getString(R.string.please_enter_pin_on_wisepad_or_press_enter_green_key_to_bypass_pin));
            }
            else if (displayText == DisplayText.ENTER_PIN) {

                setWisePadStateInfo(MswipeWisepadView.this.getString(R.string.enter_pin));
            }
            else if (displayText == DisplayText.INCORRECT_PIN) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.incorrect_pin));
            }
            else if (displayText == DisplayText.INSERT_CARD) {

                setWisePadStateInfo(MswipeWisepadView.this.getString(R.string.insert_card));
            }
            else if (displayText == DisplayText.NOT_ACCEPTED) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.not_accepted));
            }
            else if (displayText == DisplayText.PIN_OK) {

                setWisePadStateInfo(MswipeWisepadView.this.getString(R.string.pin_ok));
            }
            else if (displayText == DisplayText.PLEASE_WAIT) {

				/*
				As the state is pin result no need show other messages
				as we are waiting for the online process, so we are skipping this message
				for old firmware after pinentry result we are getting this extra message.
				 */

                if(getWisePadTranscationState() !=  WisePadTransactionState.WisePadTransactionState_Pin_Entry_Results)
                    setWisePadStateInfo(MswipeWisepadView.this.getString(R.string.wait));

            }
            else if (displayText == DisplayText.REMOVE_CARD) {

                setWisePadStateInfo(MswipeWisepadView.this.getString(R.string.remove_card));
            }
            else if (displayText == DisplayText.USE_MAG_STRIPE) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.use_mag_stripe));
            }
            else if (displayText == DisplayText.TRY_AGAIN) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.try_again));
            }
            else if (displayText == DisplayText.REFER_TO_YOUR_PAYMENT_DEVICE) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.refer_payment_device));
            }
            else if (displayText == DisplayText.TRANSACTION_TERMINATED) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.transaction_terminated));
            }
            else if (displayText == DisplayText.PROCESSING) {

                setWisePadStateInfo(MswipeWisepadView.this.getString(R.string.processing));
            }
            else if (displayText == DisplayText.LAST_PIN_TRY) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.last_pin_try));
            }
            else if(displayText == DisplayText.SELECT_ACCOUNT) {

                setWisePadStateInfo(MswipeWisepadView.this.getString(R.string.select_account));
            }
            else if(displayText == DisplayText.LOW) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.battery_low));
            }
            else if(displayText == DisplayText.CRITICALLY_LOW) {

                setWisePadStateErrorInfo(MswipeWisepadView.this.getString(R.string.battery_critically_low));
            }
        }

        /*
         * Returns the information about the device, though this callback function initiated through getDeviceInfo
         * */
        @Override
        public void onReturnDeviceInfo(Hashtable<String, String> paramHashtable) {
            // TODO Auto-generated method stub
            setWisepadDeviceInfo(paramHashtable);
        }

        @Override
        public void onReturnFunctionKeyResult(FunctionKeyResult functionKeyResult){

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

        }
    }

    private void showDeviceListDialog(){

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setIcon(R.drawable.mswipe_icon);
        builderSingle.setTitle(R.string.mswisepadview_select_wisepad);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MswipeWisepadView.this, R.layout.mswipe_simple_list_item);

        final ArrayList<BluetoothDevice> mswipeWisepadPairedDevices = mMSWisepadDeviceController.getMswipeWisepadPairedDevices();


        for (int i = 0; i < mswipeWisepadPairedDevices.size(); ++i)
        {
            arrayAdapter.add(mswipeWisepadPairedDevices.get(i).getName());
        }

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {

                if (mMSWisepadDeviceController != null)
                    mMSWisepadDeviceController.connect(mswipeWisepadPairedDevices.get(position));

                dialog.dismiss();

            }
        });
        builderSingle.show();
    }

    public void bluetoothReq()
    {

        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        final int REQUEST_ENABLE_BT = 0;
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0)
        {

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName,  "resultCode  "+resultCode , true, true);

            if (resultCode == 0)
            {

				/*final Dialog dialog = Constants.showDialog(this, mCardSaleDlgTitle,
						"bluetooth should be enabled to perform cardsale", Constants.CUSTOM_DLG_TYPE.CUSTOM_DLG_TYPE_RETURN_DLG_INFO, getString(R.string.ok),"");
				Button yes = (Button) dialog.findViewById(R.id.customdlg_BTN_yes);

				yes.setOnClickListener(new View.OnClickListener()
				{

					public void onClick(View v) {

						bluetoothReq();

						dialog.dismiss();

					}
				});

				dialog.show();*/


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage(getString(R.string.setmessage35));
                alertDialogBuilder.setCancelable(false);

                alertDialogBuilder.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                bluetoothReq();

                                dialog.cancel();

                            }
                        });

                AlertDialog alert11 = alertDialogBuilder.create();

                alert11.show();

            }else
            {
                if(mMSWisepadDeviceController != null)
                {
                    mMSWisepadDeviceController.connect();
                }
            }
        }
    }

}
