package com.socialsirius.messenger.service

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.sirius.library.mobile.helpers.ChanelHelper


class FirebaseChannelWorker(appContext: Context, workerParams: WorkerParameters):
       Worker(appContext, workerParams) {
   override fun doWork(): Result {
      val map =  inputData.keyValueMap
       val payloadText = Gson().toJson(map)
       val messageWrapper = SiriusWebSocketListener.parseSocketMessage(payloadText)
       if(messageWrapper?.topic == "indy.transport"){
           ChanelHelper.getInstance().parseMessage(messageWrapper?.messageFromMessageString ?: "")
       }
       return Result.success()
   }
}
