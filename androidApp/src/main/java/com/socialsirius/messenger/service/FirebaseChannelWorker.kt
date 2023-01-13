package com.socialsirius.messenger.service

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.sirius.library.mobile.helpers.ChanelHelper
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.sirius_sdk_impl.SDKUseCase


class FirebaseChannelWorker(val appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val map = inputData.keyValueMap
        val payloadText = Gson().toJson(map)

        val messageWrapper = SiriusWebSocketListener.parseSocketMessage(payloadText)
        initSdk(appContext, messageWrapper)
        return Result.success()
    }

    fun parseMessage(messageWrapper: ChannelMessageWrapper?){
        if (messageWrapper?.topic == "indy.transport") {
            ChanelHelper.parseMessage(messageWrapper?.messageFromMessageString ?: "")
        } else {

        }
    }

    fun initSdk(context: Context, messageWrapper: ChannelMessageWrapper?) {
        val userRepository = App.getInstance().appComponent.provideUserRepository()
        val sdkUseCase = App.getInstance().appComponent.provideSDKUseCase()
        sdkUseCase?.let {
            if (it.isInitiated) {
                parseMessage(messageWrapper)
            } else {
                userRepository?.let {
                    userRepository.setupUserFromPref()
                    var login = "defaultWalletLogin" // userRepository.myUser.uid ?: ""
                    var pass = userRepository.myUser.pass ?: ""
                    var label = userRepository.myUser.name ?: ""
                    sdkUseCase.initSdk(
                        context,
                        login,
                        pass,
                        label,
                        object : SDKUseCase.OnInitListener {
                            override fun initStart() {
                                //  initStartLiveData.postValue(true)
                            }

                            override fun initEnd() {
                                sdkUseCase.isInitiated = true
                                parseMessage(messageWrapper)
                                //  initEndLiveData.postValue(true)
                            }

                        })
                }
            }
        }


    }

}
