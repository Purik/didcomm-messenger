package com.socialsirius.messenger.ui.scan

import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.net.Uri
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.messages.Invitation
import com.sirius.library.messaging.Message
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

    val showInvitationBottomSheetLiveData = MutableLiveData<Invitation?>()
    val showErrorBottomSheetLiveData = MutableLiveData<String>()

    override fun onViewCreated() {
        super.onViewCreated()
    }

    var isConnecting = false
    fun connectToInvitation(message : Invitation){
        isConnecting = true
        ChanelHelper.getInstance().parseMessage(message.serialize())
    }


    open fun onCodeScanned(result: String, type: Int) : Boolean {
        val message = InvitationHelper.getInstance().parseInvitationLink(result)
        if (message != null) {
            showInvitationBottomSheetLiveData.postValue(message)
            return true
        } else {
            var textError ="The scanned QR code is not an invitation, please scan another QR code."
            if (type ==1){
                textError = "The pasted text from clipboard is not an invitation, please copy to clipboard another text."
            }
            if (type ==2){
                textError = "The URL that you want to parse is not an invitation, please try another URL."
            }
            showErrorBottomSheetLiveData.postValue(textError)
            return false
        }

    }

    fun getMessage(id : String) : Chats {
        val localMessage = messageRepository.getItemBy(id)
        return LocalMessageTransform.toItemContacts(localMessage)
    }


    fun OnReadClick(v : View){
        val text = v.context.getFromClipBoard() ?:""
        if (text.isNullOrEmpty()) {
            val textError ="Text from clipboard is empty. Please copy invitation text first."
            showErrorBottomSheetLiveData.postValue(textError)
            return
        }
        onCodeScanned(text, 1)
    }
}

fun Context.getFromClipBoard() : String? {
    val clipBoardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    return clipBoardManager.primaryClip?.getItemAt(0)?.text?.toString()
}