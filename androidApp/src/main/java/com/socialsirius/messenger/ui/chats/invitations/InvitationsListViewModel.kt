package com.socialsirius.messenger.ui.chats.invitations

import androidx.lifecycle.MutableLiveData
import com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.messages.ConnRequest
import com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.messages.Invitation
import com.sirius.library.mobile.helpers.PairwiseHelper
import com.socialsirius.messenger.base.ui.BaseViewModel
import com.socialsirius.messenger.models.Chats
import com.socialsirius.messenger.repository.MessageRepository
import com.socialsirius.messenger.transform.LocalMessageTransform
import com.socialsirius.messenger.transform.PairwiseTransform
import com.socialsirius.messenger.utils.extensions.observeOnce
import javax.inject.Inject

class InvitationsListViewModel @Inject constructor(val messageRepository: MessageRepository) : BaseViewModel() {

    val emptyStateLiveData = MutableLiveData<Boolean>()
    // val chatsLiveData = chatsRepository.result
    val chatsListLiveData = MutableLiveData<List<Chats>>(listOf())
    var originalList : List<Chats> = listOf()
    val chatsSelectLiveData = MutableLiveData<Chats?>()
    val showInvitationBottomSheetLiveData = MutableLiveData<Invitation?>()

    override fun onCreateview() {
        super.onCreateview()
        createList()
    }

    fun onSelectChat(chat: Chats) {
        chatsSelectLiveData.value = chat
    }

    fun updateList(list : List<Chats>){
        emptyStateLiveData.postValue(list.isEmpty())
        chatsListLiveData.postValue(list)
    }

    private fun createList() {
        messageRepository.getUnacceptedInvitationMessages().observeOnce(this){listMessage->
            val list = listMessage.map {
                var label = ""
               val message =  it.message()
                if (message is Invitation){
                   label =  message.label() ?: ""
                }else
                if (message is ConnRequest){
                    label =  message.label ?: ""
                }else{
                    label = message?.serialize() ?: ""
                }

                val chats =   Chats(it.pairwiseDid ?:"",label)
                chats.lastMessage = it
                return@map chats
            }
            updateList(list)

        }


    }
}