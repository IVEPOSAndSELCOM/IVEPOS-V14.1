package com.intuition.ivepos;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
//import com.nimbusds.jwt.JWTClaimsSet.Builder;
import com.nimbusds.jwt.SignedJWT;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class MainActivity3 extends AppCompatActivity {

    EditText et_company,et_password, et_store,et_device, et_mcc;
    TextView tv_passwordmsg;
    String company, store, device,password,confpassword, mcc;
    TextView tv_signUp;
    int i_cfg = 0;

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    SimpleDateFormat sdff2, sdff1;
    String currentDateandTimee1;
    String timee1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity3);

        final ProgressDialog dialog = new ProgressDialog(MainActivity3.this, R.style.timepicker_date_dialog);

        Button login1 = (Button) findViewById(R.id.login1);
        login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SecureRandom rnd = new SecureRandom();
                StringBuilder sb = new StringBuilder( 16 );
                for( int i = 0; i < 16; i++ )
                    sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );

//                return sb.toString();

                Date dt = new Date();
                sdff1 = new SimpleDateFormat("hhmmss");
                timee1 = sdff1.format(dt);

                String req= "{\"userType\":\"Admin\",\"userName\":\"470000095241139\",\"password\":\"6771\",\"source\":\"Mobile\",\"channel\":\" Biller - Intuitions \",\"reqDate\":\"20200316\",\"reqTime\":\""+timee1+"\",\"userfield1\":\"\",\"userfield2\":\"\"}";
//                String req= "{\"userType\":\"Operator\",\"userName\":\"9820596973\",\"password\":\"4321\",\"source\":\"Mobile\",\"channel\":\" Biller - Intuitions \",\"reqDate\":\"20200316\",\"reqTime\":\"095800\",\"userfield1\":\"\",\"userfield2\":\"\"}";

                String SUBJECT = "API_SERVICE";
                String AUDIENCE= "OCEAN";
                String ISSUER="TERMINAL";
                String PAYLOAD= "PAYLOAD";
                Date ISSUETIME = new Date();
                String JWT_ID = sb.toString(); //which will be also passed in web service call request.
                 int       ACCESS_TOKEN_VALIDITY_SECONDS = 10*60*1000;

                JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                        .subject(SUBJECT)
                        .audience(Arrays.asList(AUDIENCE))
                        .issuer(ISSUER)
                        .issueTime(ISSUETIME)
                        .jwtID(JWT_ID)
                        .expirationTime(getSystemDateTime(ACCESS_TOKEN_VALIDITY_SECONDS))
                        .claim(PAYLOAD, req).build();



                JWSSigner jws = null;
                SignedJWT signedJWT = null;
                try {
                    byte[] data = "QEU1D9GOC7Y6TR9AYSOJ4WPBA2CRPN2I".getBytes("UTF-8");//cryptosign
                    jws = new MACSigner(data);
                    signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256),claimsSet);
                    signedJWT.sign(jws);
                } catch (KeyLengthException e) {
                    e.printStackTrace();
                } catch (JOSEException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                byte[] key = new byte[0];//cryptokey
                try {
                    key = "UYNCSZUANSTKRRC0F5DJM2RXVN6ZDGXS".getBytes("UTF-8");//cryptokey
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                JWEObject jweObject = new JWEObject(new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256).contentType("JWT").build(), new Payload(signedJWT));
                try {
                    jweObject.encrypt(new DirectEncrypter(key));
                } catch (JOSEException e) {
                    e.printStackTrace();
                }
                final String encrypt = jweObject.serialize();
                System.out.println("encrypt "+encrypt);

/////////////////////////////////////////////////////////////////


                JSONObject params = new JSONObject();

                try {
                    params.put("payload",encrypt);
                    params.put("sequenceNo",JWT_ID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestQueue queue = Volley.newRequestQueue(MainActivity3.this);
                JsonObjectRequest sr = new JsonObjectRequest(
                        com.android.volley.Request.Method.POST,
                        "https://www.fdmerchantservices.com/fuelmerchant/v1/BLINTUIT/userLogin",params,
                        new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject responseString) {
                                String response= "";
                                String payload= "";
                                String sequenceNo= "";
                                JSONObject jsonObject=null;
                                try {
                                    jsonObject=responseString;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    response = jsonObject.getString("statusCode");
                                    payload = jsonObject.getString("payload");
                                    sequenceNo = jsonObject.getString("sequenceNo");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if(response.equalsIgnoreCase("200")){

//                                    parseJsonSales(jsonObject);
                                    System.out.println("success");

                                    try {
                                        decrypt(encrypt, sequenceNo, payload);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }else{
                                    Toast.makeText(MainActivity3.this, "download failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Signup confirm", "Error: " + error.getMessage());
                            }
                        })  {

                };

                sr.setRetryPolicy(new DefaultRetryPolicy(0,-1,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(sr);
            }
        });

    }

    private Date getSystemDateTime(int access_token_validity_seconds) {
        Date date = new Date(System.currentTimeMillis() + access_token_validity_seconds);
        System.out.println("System time "+date);
        return date;
    }

    private Date getSystemDateTime() {
        Date date = new Date(System.currentTimeMillis());
        System.out.println("System time "+date);
        return date;
    }

    public void decrypt(String encrypt, String seqNo, String payload) throws ParseException {
        String ALG ="DIR";
        String ENC ="A128CBC-HS256";

        JWEObject jwe = JWEObject.parse(payload);
        String alg = jwe.getHeader().getAlgorithm().getName();
        String enc = jwe.getHeader().getEncryptionMethod().getName();

        if(!StringUtils.equalsIgnoreCase(ALG, alg) || !StringUtils.equalsIgnoreCase(ENC, enc)) {
        //ERROR algorithm and encryption mode mis-match
        }

        try {
            byte[] data = "UYNCSZUANSTKRRC0F5DJM2RXVN6ZDGXS".getBytes("UTF-8");//cryptokey
            jwe.decrypt(new DirectDecrypter(data));
        } catch (JOSEException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        SignedJWT signJwt = jwe.getPayload().toSignedJWT();
            //Signature Verification
        try {
            byte[] data = "QEU1D9GOC7Y6TR9AYSOJ4WPBA2CRPN2I".getBytes("UTF-8");//crypto sign
            if(!signJwt.verify(new MACVerifier(data))) {
                //ERROR in data verification.
            }
        } catch (JOSEException e) {
            e.printStackTrace();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JWTClaimsSet claimsSet = signJwt.getJWTClaimsSet();
            //Time expire check
        final Date expiration = claimsSet.getExpirationTime();
        boolean isExpire = expiration.before(getSystemDateTime());
        if(isExpire) {
            //ERROR service time expire.
        }

        //JWT ID check
        if(!StringUtils.equalsIgnoreCase(claimsSet.getJWTID(), seqNo)) {
            //ERROR in JWT ID and api sequence number mis-match.
        }

        String finaldata = (String)signJwt.getJWTClaimsSet().getClaim("PAYLOAD");

        JSONObject obj = null;
        try {
            obj = new JSONObject(finaldata);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("My App", obj.toString());

    }
}
