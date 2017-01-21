package com.example.vincentbevia.networklistenerbroadcaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.vincentbevia.networklistenerbroadcaster.utils.NetworkUtil;

class NetworkChangeReceiver extends BroadcastReceiver {

    private String TAG_NETWORK_STATUS;
    private Intent intent;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        int status = NetworkUtil.getConnectivityStatusString(context);

        if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {

            TAG_NETWORK_STATUS = "NOT CONNECTED";

        } else if (status == 1) {

            TAG_NETWORK_STATUS = "CONNECTED TO WIFI";

        } else if (status == 2) {

            TAG_NETWORK_STATUS = "CONNECTED TO MOBILE";

        } else {
            TAG_NETWORK_STATUS = "UNABLE TO DETERMINE NETWORK STATUS";
        }

        callObservers(context, intent);
    }

    private void callObservers(Context context, Intent intent) {
        this.intent = intent;
        this.intent = new Intent("get-network-status");
        this.intent.putExtra("network_status", TAG_NETWORK_STATUS);
        LocalBroadcastManager.getInstance(context).sendBroadcast(this.intent);
    }
}