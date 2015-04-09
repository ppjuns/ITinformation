package com.rabbit.application.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by Rabbit on 2015/4/9.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo=connectivityManager.getActiveNetworkInfo();
        if(mNetworkInfo!=null&&mNetworkInfo.isAvailable()){


    }else{

            Toast.makeText(context,"当然网络信号不好",Toast.LENGTH_SHORT).show();

        }
}}
