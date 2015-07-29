package com.rabbit.application.ui;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.rabbit.application.BroadcastReceiver.NetworkChangeReceiver;
import com.rabbit.application.util.ActivityCollecter;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Rabbit on 2015/3/27.
 */
public class BaseActivity extends ActionBarActivity {

    private IntentFilter intentFilter;

    private NetworkChangeReceiver mNetworkChangeReceiver;
    public static Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollecter.addAvtivty(this);

        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mNetworkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(mNetworkChangeReceiver, intentFilter);
    }


    @Override
    protected void onResume() {

        super.onResume();


        MobclickAgent.onResume(this);
        MobclickAgent.updateOnlineConfig(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
MobclickAgent.onPause(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollecter.removeActivity(this);
        unregisterReceiver(mNetworkChangeReceiver);

    }

    public static void itemonclick(Context context, String data) {
        context.startActivity(new Intent(context, InfoActivity.class).putExtra("url", data));


    }
}
