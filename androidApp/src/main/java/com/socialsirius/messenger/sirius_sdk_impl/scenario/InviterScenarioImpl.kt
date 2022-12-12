package com.socialsirius.messenger.sirius_sdk_impl.scenario


import com.sirius.library.agent.listener.Event
import com.sirius.library.mobile.scenario.impl.InviterScenario
import com.socialsirius.messenger.repository.EventRepository
import com.socialsirius.messenger.repository.MessageRepository

class InviterScenarioImpl  constructor(val messageRepository: MessageRepository,
                                       val  eventRepository: EventRepository) : InviterScenario(eventRepository) {
    override fun onScenarioStart(id : String,event: Event) {
        messageRepository.invitationStartLiveData.postValue(null)
    }

    override fun onScenarioEnd(id : String,event: Event,success: Boolean, error: String?) {
        messageRepository.invitationPolicemanSuccessLiveData.postValue(id)
       // messageRepository?.invitationSuccessLiveData?.postValue(id)
    }

}