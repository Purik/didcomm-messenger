/*
package com.socialsirius.messenger.ui.scan

import android.os.Handler

import com.socialsirius.messenger.base.providers.ResourcesProvider
import com.socialsirius.messenger.repository.MessageRepository
import javax.inject.Inject

class HandleWebInviteViewModel @Inject constructor(
    resourcesProvider: ResourcesProvider,
 //   messagesUseCase: MessagesUseCase,
 //   chatsRepository: ChatsRepository,
    messageRepository: MessageRepository
 //   messageListenerUseCase: MessageListenerUseCase

) : MenuScanQrViewModel(resourcesProvider,messageRepository) {


    override fun onViewCreated() {
        super.onViewCreated()
    }

    override fun onCodeScanned(code: String): Boolean {
        showProgressDialog()
        super.onCodeScanned(code)
        hideProgressDialog()
        val handler = Handler()
        handler.postDelayed({
            finishActivityLiveData.postValue(true)
        }, 2000)
        return true
    }


}*/
