package com.socialsirius.messenger.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.sirius.library.mobile.SiriusSDK
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class NetworkStateReceiver : BroadcastReceiver() {

    private fun restoreConnections(context: Context) {
       // val intent = Intent(context, WebSocketService::class.java)
      //  intent.action = WebSocketService.EXTRA_CONNECT
     //   context.startService(intent)
        connect()
        //  MainActivityActivity.getRoster();
    }

    fun connect(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            GlobalScope.launch {
                if (!task.isSuccessful) {
                    Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                    SiriusSDK.connectToMediator()
                }else{
                    val token = task.result
                    SiriusSDK.connectToMediator()
                }
            }

        })
    }

    var previousNetwork: String? = null
    override fun onReceive(context: Context, networkIntent: Intent) {
        val networkInfo = getActiveNetwork(context)
        Log.d("mylog909","networkInfo="+networkInfo +" previousNetwor="+previousNetwork)
        if (isNetworkConnected(networkInfo)) {
            Log.d("mylog909","isNetworkConnected")
            if (previousNetwork != null) {
                Log.d("mylog909","restoreConnections")
                restoreConnections(context)
            }

            previousNetwork = networkInfo!!.typeName
            Log.d("mylog909","previousNetwork="+previousNetwork)
        }
    }

    private fun getActiveNetwork(context: Context): NetworkInfo? {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo
    }

    fun isNetworkConnected(netInfo: NetworkInfo?): Boolean {
        //should check null because in airplane mode it will be null
        return netInfo != null && netInfo.isAvailable && netInfo.isConnected
    }
}