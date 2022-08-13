package com.socialsirius.messenger.sirius_sdk_impl.scenario

import android.util.Log
import com.sirius.library.agent.aries_rfc.feature_0015_ack.Ack
import com.sirius.library.agent.aries_rfc.feature_0048_trust_ping.Ping
import com.sirius.library.agent.aries_rfc.feature_0048_trust_ping.Pong
import com.sirius.library.agent.listener.Event
import com.sirius.library.messaging.Message
import com.sirius.library.mobile.scenario.BaseScenario
import com.sirius.library.mobile.scenario.EventTransform
import com.socialsirius.messenger.models.ChatMessageStatus
import com.socialsirius.messenger.repository.EventRepository
import com.socialsirius.messenger.repository.MessageRepository

import com.socialsirius.messenger.transform.LocalMessageTransform
import com.socialsirius.messenger.use_cases.EventUseCase
import com.socialsirius.messenger.utils.NotificationsUtils
import kotlin.reflect.KClass

class AckScenarioImpl(val eventRepository: EventRepository, val messageRepository: MessageRepository) : BaseScenario() {

    override fun initMessages(): List<KClass<out Message>> {
        return listOf(Ack::class)
    }



    override fun onScenarioEnd(id: String,success: Boolean, error: String?) {


    }

    override fun onScenarioStart(id: String) {

       /* val localMessage = messageRepository.getItemBy(id)
        val label = localMessage?.restorePairwise()?.their?.label
        val baseItem = LocalMessageTransform.toBaseItemMessage( localMessage)
        NotificationsUtils.callMessageNotify(baseItem.getTitle(),baseItem.getText(),label,null)*/
    }

    override fun start(event: Event): Pair<Boolean, String?> {
        Log.d("mylog2090","PongScenarioImpl event="+event.messageObj)
       val ackMessage =  event.message() as? Ack
       val messId =  ackMessage?.getThreadId() ?:""
        var status = ChatMessageStatus.default
        if(ackMessage?.getStatus() == Ack.Status.PENDING){
            status = ChatMessageStatus.received
        }else if(ackMessage?.getStatus() == Ack.Status.OK){
            status = ChatMessageStatus.acknowlege
        }
        messageRepository.updateStatus(messId, status)
        return Pair(true,messId)
    }

}