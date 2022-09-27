package com.socialsirius.messenger.ui.activities.splash


import com.socialsirius.messenger.base.providers.ResourcesProvider
import com.socialsirius.messenger.base.ui.BaseActivityModel
import com.socialsirius.messenger.repository.MessageRepository
import com.socialsirius.messenger.sirius_sdk_impl.SDKUseCase
import javax.inject.Inject

class SplashActivityModel @Inject constructor(
    resourceProvider: ResourcesProvider,
    val sdkUseCase: SDKUseCase,
    messageRepository: MessageRepository
) :
    BaseActivityModel(messageRepository) {


    fun isMediatorConnected()  : Boolean{
        return sdkUseCase.isInitiated
    }

}