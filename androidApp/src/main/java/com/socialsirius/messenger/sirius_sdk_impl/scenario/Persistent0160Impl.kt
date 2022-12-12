package com.socialsirius.messenger.sirius_sdk_impl.scenario

import android.util.Log
import com.sirius.library.agent.aries_rfc.feature_0015_ack.Ack
import com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.messages.ConnRequest
import com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.messages.ConnResponse
import com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.messages.Invitation
import com.sirius.library.agent.connections.Endpoint
import com.sirius.library.agent.listener.Event
import com.sirius.library.agent.pairwise.Pairwise
import com.sirius.library.agent.pairwise.WalletPairwiseList
import com.sirius.library.mobile.SiriusSDK
import com.sirius.library.mobile.helpers.PairwiseHelper
import com.sirius.library.mobile.scenario.impl.Persistent0160Scenario
import com.sirius.library.utils.JSONObject
import com.socialsirius.messenger.repository.EventRepository
import com.socialsirius.messenger.repository.MessageRepository
import com.socialsirius.messenger.service.SiriusWebSocketListener
import com.socialsirius.messenger.utils.NotificationsUtils

class Persistent0160Impl(
    val messageRepository: MessageRepository,
    val eventRepository: EventRepository
) : Persistent0160Scenario(eventRepository) {


    var connectionKey: String? = null


    open fun generateInvitation(serverUri: String): String {
        val verkey = SiriusSDK.context?.crypto?.createKey()
        connectionKey = verkey
        val myEndpoint: Endpoint = SiriusSDK.context?.endpointWithEmptyRoutingKeys
            ?: return ""
        val invitation = Invitation.builder()
            .setLabel(SiriusSDK.label)
            .setRecipientKeys(listOfNotNull(verkey)).setEndpoint(myEndpoint.address).build()
        val qrContent = serverUri + invitation.invitationUrl()
        return qrContent
    }

    override fun onScenarioStart(id: String,event: Event) {

    }

    override fun onScenarioEnd(id: String, event: Event, success: Boolean, error: String?) {
        val event = eventRepository.eventToEvent(event)

        if (event?.second is Invitation) {
            messageRepository.invitationStarInviteeLiveData.postValue(id)
        } else if (event?.second is ConnRequest) {
            val connRequest =  event.second as ConnRequest
            if (SiriusWebSocketListener.isForeground) {
                messageRepository.invitationStartLiveData.postValue(connRequest)
            } else {
                NotificationsUtils.callNewConnectionNotify(connRequest.label,connRequest.theirDid())
            }
            //     val message = messageRepository.getItemBy(id)
            /*  ScenarioHelper.getInstance().acceptScenario("Invitee", message?.getId() as? String ?: "", "", object :
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

              })*/
        } else if (success) {

            if (event?.second is ConnResponse) {
                val connResponse = event?.second as? ConnResponse
                val connectionKey =
                    connResponse?.messageObj?.getJSONObject("connection~sig")?.optString("signer")?:""
                val invitationEvent =
                    eventRepository.messageRepository.getLastMessagesForPairwiseDid2(connectionKey)
               val id =  invitationEvent?.id
               val pairwise =  WalletPairwiseList.restorePairwise(JSONObject(error))
                invitationEvent?.let {
                    eventStorage.eventStore(
                        id ?: "",
                        Pair(pairwise?.their?.did, invitationEvent.message()),
                        pairwise != null
                    )
                }

                messageRepository?.invitationSuccessLiveData?.postValue(id)

            } else if (event?.second is Ack) {
                val ackMessage = event.second as Ack
               val threadId =  ackMessage.getThreadId()
                val invitationEvent =
                    threadId?.let {
                        eventRepository.messageRepository.getItemBy(it)
                    }
                Log.d("mylog20901","ackMessage="+ackMessage +" threadId="+threadId+" invitationEvent="+invitationEvent)
                if (invitationEvent!=null){
                    messageRepository?.invitationSuccessLiveData?.postValue(invitationEvent.id)
                }
            }


        } else {
            messageRepository?.invitationErrorLiveData?.postValue(Pair(true, error))
        }
    }
}