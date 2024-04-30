package com.intuition.ivepos;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.AuthenticationResult;
import com.microsoft.identity.client.MsalClientException;
import com.microsoft.identity.client.MsalException;
import com.microsoft.identity.client.MsalServiceException;
import com.microsoft.identity.client.MsalUiRequiredException;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.User;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 6/1/2017.
 */

public class EmailSetup_Microsoft extends AppCompatActivity {


    final static String CLIENT_ID = "98bee6aa-be43-400f-a7f9-bf1a7306b686";
    final static String SCOPES[] = {"https://graph.microsoft.com/User.Read"};
    final static String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me";

    /* UI & Debugging Variables */
    private static final String TAG = EmailSetup_Microsoft.class.getSimpleName();
    Button callGraphButton;
    Button signOutButton, btn;

    /* Azure AD Variables */
    private PublicClientApplication sampleApp;
    private AuthenticationResult authResult;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.microsoft_setup);
//
//        btn = (Button) findViewById(R.id.send_mail);
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String na = "krrohith934@gmail.com";
//                List to = Arrays.asList(na.split("\\s*,\\s*"));
//
//                new SendMailTask_Outlook(EmailSetup_Microsoft.this).execute("asaianand@hotmail.com", "Jaiavani@1954", to, "SUB", "hi hw r u");
//
//            }
//        });
//
//
//  /* Configure your sample app and save state for this activity */
//        sampleApp = null;
//        if (sampleApp == null) {
//            sampleApp = new PublicClientApplication(
//                    this.getApplicationContext(),
//                    CLIENT_ID);
//        }
//
//  /* Attempt to get a user and acquireTokenSilent
//   * If this fails we do an interactive request
//   */
//        List<User> users = null;
//
//        try {
//            users = sampleApp.getUsers();
//
//            if (users != null && users.size() == 1) {
//          /* We have 1 user */
//
//                sampleApp.acquireTokenSilentAsync(SCOPES, users.get(0), getAuthSilentCallback());
//            } else {
//          /* We have no user */
//
//          /* Let's do an interactive request */
//                sampleApp.acquireToken(this, SCOPES, getAuthInteractiveCallback());
//            }
//        } catch (MsalClientException e) {
//            Log.d(TAG, "MSAL Exception Generated while getting users: " + e.toString());
//
//        } catch (IndexOutOfBoundsException e) {
//            Log.d(TAG, "User at this position does not exist: " + e.toString());
//        }
//
//
//        onCallGraphClicked();
    }

//    private void onCallGraphClicked() {
//        sampleApp.acquireToken(this, SCOPES, getAuthInteractiveCallback());
//    }
//
//
//    private AuthenticationCallback getAuthInteractiveCallback() {
//        return new AuthenticationCallback() {
//            @Override
//            public void onSuccess(AuthenticationResult authenticationResult) {
//            /* Successfully got a token, call graph now */
//                Log.d(TAG, "Successfully authenticated");
//                Log.d(TAG, "ID Token: " + authenticationResult.getIdToken());
//
//            /* Store the auth result */
//                authResult = authenticationResult;
//
//            /* call Graph */
//                callGraphAPI();
//
//            /* update the UI to post call Graph state */
//                updateSuccessUI();
//            }
//
//            @Override
//            public void onError(MsalException exception) {
//            /* Failed to acquireToken */
//                Log.d(TAG, "Authentication failed: " + exception.toString());
//
//                if (exception instanceof MsalClientException) {
//                /* Exception inside MSAL, more info inside MsalError.java */
//                } else if (exception instanceof MsalServiceException) {
//                /* Exception when communicating with the STS, likely config issue */
//                }
//            }
//
//            @Override
//            public void onCancel() {
//            /* User canceled the authentication */
//                Log.d(TAG, "User cancelled login.");
//            }
//        };
//    }
//
//
//    private void callGraphAPI() {
//        Log.d(TAG, "Starting volley request to graph");
//
//    /* Make sure we have a token to send to graph */
//        if (authResult.getAccessToken() == null) {
//            return;
//        }
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        JSONObject parameters = new JSONObject();
//
//        try {
//            parameters.put("key", "value");
//        } catch (Exception e) {
//            Log.d(TAG, "Failed to put parameters: " + e.toString());
//        }
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, MSGRAPH_URL,
//                parameters, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//            /* Successfully called graph, process data and send to UI */
//                Log.d(TAG, "Response: " + response.toString());
//
//                updateGraphUI(response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d(TAG, "Error: " + error.toString());
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Bearer " + authResult.getAccessToken());
//                return headers;
//            }
//        };
//
//        Log.d(TAG, "Adding HTTP GET to Queue, Request: " + request.toString());
//
//        request.setRetryPolicy(new DefaultRetryPolicy(
//                3000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(request);
//    }
//
//    /* Sets the Graph response */
//    private void updateGraphUI(JSONObject graphResponse) {
//        TextView graphText = (TextView) findViewById(R.id.graphData);
//        graphText.setText(graphResponse.toString());
//    }
//
//
//    /* Set the UI for successful token acquisition data */
//    private void updateSuccessUI() {
////        callGraphButton.setVisibility(View.INVISIBLE);
//        signOutButton.setVisibility(View.VISIBLE);
//        findViewById(R.id.welcome).setVisibility(View.VISIBLE);
//        ((TextView) findViewById(R.id.welcome)).setText("Welcome, " +
//                authResult.getUser().getName());
//        findViewById(R.id.graphData).setVisibility(View.VISIBLE);
//    }
//
//
//    private AuthenticationCallback getAuthSilentCallback() {
//        return new AuthenticationCallback() {
//            @Override
//            public void onSuccess(AuthenticationResult authenticationResult) {
//            /* Successfully got a token, call Graph now */
//                Log.d(TAG, "Successfully authenticated");
//
//            /* Store the authResult */
//                authResult = authenticationResult;
//
//            /* call graph */
//                callGraphAPI();
//
//            /* update the UI to post call Graph state */
//                updateSuccessUI();
//            }
//
//            @Override
//            public void onError(MsalException exception) {
//            /* Failed to acquireToken */
//                Log.d(TAG, "Authentication failed: " + exception.toString());
//
//                if (exception instanceof MsalClientException) {
//                /* Exception inside MSAL, more info inside MsalError.java */
//                } else if (exception instanceof MsalServiceException) {
//                /* Exception when communicating with the STS, likely config issue */
//                } else if (exception instanceof MsalUiRequiredException) {
//                /* Tokens expired or no session, retry with interactive */
//                }
//            }
//
//            @Override
//            public void onCancel() {
//            /* User canceled the authentication */
//                Log.d(TAG, "User cancelled login.");
//            }
//        };
//    }



}