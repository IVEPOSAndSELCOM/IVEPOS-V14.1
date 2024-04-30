package com.intuition.ivepos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.eze.api.EzeAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Ezetap_Payment extends AppCompatActivity {


    private final int REQUEST_CODE_SALE_TXN = 10007;


    JSONObject jsonRequest = new JSONObject();
    JSONObject jsonOptionalParams = new JSONObject();
    JSONObject jsonReferences = new JSONObject();
    JSONObject jsonCustomer = new JSONObject();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle extras = getIntent().getExtras();
        String billnumber = extras.getString("billnumber");
        String total = extras.getString("total");

        //Building Customer Object
        try {
            jsonCustomer.put("name", "kumar");
            jsonCustomer.put("mobileNo", "8338061416");
            jsonCustomer.put("email", "risi.kmr@gmail.com");


    //Building References Object
            jsonReferences.put("reference1", billnumber);
            jsonReferences.put("reference2", "Reference 2");
            jsonReferences.put("reference3", "Reference 3");

    //Passing Additional References
            JSONArray array = new JSONArray();
            array.put("addRef_xx1");
            array.put("addRef_xx2");
            jsonReferences.put("additionalReferences",array);

    //Building Optional params Object

            //Cannot have amount cashback in SALE transaction.
            jsonOptionalParams.put("amountCashback",0.00);
            jsonOptionalParams.put("amountTip",0.00);
            jsonOptionalParams.put("references",jsonReferences);
            jsonOptionalParams.put("customer",jsonCustomer);


            // jsonRequest.put("merchantName","Start Fresh");
            jsonRequest.put("orgCode","START_FRESH_232");
            jsonRequest.put("merchantName","Start Fresh");
            jsonRequest.put("merchantEmail", "rts@hj.com");

            //Passing 3PL Label to options object
            jsonOptionalParams.put("payToAccount",null);

            //Building final request object
            jsonRequest.put("amount", total);

            //This attributes determines the type of transaction
            jsonRequest.put("mode", "SALE");
            jsonRequest.put("options", jsonOptionalParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        EzeAPI.cardTransaction(this, REQUEST_CODE_SALE_TXN, jsonRequest);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE_SALE_TXN) {
            try {
                if (intent != null && intent.hasExtra("response")) {
                    if (resultCode == RESULT_OK) {
                        JSONObject response = new JSONObject(intent.getStringExtra("response"));
                        response = response.getJSONObject("result");
//                        Toast.makeText(this, "Sucessful 1 " + response, Toast.LENGTH_LONG).show();
                        System.out.println("Sucessful 1 " + response);

//                        Intent intent1=new Intent();
//                        intent1.putExtra("MESSAGE","SUCCESS");
//                        setResult(8,intent1);
//                        finish();

                        gotohome();



                        // Initialization of SDK is successful, proceed with your action
                    } else if (resultCode == RESULT_CANCELED) {
                        JSONObject response = new JSONObject(intent.getStringExtra("response"));
                        response = response.getJSONObject("error");
                        String errorCode = response.getString("code");
                        String errorMessage = response.getString("message");
                        Toast.makeText(this, "Failed" + response + errorMessage + errorCode, Toast.LENGTH_LONG).show();
                        // Show the error to user as a pop-up informing the details

                        gotohome_failed();

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Do your exception handling
            }
        }

    }

    public void gotohome() {
        Intent intent = new Intent();
        intent.putExtra("status", true);
        setResult(8, intent);
        finish();
    }

    public void gotohome_failed() {
        Intent intent = new Intent();
        intent.putExtra("status", true);
        setResult(9, intent);
        finish();
    }
}
