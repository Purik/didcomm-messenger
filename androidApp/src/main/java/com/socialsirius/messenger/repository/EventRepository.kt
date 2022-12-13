package com.socialsirius.messenger.repository

import androidx.lifecycle.MutableLiveData
import com.socialsirius.messenger.repository.models.LocalMessage
import com.socialsirius.messenger.utils.DateUtils
import com.sirius.library.agent.aries_rfc.feature_0036_issue_credential.messages.OfferCredentialMessage
import com.sirius.library.agent.aries_rfc.feature_0036_issue_credential.messages.ProposeCredentialMessage
import com.sirius.library.agent.aries_rfc.feature_0037_present_proof.messages.RequestPresentationMessage
import com.sirius.library.agent.aries_rfc.feature_0113_question_answer.messages.QuestionMessage
import com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.messages.ConnRequest
import com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.messages.Invitation
import com.sirius.library.agent.listener.Event
import com.sirius.library.messaging.Message
import com.sirius.library.mobile.scenario.EventStorageAbstract


import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

enum class EventFields{
    isCanceled,
    isAccepted,
    canceledCause,
    acceptedComment
}


@Singleton
class EventRepository @Inject constructor(val messageRepository: MessageRepository) :
    EventStorageAbstract {


    companion object {
        fun createParams(isCanceled: Boolean? = null, canceledCause : String? = null,
                         isAccepted: Boolean? = null,acceptedComment : String? = null) : Map<String,Any?>{
            val map = mutableMapOf<String,Any?>()
            isCanceled?.let { map.put(EventFields.isCanceled.name,isCanceled) }
            canceledCause?.let { map.put(EventFields.canceledCause.name,canceledCause) }
            isAccepted?.let { map.put(EventFields.isAccepted.name,isAccepted) }
            acceptedComment?.let { map.put(EventFields.acceptedComment.name,acceptedComment) }
            return map

        }
    }

    override fun eventStore(
        id: String,
        event: Pair<String?, Message?>?,
        fields: Map<String, Any?>?
    ) {
        val localMessage = LocalMessage(id, event?.first)
        localMessage.message = event?.second?.serialize()

        if (event?.second is com.sirius.library.agent.aries_rfc.feature_0095_basic_message.Message) {
            localMessage.type = "text"
        } else if (event?.second is Invitation) {
            localMessage.type = "invitation"
            if( localMessage.pairwiseDid.isNullOrEmpty()){
                localMessage.pairwiseDid = (event?.second as? Invitation)?.recipientKeys()?.get(0)
            }else{
                localMessage.pairwiseDid = event.first
            }
        } else if ( event?.second is ConnRequest) {
            localMessage.type = "invitation"
        } else if (event?.second is OfferCredentialMessage) {
            localMessage.type = "offer"
        } else if (event?.second is RequestPresentationMessage) {
            localMessage.type = "prover"
        } else if (event?.second is QuestionMessage) {
            localMessage.type = "question"
        } else if (event?.second is ProposeCredentialMessage) {
            localMessage.type = "propose"
        }

        if (event?.second?.messageObjectHasKey("~timing") == true) {
            val sentTimeObject = event?.second?.getJSONOBJECTFromJSON("~timing")
            val outTimeString = sentTimeObject?.optString("out_time")
            localMessage.sentTime = DateUtils.getDateFromString(
                outTimeString,
                RequestPresentationMessage.TIME_FORMAT, true
            )
        }
        if (localMessage.sentTime == null) {
            localMessage.sentTime = Date()
        }

        fields?.let {
            val isCanceled =  fields.get(EventFields.isCanceled.name) as? Boolean
            isCanceled?.let {  localMessage.isCanceled = isCanceled }

            val canceledCause =  fields.get(EventFields.canceledCause.name) as? String
            canceledCause?.let {  localMessage.canceledCause = canceledCause }

            val isAccepted =  fields.get(EventFields.isAccepted.name) as? Boolean
            isAccepted?.let {    localMessage.isAccepted = isAccepted}

            val acceptedComment =  fields.get(EventFields.acceptedComment.name) as? String
            acceptedComment?.let {   localMessage.acceptedComment = acceptedComment}

        }
        messageRepository.createOrUpdateItem(localMessage)
    }


    override fun eventRemove(id: String) {
        //   messageRepository.r
    }

    fun eventToEvent(event:Event): Pair<String, Message>?{
        event.message()?.let {
            return Pair(event.pairwise?.their?.did ?: "", it)
        }
        return null
    }

    override fun getEvent(id: String): Pair<String, Message>? {
        val message = messageRepository.getItemBy(id)
        message?.let { local ->
            val restoredMessage = local.message()
            restoredMessage?.let {
                return Pair(local.pairwiseDid ?: "", it)
            }
        }
        return null
    }
}