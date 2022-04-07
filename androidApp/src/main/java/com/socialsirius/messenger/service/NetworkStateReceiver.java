package com.socialsirius.messenger.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetworkStateReceiver extends BroadcastReceiver {

    private void restoreConnections(Context context) {

     /*   Intent intent = new Intent(context, SmackService.class);
        intent.setAction(StaticFields.EXTRA_RECONNECT);
        context.startService(intent);
        MainActivityActivity.getRoster();*/
    }

    String previousNetwork = null;

    @Override
    public void onReceive(Context context, Intent networkIntent) {
        NetworkInfo networkInfo = getActiveNetwork(context);
        if (isNetworkConnected(networkInfo)) {
            if (previousNetwork != null) {
                restoreConnections(context);
            }
            previousNetwork = networkInfo.getTypeName();
        }

    }

    private NetworkInfo getActiveNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }


    boolean isNetworkConnected(NetworkInfo netInfo) {
        //should check null because in airplane mode it will be null
        return netInfo != null && netInfo.isAvailable() && netInfo.isConnected();

    }

}