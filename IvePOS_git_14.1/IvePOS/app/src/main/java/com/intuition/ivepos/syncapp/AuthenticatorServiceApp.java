package com.intuition.ivepos.syncapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by HP on 8/10/2018.
 */

public class AuthenticatorServiceApp extends Service {

    // Instance field that stores the authenticator object
    private AuthenticatorApp mAuthenticator;
    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new AuthenticatorApp(this);
    }
    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}