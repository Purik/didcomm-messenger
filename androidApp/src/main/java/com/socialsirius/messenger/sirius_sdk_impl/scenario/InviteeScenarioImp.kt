package com.socialsirius.messenger.sirius_sdk_impl.scenario


import com.sirius.library.mobile.scenario.impl.InviteeScenario
import com.socialsirius.messenger.repository.EventRepository
import com.socialsirius.messenger.repository.MessageRepository
import com.socialsirius.messenger.transform.LocalMessageTransform
import com.sirius.library.mobile.helpers.ScenarioHelper
import com.sirius.library.mobile.scenario.EventAction
import com.sirius.library.mobile.scenario.EventActionListener
import com.sirius.library.agent.listener.Event


class InviteeScenarioImp  constructor(val messageRepository: MessageRepository,
                                      val eventRepository: EventRepository) : InviteeScenario(eventRepository) {

    override fun onScenarioStart(id: String,event: Event) {

    }

    override fun onScenarioEnd(id: String,event: Event,success: Boolean, error: String?) {
        messageRepository.invitationStartLiveData.postValue(null)
        val message = messageRepository.getItemBy(id)
        ScenarioHelper.acceptScenario("Invitee", message?.getId() as? String ?: "", "", object :
            EventActionListener {
            override fun onActionStart(action: EventAction, id: String, comment: String?) {

            }

            override fun onActionEnd(
                action: EventAction,
                id: String,
                comment: String?,
                success: Boolean,
                error: String?
            ) {
                val isError = !success
                if(!isError){
                    messageRepository?.invitationSuccessLiveData?.postValue(id)
                }else{
                    messageRepository?.invitationErrorLiveData?.postValue(Pair(isError, error))
                }

            }

        })

    }

}