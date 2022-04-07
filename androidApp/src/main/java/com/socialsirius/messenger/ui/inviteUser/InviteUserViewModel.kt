package com.socialsirius.messenger.ui.inviteUser

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.socialsirius.messenger.base.providers.ResourcesProvider
import com.socialsirius.messenger.base.ui.BaseViewModel

import javax.inject.Inject

class InviteUserViewModel @Inject constructor(
    val resourcesProvider: ResourcesProvider
   // val messageUseCase: MessagesUseCase,
  //  val chatsRepository: ChatsRepository
) : BaseViewModel( ) {
    //val repositoryCreatedLiveData = chatsRepository.oneChatCreatedUpdateLiveData
    val qrCodeLiveData = MutableLiveData<String>()
    val shareButtonAction = MutableLiveData<String>()

    override fun onViewCreated() {
        super.onViewCreated()
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
}