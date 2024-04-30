package com.intuition.ivepos.mSwipe;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.intuition.ivepos.R;
import com.mswipetech.sdk.network.MSGatewayConnectionListener;
import com.mswipetech.wisepad.sdk.MSWisepadController;
import com.mswipetech.wisepad.sdk.data.ChangePasswordResponseData;
import com.mswipetech.wisepad.sdk.data.MSDataStore;
import com.mswipetech.wisepad.sdk.listeners.MSWisepadControllerResponseListener;

/**
 * ChangePasswordView 
 * ChangePasswordView enables the merchant to change his password
 */
public class ChangePasswordView extends BaseTitleActivity
{
    
    //The mswipe controller observes all the responses from the mswipe gateway
    private MSWisepadControllerResponseListenerObserver mMSWisepadControllerResponseListenerObserver = null;
    
    //The progress of the application activity  
    private CustomProgressDialog mProgressActivity = null;
    
    //fields for changing the merchant password n
    EditText mTxtPassword = null;
    EditText mTxtNewPassword = null;
    EditText mTxtReTypeNewPassword = null;

    // this time will handle the duplicate transactions, by ignoring the click action for 5 seconds time interval
    private long lastRequestTime = 0;
   
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_changepassword);
        
        mMSWisepadControllerResponseListenerObserver = new MSWisepadControllerResponseListenerObserver();

        initViews();
    }


    private void initViews()
    {
        ((TextView) findViewById(R.id.topbar_LBL_heading)).setText(getString(R.string.title33));
        

        mTxtPassword = (EditText) findViewById(R.id.changepassword_TXT_password);
        mTxtNewPassword = (EditText) findViewById(R.id.changepassword_TXT_newpassword);
        mTxtReTypeNewPassword = (EditText) findViewById(R.id.changepassword_TXT_retypepassword);

       
        Button submit = (Button) findViewById(R.id.changepassword_BTN_submit);
        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.hideSoftInputFromWindow(tabHost.getApplicationWindowToken(), 0);
                imm.hideSoftInputFromWindow(ChangePasswordView.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                if (mTxtPassword.getText().toString().trim().length() == 0) {
                    Constants.showDialog(ChangePasswordView.this, Constants.PWD_DIALOG_MSG, Constants.PWD_ERROR_INVALIDPWD);


                    mTxtPassword.requestFocus();
                    return;
                } 
                else if (mTxtPassword.getText().length() < 6) {
                    Constants.showDialog(ChangePasswordView.this, Constants.PWD_DIALOG_MSG, Constants.PWD_ERROR_INVALIDPWDLENGTH);

                    mTxtPassword.requestFocus();
                    return;
                } 
                else if (mTxtPassword.getText().length() > 10) {
                    Constants.showDialog(ChangePasswordView.this, Constants.PWD_DIALOG_MSG, Constants.PWD_ERROR_INVALIDPWDMAXLENGTH);

                    mTxtPassword.requestFocus();
                    return;
                } 
                else if (mTxtNewPassword.getText().toString().trim().length() < 6) {
                    Constants.showDialog(ChangePasswordView.this, Constants.PWD_DIALOG_MSG, Constants.PWD_ERROR_INVALIDPWDNEWLENGTH);
                    mTxtNewPassword.requestFocus();
                    return;
                } 
                else if (mTxtNewPassword.getText().toString().trim().length() > 10) {
                    Constants.showDialog(ChangePasswordView.this, Constants.PWD_DIALOG_MSG, Constants.PWD_ERROR_INVALIDPWDMAXNEWLENGTH);
                    mTxtNewPassword.requestFocus();
                    return;

                } 
                else if (mTxtReTypeNewPassword.getText().toString().trim().length() < 6) {
                    Constants.showDialog(ChangePasswordView.this, Constants.PWD_DIALOG_MSG, Constants.PWD_ERROR_INVALIDPWDRETYPELENGTH);
                    mTxtReTypeNewPassword.requestFocus();
                    return;
                } 
                else if (!mTxtReTypeNewPassword.getText().toString().trim().equals(mTxtNewPassword.getText().toString().trim())) {
                    Constants.showDialog(ChangePasswordView.this, Constants.PWD_DIALOG_MSG, Constants.PWD_ERROR_INVALIDNEWANDRETYPE);
                    mTxtReTypeNewPassword.requestFocus();
                    return;
                }
                

                final Dialog dialog = Constants.showDialog(ChangePasswordView.this, Constants.PWD_DIALOG_MSG, Constants.PWD_CHANGEPWD_CONFIRMATION,
                		Constants.CUSTOM_DLG_TYPE.CUSTOM_DLG_TYPE_RETURN_DLG_CONFIRMATION);
                Button yes = (Button) dialog.findViewById(R.id.customdlg_BTN_yes);
                 yes.setOnClickListener(new OnClickListener()
                 {

                    public void onClick(View v)
                    {
                        dialog.dismiss();


                        try
                        {

                            final MSWisepadController wisepadController = MSWisepadController.
                                    getSharedMSWisepadController(ChangePasswordView.this ,
                                            AppSharedPrefrences.getAppSharedPrefrencesInstace().getGatewayEnvironment(),
                                            AppSharedPrefrences.getAppSharedPrefrencesInstace().getNetworkSource()
                                            ,null);

                            MSWisepadController.setNetworkSource(AppSharedPrefrences.getAppSharedPrefrencesInstace().getNetworkSource());


                            if (System.currentTimeMillis() - lastRequestTime >= ApplicationData.REQUEST_THRESHOLD_TIME) {

                                wisepadController.changePassword(
                                        AppSharedPrefrences.getAppSharedPrefrencesInstace().getReferenceId(),
                                        AppSharedPrefrences.getAppSharedPrefrencesInstace().getSessionToken(),
                                        mTxtPassword.getText().toString(),
                                        mTxtNewPassword.getText().toString(),
                                        new MSWisepadControllerResponseListenerObserver());

                                mProgressActivity = new CustomProgressDialog(ChangePasswordView.this, "Processing password...");
                                mProgressActivity.show();
                            }

                            lastRequestTime = System.currentTimeMillis();





                        } catch (Exception e) {
                            e.printStackTrace();
                        }


   
                    }
                });

                Button no = (Button) dialog.findViewById(R.id.customdlg_BTN_no);
                no.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
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
         * 		the generic mswipe data store, this instance refers to ChangePasswordResponseData, so this 
         * need be type cast back to ChangePasswordResponseData to access response details for the requested password change
         * @return 
         */
    	public void onReponseData(MSDataStore aMSDataStore)
    	{
    		if(mProgressActivity != null)
    		{
    			mProgressActivity.dismiss();
    			mProgressActivity = null;
    		}
    		
    		ChangePasswordResponseData changePasswordResponseData = (ChangePasswordResponseData) aMSDataStore;
    		boolean responseStatus = changePasswordResponseData.getResponseStatus();
    		
    		if (!responseStatus) 
    		{
                
                Constants.showDialog(ChangePasswordView.this, Constants.PWD_DIALOG_MSG, changePasswordResponseData.getResponseFailureReason());

            } 
    		else if (responseStatus){
    			
    			//and if the password changes the session token which is like a OAUTH authentication to the
    			//rest of the SDK api's
    			
    			//so the new session should be saved to be used again when accessing the application again
    			String Session_Tokeniser =  changePasswordResponseData.getSessionTokeniser();
    			
                AppSharedPrefrences.getAppSharedPrefrencesInstace().setSessionToken(Session_Tokeniser);
                
    			 final Dialog dlg = Constants.showDialog(ChangePasswordView.this, Constants.PWD_DIALOG_MSG, changePasswordResponseData.getResponseSuccessMessage(),
                 		Constants.CUSTOM_DLG_TYPE.CUSTOM_DLG_TYPE_RETURN_DLG_INFO);
                 Button btnOk = (Button) dlg.findViewById(R.id.customdlg_BTN_yes);
                 btnOk.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							dlg.dismiss();
							
							setResult(RESULT_OK);
							finish();
							
						}
					});
                 dlg.show();
    			
    		}
            else{
                
            	Constants.showDialog(ChangePasswordView.this, Constants.PWD_DIALOG_MSG, "Invalid response from Mswipe server, please contact support.");
            }
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

    public void doneWithChangePassword() {
        voidTrxViewDestory();
        finish();
    }

    public void voidTrxViewDestory() {
    }
}
