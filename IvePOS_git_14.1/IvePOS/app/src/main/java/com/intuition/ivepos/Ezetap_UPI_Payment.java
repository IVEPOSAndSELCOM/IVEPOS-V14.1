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

public class Ezetap_UPI_Payment extends AppCompatActivity {


    private final int REQUEST_CODE_UPI = 10014;


    JSONObject jsonRequest = new JSONObject();
    JSONObject jsonOptionalParams = new JSONObject();
    JSONObject jsonReferences = new JSONObject();
    JSONObject jsonCustomer = new JSONObject();
    JSONObject jsonUpi = new JSONObject();


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
            jsonOptionalParams.put("references",jsonReferences);
            jsonOptionalParams.put("customer",jsonCustomer);

            //Building UPI Object
            jsonUpi.put("payerVPA", "xyz@upi");

            //Building final request object
            jsonRequest.put("amount", total);
            jsonRequest.put("upi", jsonUpi);

            jsonRequest.put("options", jsonOptionalParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        System.out.println("ezetap request "+jsonRequest);

        EzeAPI.upiTransaction(this, REQUEST_CODE_UPI, jsonRequest);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE_UPI) {
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
        setResult(10, intent);
        finish();
    }

    public void gotohome_failed() {
        Intent intent = new Intent();
        intent.putExtra("status", true);
        setResult(11, intent);
        finish();
    }
}
