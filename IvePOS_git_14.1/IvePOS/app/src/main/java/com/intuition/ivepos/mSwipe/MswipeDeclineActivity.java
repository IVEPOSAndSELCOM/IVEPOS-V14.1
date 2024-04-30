package com.intuition.ivepos.mSwipe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intuition.ivepos.BeveragesMenuFragment_Dine;
import com.intuition.ivepos.BeveragesMenuFragment_Dine_l;
import com.intuition.ivepos.BeveragesMenuFragment_Qsr;
import com.intuition.ivepos.BeveragesMenuFragment_Retail;
import com.intuition.ivepos.MainActivity;
import com.intuition.ivepos.R;
import com.intuition.ivepos.SplashScreenActivity;
import com.intuition.ivepos.Tables_list_Activity;
import com.mswipetech.wisepad.sdk.data.CardSaleResponseData;
import com.mswipetech.wisepad.sdk.device.MSWisepadDeviceControllerResponseListener;

public class MswipeDeclineActivity extends Activity {

    public SQLiteDatabase db = null;
    private final static String log_tab = "CreditSaleDeclineView=>";
    private String title = "";

    private boolean isEmvSwiper = false;

    String mStrCardNum = "";
    String mStrExpDate = "";
    String mStrAmt = "";
    String mDeclineErrorMsg = "";

    ApplicationData applicationData = ApplicationData.getApplicationDataSharedInstance();

    /**
     * Stores the card sale response data, which received from the sdk
     */
    CardSaleResponseData cardSaleResponseData = null;

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

        setContentView(R.layout.mswipe_creditsale_declineview);

        db = openOrCreateDatabase("mydb_Appdata", Context.MODE_PRIVATE|Context.MODE_NO_LOCALIZED_COLLATORS,null);

        SharedPreferences sharedpreferences_select =  SplashScreenActivity.getDefaultSharedPreferencesMultiProcess(MswipeDeclineActivity.this);
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

        //mDrawerRequired = false;

        if(title == null)
            title = "Card sale";

        title = getIntent().getStringExtra("Title");

        if (ApplicationData.IS_DEBUGGING_ON)
            Logs.v(ApplicationData.packName, "title "+title, true, true);


        cardSaleResponseData = (CardSaleResponseData) getIntent().getSerializableExtra("cardSaleResponseData");

        if (cardSaleResponseData == null){

            mDeclineErrorMsg = getIntent().getStringExtra("statusMessage");

        }else {

            try {


                String errorno = "";
                try{
                    errorno = ((cardSaleResponseData.getFO39Tag().length() > 0)? cardSaleResponseData.getFO39Tag(): cardSaleResponseData.getErrorNo() + "");
                }catch(Exception ex){
                    errorno = cardSaleResponseData.getErrorNo() + "";
                }

                mDeclineErrorMsg = cardSaleResponseData.getResponseFailureReason() + " (" + cardSaleResponseData.getErrorCode() + "-" + errorno + ")";

                if(cardSaleResponseData.getCardSchemeResults() == MSWisepadDeviceControllerResponseListener.CARDSCHEMERRESULTS.ICC_CARD)
                    isEmvSwiper = true;

            } catch (Exception e)
            {
                // TODO: handle exception
            }

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName, "mLast4Digits "+cardSaleResponseData.getLast4Digits(), true, true);

            if (ApplicationData.IS_DEBUGGING_ON)
                Logs.v(ApplicationData.packName, "mAmt "+cardSaleResponseData.getTrxAmount(), true, true);



            Resources resources = this.getResources();

            String ExpiryDate = cardSaleResponseData.getExpiryDate();
            String mLast4Digits = cardSaleResponseData.getLast4Digits();
            String mAmt = cardSaleResponseData.getTrxAmount();

            if(isEmvSwiper) {

                mStrCardNum = "card num: XXXX XXXX XXXX " + mLast4Digits;
                mStrExpDate = "exp date: " + "xx/xx";
                mStrAmt = "amt: " + applicationData.mCurrency + " " + mAmt;

            }else{

                mStrCardNum = "card num: XXXX XXXX XXXX " + mLast4Digits;
                mStrExpDate = "exp date: " +"xx/xx";
                mStrAmt = "amt: " + applicationData.mCurrency + " " + mAmt  ;
            }

        }

        initViews();

    }

    public void initViews()
    {

        ((RelativeLayout)findViewById(R.id.top_bar_REL_content)).setBackgroundColor(getResources().getColor(R.color.red));

        ((LinearLayout) findViewById(R.id.topbar_LNR_topbar_menu)).setVisibility(View.GONE);
        ((LinearLayout) findViewById(R.id.topbar_LNR_topbar_cancel)).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.topbar_LBL_heading)).setText(title);

        TextView errorMessage = (TextView)findViewById(R.id.creditsale_decline_LBL_declinederror);
        TextView cardData = (TextView)findViewById(R.id.creditsale_decline_TXT_redceiptdetails);

        //setting tht receipt details to textview
        cardData.setText(""+mStrCardNum+"\n"+mStrExpDate+" "+mStrAmt);

        errorMessage.setText(mDeclineErrorMsg);

        ImageButton btnSubmit = (ImageButton) findViewById(R.id.creditsale_decline_BTN_submitsignature);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                doneWithCreditSale();
            }
        });

    }


    public void doneWithCreditSale()
    {
        finish();
//        Intent intent = new Intent(MswipeDeclineActivity.this , BeveragesMenuFragment_Dine.class);
//        startActivity(intent);
        if (account_selection.toString().equals(getString(R.string.dine_nospace))) {
            Cursor cursor3 = db.rawQuery("SELECT * FROM Table_kot", null);
            if (cursor3.moveToFirst()) {
                String lite_pro = cursor3.getString(1);

                TextView tv = new TextView(MswipeDeclineActivity.this);
                tv.setText(lite_pro);

                if (tv.getText().toString().equals("Lite")) {
                    Intent intent = new Intent(MswipeDeclineActivity.this , BeveragesMenuFragment_Dine_l.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(MswipeDeclineActivity.this , BeveragesMenuFragment_Dine.class);
                    startActivity(intent);
                }
            }else {
                Intent intent = new Intent(MswipeDeclineActivity.this , BeveragesMenuFragment_Dine_l.class);
                startActivity(intent);
            }
//            Intent intent = new Intent(MswipeDeclineActivity.this , BeveragesMenuFragment_Dine.class);
//            startActivity(intent);
        }else {
            if (account_selection.toString().equals(getString(R.string.qsr_nospace))) {
                Intent intent = new Intent(MswipeDeclineActivity.this , BeveragesMenuFragment_Qsr.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(MswipeDeclineActivity.this , BeveragesMenuFragment_Retail.class);
                startActivity(intent);
            }
        }
    }
}
