package com.socialsirius.messenger.ui.inviteUser

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.MutableLiveData
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.providers.ResourcesProvider
import com.socialsirius.messenger.base.ui.BaseViewModel
import com.socialsirius.messenger.models.Chats
import com.socialsirius.messenger.repository.MessageRepository
import com.socialsirius.messenger.sirius_sdk_impl.SDKUseCase
import com.socialsirius.messenger.transform.LocalMessageTransform
import javax.inject.Inject


class InviteUserViewModel @Inject constructor(
    val resourcesProvider: ResourcesProvider,
    val sdkUseCase: SDKUseCase,
    val messageRepository: MessageRepository
   // val messageUseCase: MessagesUseCase,
  //  val chatsRepository: ChatsRepository
) : BaseViewModel( ) {
    //val repositoryCreatedLiveData = chatsRepository.oneChatCreatedUpdateLiveData
    val qrCodeLiveData = MutableLiveData<String>()
    val shareButtonAction = MutableLiveData<String?>()
    //val invitationStartLiveData = messageRepository.invitationStartLiveData
    val invitationErrorLiveData = messageRepository.invitationErrorLiveData
    val invitationSuccessLiveData = messageRepository.invitationSuccessLiveData

    val invitationPolicemanSuccessLiveData = messageRepository.invitationPolicemanSuccessLiveData

    override fun onViewCreated() {
        super.onViewCreated()
        val inviteLink = sdkUseCase.generateInvitation() ?:""
        Log.d("mylog200","inviteLink="+inviteLink)
        qrCodeLiveData.value = inviteLink
      /* val inviteWrapper =  Feature0160(true).generateMyInvite()
        val messageInvite = inviteWrapper.indyMessageString
        val messageDid = inviteWrapper.indyMessage.firstRecipient
        val user = RosterUser()
        user.name = inviteWrapper.indyMessage.label
        val secretChat = SecretChats(user, messageDid, messageInvite)
        val indyTransportMessage = messageUseCase.createIndyTransportMessage(messageInvite, secretChat)
        val gson = Gson()
        val mesString = gson.toJson(indyTransportMessage, MessagesNew::class.java)
        secretChat.inviteMessage = mesString
        secretChat.isHidden = true
        chatsRepository.createSecretChatWith(secretChat).observeOnce(this) {
            chatsRepository.getAllChats(true)
        }*/
       /* messagesRepository.sendMessageWithOutSave(indyTransportMessage).observeOnceForDone(this) {
            if (it.status == Status.SUCCESS) {
                chatsRepository.createSecretChatWith(secretChats).observeOnce(this) {
                    chatsRepository.getAllChats(true)
                    goToNewSecretChatLiveData.postValue(secretChats)
                }
            } else if (it.status == Status.ERROR) {
                onShowToastLiveData.postValue(resourceProvider.getString(R.string.error))
            }
        }*/

       // val inviteLink: String = inviteWrapper.generateInviteLink(AppPref.getInstance().getMainDomain(true, true))
      //  qrCodeLiveData.value = inviteLink

    }

    fun onShareButtonClick(v: View?) {
        shareButtonAction.value = qrCodeLiveData.value
    }


    fun onCopyButtonClick(v: View) {
        val clipboard: ClipboardManager? = v.context.getSystemService(CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = ClipData.newPlainText("Invitation QR", qrCodeLiveData.value)
        clipboard?.setPrimaryClip(clip)
       onShowToastLiveData.postValue(resourcesProvider.getString(R.string.copy_to_clipboard_succes))
    }


    fun getMessage(id : String) : Chats {
        val localMessage = messageRepository.getItemBy(id)
        return LocalMessageTransform.toItemContacts(localMessage)
    }


}