package com.socialsirius.messenger.service

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.sirius.library.mobile.SiriusSDK
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ConnectSocketWorker(appContext: Context, workerParams: WorkerParameters):
       Worker(appContext, workerParams) {
   override fun doWork(): Result {
       connect()
       return Result.success()
   }

    fun connect(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            GlobalScope.launch {
                if (!task.isSuccessful) {
                    Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                    SiriusSDK.connectToMediator()
                }else{
                   // val token = task.result
                    SiriusSDK.connectToMediator()
                }
            }
        })
    }
}