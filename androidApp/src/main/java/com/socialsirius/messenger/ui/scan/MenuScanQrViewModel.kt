package com.socialsirius.messenger.ui.scan

import android.net.Uri
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sirius.library.mobile.helpers.ChanelHelper
import com.sirius.library.mobile.helpers.InvitationHelper
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.providers.ResourcesProvider
import com.socialsirius.messenger.base.ui.BaseViewModel
import com.socialsirius.messenger.models.Chats
import com.socialsirius.messenger.models.ui.ItemContacts
import com.socialsirius.messenger.repository.MessageRepository
import com.socialsirius.messenger.transform.LocalMessageTransform

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

open class MenuScanQrViewModel @Inject constructor(
    open var resourcesProvider: ResourcesProvider,
  //  open var messagesUseCase: MessagesUseCase,
//    open var chatsRepository: ChatsRepository,
    open  var messageRepository: MessageRepository
   // open  var messageListenerUseCase: MessageListenerUseCase

) : BaseViewModel() {

    val invitationStartLiveData = messageRepository.invitationStartLiveData
    val invitationErrorLiveData = messageRepository.invitationErrorLiveData
    val invitationSuccessLiveData = messageRepository.invitationSuccessLiveData
    val invitationPolicemanSuccessLiveData = messageRepository.invitationPolicemanSuccessLiveData

    val goToNewSecretChatLiveData = MutableLiveData<Chats?>()

    override fun onViewCreated() {
        super.onViewCreated()
    }

    open fun onCodeScanned(result: String) : Boolean {
        val message = InvitationHelper.getInstance().parseInvitationLink(result)
        if (message != null) {
            ChanelHelper.getInstance().parseMessage(message)
            return true
        } else {
            val textError: String ="The scanned QR code is not an invitation, please scan another QR code."
            onShowToastLiveData.postValue(textError)
            return false
        }

    }

    fun getMessage(id : String) : Chats {
        val localMessage = messageRepository.getItemBy(id)
        return LocalMessageTransform.toItemContacts(localMessage)
    }

}