package com.intuition.ivepos.mSwipe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.intuition.ivepos.MultiFragPreferenceActivity;
import com.intuition.ivepos.R;
import com.intuition.ivepos.paytm.Card_Wallets_Settings1;
import com.intuition.ivepos.syncapp.StubProviderApp;
import com.mswipetech.sdk.network.MSGatewayConnectionListener;
import com.mswipetech.wisepad.sdk.MSWisepadController;
import com.mswipetech.wisepad.sdk.data.LoginResponseData;
import com.mswipetech.wisepad.sdk.data.MSDataStore;
import com.mswipetech.wisepad.sdk.listeners.MSWisepadControllerResponseListener;

import java.util.Timer;
import java.util.TimerTask;

/**
 * LoginView
 * Login activity will unable the merchant to login into the mswipe system
 */

public class LoginView extends BaseTitleActivity
{
    //The mobile no of the merchant identified as a unique id
    private TextView mTxtMerchantId = null;
    private TextView mTxtMerchantPassword = null;
    private TextView mTxtTimeOut = null;
    Intent intent = null;
    public SQLiteDatabase db = null;
    public static final String DATABASE_NAME = "mydb_Appdata";
    public static final String TABLE_NAME = "PaynearActivation";
    LinearLayout clear;
    ImageView back;
    TextView btnLogin;
    ProgressDialog progress;
    //    public SQLiteDatabase db = null;
    //The progress of the application activity
    private CustomProgressDialog mProgressActivity = null;


    //The network connection listener observes all the network notifications to the mswipe gateways
    private MSGatewayConncetionObserver mMSGatewayConncetionObserver = null;

    //The mswipe controller observes all the responses from the mswipe gateway
    private MSWisepadControllerResponseListenerObserver mMSWisepadControllerResponseListenerObserver = null;

    //The mswipe controller instance used for calling up the api's
    private MSWisepadController mMSWisepadController = null;

    /*images to show the bluetooth and  websocket connection status i.e wheteher topbar_img_host_active or not*/
    public ImageView imgHostConnectionStatus;

    /*using these animation for connection status*/
    public Animation alphaAnim;

    // this time will handle the duplicate transactions, by ignoring the click action for 5 seconds time interval
    private long lastRequestTime = 0;

    Uri contentUri,resultUri;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_login);

        final EditText user = (EditText)findViewById(R.id.login_TXT_merchantid);
        final EditText pwd = (EditText)findViewById(R.id.login_TXT_merchantpassword);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(user.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(pwd.getWindowToken(), 0);

        db = openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        btnLogin = (TextView) findViewById(R.id.login_BTN_signin);

        mMSGatewayConncetionObserver = new MSGatewayConncetionObserver();
        mMSWisepadControllerResponseListenerObserver = new MSWisepadControllerResponseListenerObserver();

        mMSWisepadController = MSWisepadController.getSharedMSWisepadController(getApplicationContext(),
                        AppSharedPrefrences.getAppSharedPrefrencesInstace().getGatewayEnvironment(),
                        AppSharedPrefrences.getAppSharedPrefrencesInstace().getNetworkSource(),
                        mMSGatewayConncetionObserver);
        MSWisepadController.setNetworkSource(AppSharedPrefrences.getAppSharedPrefrencesInstace().getNetworkSource());



        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginView.this, Card_Wallets_Settings1.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                return;
            }
        });

        clear = (LinearLayout) findViewById(R.id.clear);
        Cursor c1 = db.rawQuery("SELECT * FROM CardSwiperActivation WHERE Config_status = 'Activated'", null);
        if (c1.moveToFirst()) {
            do {
                String activated = c1.getString(1);
                String userid = c1.getString(2);
                String pwdd = c1.getString(3);
//                Toast.makeText(LoginView.this, "1 "+activated+" "+userid+" "+pwdd, Toast.LENGTH_LONG).show();
                if (activated.equals("PaySwiff")) {

                } else {
                    if (activated.equals("mSwipe")) {
                        user.setText(userid);
                        pwd.setText(pwdd);
                        clear.setVisibility(View.VISIBLE);
                        btnLogin.setVisibility(View.GONE);
                    }

//                        Cursor c2 = db.rawQuery("SELECT * FROM CardSwiperActivation WHERE Config_status = 'Activated' ", null);
//                        if (c2.moveToFirst()){
//                            do {
//                                if (activated.equals("mSwipe")) {
//
//
//                                }
//                            } while (c2.moveToNext());
//
//                        }
                }
            }while (c1.moveToNext());
        }
        c1.close();

        initViews();
    }

    /**
     * Initializes the UI elements for the login screen
     * @param
     *
     * @return
     *
     */
    private void initViews()
    {


        imgHostConnectionStatus = (ImageView) findViewById(R.id.topbar_IMG_position2);
        imgHostConnectionStatus.setVisibility(View.VISIBLE);

        alphaAnim = AnimationUtils.loadAnimation(this, R.anim.alpha_anim);

        mTxtMerchantId = (TextView) findViewById(R.id.login_TXT_merchantid);
        mTxtMerchantPassword = (TextView) findViewById(R.id.login_TXT_merchantpassword);
        mTxtTimeOut = (TextView) findViewById(R.id.login_TXT_timeout);


        ((TextView) findViewById(R.id.topbar_LBL_heading)).setText("Login");


        btnLogin.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(LoginView.this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                validateUserCredentials(mTxtMerchantId.getText().toString(),
                        mTxtMerchantPassword.getText().toString(), mTxtTimeOut.getText().toString().trim());

            }
        });



        clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginView.this);
                builder.setTitle(getString(R.string.title34));
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
//                                progress = new ProgressDialog(LoginView.this);
//                                progress.setTitle("Please Wait!!");
//                                progress.setMessage(getString(R.string.setmessage43));
//                                progress.setCancelable(true);
//                                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                                progress.show();
//
//                                Timer timer = new Timer();
//                                timer.schedule(new TimerTask() {
//                                    @Override
//                                    public void run() {
//                                        progress.dismiss();
//
//                                    }
//                                }, 1000);
                                // Delete Table data...
                                ContentValues contentValues = new ContentValues();
//                                contentValues.put("CardSwiperName", "mSwipe");
                                contentValues.put("merchantKey", "");
                                contentValues.put("partnerkey", "");
                                contentValues.put("Config_status", "Not Activated");
                                db.update("CardSwiperActivation", contentValues, "CardSwiperName = 'mSwipe'", null);
                                Toast.makeText(LoginView.this, "Data Deleted", Toast.LENGTH_SHORT).show();
                                mTxtMerchantId.setText("");
                                mTxtMerchantPassword.setText("");

                                clear.setVisibility(View.GONE);
                                btnLogin.setVisibility(View.VISIBLE);

                            }
                        }
                )
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                }
                        );
                builder.show();


            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();

        mMSWisepadController.stopMSGatewayConnection();
    }

    /**
     * Validates the login credential of the merchant
     * @param
     * merchantId
     * 		merchant mobile no
     * merchantPassword
     * 		merchant password
     * @return
     */
    public void validateUserCredentials(final String merchantId, final String merchantPassword, final String timeOut)
    {

        if (System.currentTimeMillis() - lastRequestTime >= 3000) {

            if ((merchantId.equals(""))) {
                Constants.showDialog(LoginView.this, Constants.LOGIN_DIALOG_MSG, Constants.LOGIN_ERROR_ValidUserMsg);

            }
            else if (merchantPassword.equals("")) {
                Constants.showDialog(LoginView.this, Constants.LOGIN_DIALOG_MSG, Constants.LOGIN_ERROR_ValidUserMsg);


            }
            else if (merchantPassword.length() < 6) {
                Constants.showDialog(LoginView.this, Constants.LOGIN_DIALOG_MSG, Constants.PWD_ERROR_INVALIDPWDLENGTH);

                mTxtMerchantPassword.requestFocus();
            }
            else if (merchantPassword.length() > 10) {
                Constants.showDialog(LoginView.this, Constants.LOGIN_DIALOG_MSG, Constants.PWD_ERROR_INVALIDPWDMAXLENGTH);

            }
            else {

                //call the mswipe wisepad api concurrently which run in a different process separated from the UI process
                new Thread() {
                    @Override
                    public void run() {
                        try
                        {
                            if(timeOut.length() > 0){

                                int timOut = Integer.parseInt(timeOut);
                                MSWisepadController.setRequestTimeOut(timOut);
                            }

                            mMSWisepadController.authenticateMerchant( merchantId, merchantPassword,
                                    mMSWisepadControllerResponseListenerObserver);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();


                //display a progress of the activity to  user display

                mProgressActivity = new CustomProgressDialog(LoginView.this, "Logging in...");
                mProgressActivity.show();

            }

        }
        lastRequestTime = System.currentTimeMillis();

    }


    /**
     * MSWisepadControllerResponseListenerObserver
     * The mswipe overridden class  observer which listens to the responses from the mswipe sdk function
     */
    class MSWisepadControllerResponseListenerObserver implements MSWisepadControllerResponseListener
    {

        /**
         * onReponseData
         * The response data notified back to the call back function
         * @param
         * aMSDataStore
         * 		the generic mswipe data store, this instance refers to LoginResponseData, so this
         * need be type cast back to LoginResponseData to access the login response data
         * @return
         */
        public void onReponseData(MSDataStore aMSDataStore)
        {
            if(mProgressActivity != null)
            {
                mProgressActivity.dismiss();
                mProgressActivity = null;
            }

            LoginResponseData loginResponseData = (LoginResponseData) aMSDataStore;

            boolean responseStatus = loginResponseData.getResponseStatus();

            if (!responseStatus)
            {

                Constants.showDialog(LoginView.this, "LoginView", loginResponseData.getResponseFailureReason());

            }
            else if (responseStatus){

                String FirstName =  loginResponseData.getFirstName();

                //below details be used to call up the rest of the api, so by saving them to the device stores like,
                //shared preferences or sqllite, if this details are saved the login to the mswipe gateway is not
                //required again,


                //this Session_Tokeniser does not change until the password for the user id is not changed or if the same user has
                //logged in again.
                String Reference_Id =  loginResponseData.getReferenceId();
                String Session_Tokeniser =  loginResponseData.getSessionTokeniser();

                //save the referenceId and Session_Tokeniser for accessing the other api's, this are a kind of outh
                //authenticated permission for accessing the other services
                AppSharedPrefrences.getAppSharedPrefrencesInstace().setReferenceId(Reference_Id);
                AppSharedPrefrences.getAppSharedPrefrencesInstace().setSessionToken(Session_Tokeniser);
                AppSharedPrefrences.getAppSharedPrefrencesInstace().setUserId(mTxtMerchantId.getText().toString());
                AppSharedPrefrences.getAppSharedPrefrencesInstace().setMID(loginResponseData.getMID());

                String Currency_Code =  loginResponseData.getCurrency();
                boolean tipRequired =  loginResponseData.isTipsRequired();
                float convienencePercentage = loginResponseData.getConveniencePercentage();
                float serviceTax =  loginResponseData.getServiceTax();

                AppSharedPrefrences.getAppSharedPrefrencesInstace().setCurrencyCode(Currency_Code+".");
                AppSharedPrefrences.getAppSharedPrefrencesInstace().setTipEnabled(tipRequired);

                AppSharedPrefrences.getAppSharedPrefrencesInstace().setConveniencePercentage(convienencePercentage);
                AppSharedPrefrences.getAppSharedPrefrencesInstace().setServicePercentageOnConvenience(serviceTax);

                AppSharedPrefrences.getAppSharedPrefrencesInstace().setPinBypass(loginResponseData.isPinBypass());
                AppSharedPrefrences.getAppSharedPrefrencesInstace().setReceiptEnabled(loginResponseData.isReceiptRequired());

                boolean IS_Password_Changed = loginResponseData.isFirstTimePasswordChanged();

                //if this is false, then its suggest that the password has not changed since the user has 
                //been created at-least once
                if (!IS_Password_Changed)
                {
                    startActivity(new Intent(LoginView.this, ChangePasswordView.class));
                    finish();
                    return;

                }
                else{

                    final Dialog dlg = Constants.showDialog(LoginView.this, "LoginView", FirstName + " User authenticated.",
                            Constants.CUSTOM_DLG_TYPE.CUSTOM_DLG_TYPE_RETURN_DLG_INFO);
                    Button btnOk = (Button) dlg.findViewById(R.id.customdlg_BTN_yes);
                    btnOk.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            dlg.dismiss();
                            Intent i = new Intent(LoginView.this, MultiFragPreferenceActivity.class);
                            startActivity(i);

//                            progress = new ProgressDialog(LoginView.this);
//                            progress.setTitle("Please Wait!!");
//                            progress.setMessage("Registering...");
//                            progress.setCancelable(true);
//                            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                            progress.show();
//                            Timer timer = new Timer();
//                            timer.schedule(new TimerTask() {
//                                @Override
//                                public void run() {
//                                    progress.dismiss();
//
//                                }
//                            }, 1000);

                            Cursor payswiff = db.rawQuery("SELECT * FROM CardSwiperActivation WHERE CardSwiperName = 'mSwipe'", null);
                            if (payswiff.moveToFirst()) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("CardSwiperName", "mSwipe");
                                contentValues.put("merchantKey", mTxtMerchantId.getText().toString());
                                contentValues.put("partnerkey", mTxtMerchantPassword.getText().toString());
                                contentValues.put("Config_status", "Activated");


                                contentUri = Uri.withAppendedPath(StubProviderApp.CONTENT_URI, "CardSwiperActivation");
                                getContentResolver().update(contentUri, contentValues, "_id=" + "2",null);
                                resultUri = new Uri.Builder()
                                        .scheme("content")
                                        .authority(StubProviderApp.AUTHORITY)
                                        .path("CardSwiperActivation")
                                        .appendQueryParameter("operation", "update")
                                        .appendQueryParameter("_id","2")
                                        .build();
                                getContentResolver().notifyChange(resultUri, null);






                         //       db.update("CardSwiperActivation", contentValues, "_id=" + "2", null);
//                            db.insert("CardSwiperActivation", null, contentValues);
                                Toast.makeText(LoginView.this, "Data Saved", Toast.LENGTH_SHORT).show();
                            }
                            startActivity(new Intent(LoginView.this, Card_Wallets_Settings1.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                            return;

                        }
                    });
                    dlg.show();

                }
            }
            else{

                Constants.showDialog(LoginView.this, "LoginView", "Invalid response from Mswipe server, please contact support.");
            }
        }
    }



    class MSGatewayConncetionObserver implements MSGatewayConnectionListener {

        @Override
        public void Connected(String msg) {

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName, " msg " + msg, true, true);

            imgHostConnectionStatus.setAnimation(null);
            imgHostConnectionStatus.setImageResource(R.drawable.topbar_img_host_active);
        }

        @Override
        public void Connecting(String msg) {


			/*if(ApplicationData.IS_PERFORMENCE_TEST_ON){
				Logs.p(ApplicationData.packName, "Wedrocket connection started", true, true);
			}*/

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName, " msg " + msg, true, true);

            imgHostConnectionStatus.startAnimation(alphaAnim);
            imgHostConnectionStatus.setImageResource(R.drawable.topbar_img_host_inactive);
        }

        @Override
        public void disConnect(String msg) {


			/*if(ApplicationData.IS_PERFORMENCE_TEST_ON){
				Logs.p(ApplicationData.packName, "Wedrocket disconnected", true, true);
			}*/

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName, " msg " + msg, true, true);

            imgHostConnectionStatus.setAnimation(null);
            imgHostConnectionStatus.setImageResource(R.drawable.topbar_img_host_inactive);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Card_Wallets_Settings1.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        return;
    }

    /*  *//**
 * MSGatewayConncetionObserver
 * The mswipe overridden class  observer which observers the mswipe gateway network connections states

 *//*
    class MSGatewayConncetionObserver implements MSGatewayConnectionListener {

		@Override
		public void Connected(String msg) {
			
			((TextView)findViewById(R.id.topbar_LBL_status)).setText(msg);
			
		}

		@Override
		public void Connecting(String msg) {
			
			((TextView)findViewById(R.id.topbar_LBL_status)).setText(msg);
			
		}

		@Override
		public void disConnect(String msg) {
			
			((TextView)findViewById(R.id.topbar_LBL_status)).setText(msg);
			
		}
    }*/
}