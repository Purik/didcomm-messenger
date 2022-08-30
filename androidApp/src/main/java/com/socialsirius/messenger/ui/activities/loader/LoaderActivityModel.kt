package com.socialsirius.messenger.ui.activities.loader



import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.socialsirius.messenger.base.providers.ResourcesProvider
import com.socialsirius.messenger.base.ui.BaseActivityModel
import com.socialsirius.messenger.sirius_sdk_impl.SDKUseCase
import com.socialsirius.messenger.repository.UserRepository

import javax.inject.Inject

class LoaderActivityModel @Inject constructor(
    resourceProvider: ResourcesProvider,
    private val sdkUseCase: SDKUseCase,
    private val userRepository: UserRepository
) :
    BaseActivityModel() {
    val initStartLiveData = MutableLiveData<Boolean>()
    val initEndLiveData = MutableLiveData<Boolean>()

        fun initSdk(context: Context){
            userRepository.setupUserFromPref()
            var  login = "defaultWalletLogin" // userRepository.myUser.uid ?: ""
            var  pass = userRepository.myUser.pass ?:""
            var  label = userRepository.myUser.name ?:""
            sdkUseCase.initSdk(context,login,pass, label,object : SDKUseCase.OnInitListener{
                override fun initStart() {
                    initStartLiveData.postValue(true)
                }

                override fun initEnd() {
                    sdkUseCase.isInitiated = true
                    initEndLiveData.postValue(true)
                }

            })
        }


}