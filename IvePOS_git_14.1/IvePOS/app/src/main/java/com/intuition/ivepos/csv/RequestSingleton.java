package com.intuition.ivepos.csv;


import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestSingleton {
    private static RequestSingleton sSoleInstance;
    private static RequestQueue reQuestQue;

    private RequestSingleton(){}  //private constructor.

    public static RequestSingleton getInstance(Context context){
        if (sSoleInstance == null){ //if there is no instance available... create new one
            sSoleInstance = new RequestSingleton();
            reQuestQue = Volley.newRequestQueue(context);
        }

        return sSoleInstance;
    }


    public  synchronized RequestQueue getInstance() {
        Log.d("Request Que Obj",reQuestQue.hashCode()+"");
        return reQuestQue;
    }}
