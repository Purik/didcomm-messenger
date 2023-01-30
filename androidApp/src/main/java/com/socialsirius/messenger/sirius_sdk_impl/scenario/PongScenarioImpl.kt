package com.socialsirius.messenger.sirius_sdk_impl.scenario

import android.util.Log
import com.sirius.library.agent.aries_rfc.feature_0048_trust_ping.Ping
import com.sirius.library.agent.aries_rfc.feature_0048_trust_ping.Pong
import com.sirius.library.agent.listener.Event
import com.sirius.library.messaging.Message
import com.sirius.library.mobile.scenario.BaseScenario
import com.sirius.library.mobile.scenario.EventTransform
import com.socialsirius.messenger.repository.EventRepository
import com.socialsirius.messenger.repository.MessageRepository
import com.socialsirius.messenger.service.SiriusWebSocketListener
import com.socialsirius.messenger.transform.LocalMessageTransform
import com.socialsirius.messenger.use_cases.EventUseCase
import com.socialsirius.messenger.utils.NotificationsUtils
import kotlin.reflect.KClass

class PongScenarioImpl(val eventRepository: EventRepository, val messageRepository: MessageRepository) : BaseScenario() {

    override fun initMessages(): List<KClass<out Message>> {
        return listOf(Pong::class)
    }



    override fun onScenarioEnd(id: String,event: Event,success: Boolean, error: String?) {
            messageRepository.pongMutableLiveData.postValue(Pair(true, error?:""))
    }

    override fun onScenarioStart(id: String,event: Event) {

       /* val localMessage = messageRepository.getItemBy(id)
        val label = localMessage?.restorePairwise()?.their?.label
        val baseItem = LocalMessageTransform.toBaseItemMessage( localMessage)
        NotificationsUtils.callMessageNotify(baseItem.getTitle(),baseItem.getText(),label,null)*/
    }

    override suspend fun start(event: Event): Pair<Boolean, String?> {
        Log.d("TRUST PING ","PongScenarioImpl event="+event.messageObj)
        return Pair(true,event.pairwise?.their?.did)
    }

}