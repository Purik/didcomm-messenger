package com.socialsirius.messenger.ui.activities.main




import com.socialsirius.messenger.base.providers.ResourcesProvider
import com.socialsirius.messenger.base.ui.BaseActivityModel
import com.socialsirius.messenger.models.Chats
import com.socialsirius.messenger.models.ui.ItemContacts
import com.socialsirius.messenger.repository.EventRepository
import com.socialsirius.messenger.repository.MessageRepository
import com.socialsirius.messenger.transform.LocalMessageTransform
import javax.inject.Inject


class MainActivityModel @Inject constructor(val messageRepository: MessageRepository)
: BaseActivityModel() {

   val invitationStartLiveData = messageRepository.invitationStartLiveData
   val invitationErrorLiveData = messageRepository.invitationErrorLiveData
   val invitationSuccessLiveData = messageRepository.invitationSuccessLiveData

    val invitationPolicemanSuccessLiveData = messageRepository.invitationPolicemanSuccessLiveData


    val eventStoreLiveData = messageRepository.eventStoreLiveData
    val eventStartLiveData = messageRepository.eventStartLiveData
    val eventStopLiveData = messageRepository.eventStopLiveData

    override fun onViewCreated() {
        super.onViewCreated()
    }

    fun getMessage(id : String) : Chats {
        val localMessage = messageRepository.getItemBy(id)
        return LocalMessageTransform.toItemContacts(localMessage)
    }
}