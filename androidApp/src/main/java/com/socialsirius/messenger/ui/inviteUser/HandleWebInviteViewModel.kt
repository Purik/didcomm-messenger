package com.socialsirius.messenger.ui.inviteUser

import android.os.Handler
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.providers.ResourcesProvider
import com.socialsirius.messenger.repository.MessageRepository
import com.socialsirius.messenger.ui.scan.MenuScanQrViewModel

import javax.inject.Inject

class HandleWebInviteViewModel @Inject constructor(
    resourcesProvider: ResourcesProvider,
//    messagesUseCase: MessagesUseCase,
//    chatsRepository: ChatsRepository,
    messagesRepository: MessageRepository
 //   messageListenerUseCase: MessageListenerUseCase

) : MenuScanQrViewModel(resourcesProvider, messagesRepository) {

    val errorTextLiveData = MutableLiveData<String>()
    val loadingVisibilityLiveData = MutableLiveData<Int>(View.VISIBLE)
    val splashTextLiveData = MutableLiveData<String>(resourcesProvider.getString(R.string.invitation_in_progress))
    override fun onViewCreated() {
        super.onViewCreated()
    }

    fun setError(error : String?){
        errorTextLiveData.postValue(error)
        splashTextLiveData.postValue("")
        loadingVisibilityLiveData.postValue(View.GONE)
    }
    override fun onCodeScanned(code: String): Boolean {
        return super.onCodeScanned(code)
    }


}