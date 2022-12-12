package com.socialsirius.messenger.sirius_sdk_impl.scenario

import com.sirius.library.agent.aries_rfc.feature_0015_ack.Ack
import com.sirius.library.agent.listener.Event
import com.sirius.library.messaging.Message
import com.sirius.library.mobile.scenario.BaseScenario
import com.sirius.library.mobile.scenario.EventTransform
import com.socialsirius.messenger.repository.MessageRepository
import com.socialsirius.messenger.sirius_sdk_impl.SDKUseCase
import com.socialsirius.messenger.transform.LocalMessageTransform
import com.socialsirius.messenger.utils.NotificationsUtils
import kotlin.reflect.KClass

class NotificationScenarioImpl(
    val messageRepository: MessageRepository
) : BaseScenario() {

    override fun initMessages(): List<KClass<out Message>> {
        return listOf(com.sirius.library.agent.aries_rfc.feature_0095_basic_message.Message::class)
    }


    override fun onScenarioEnd(id: String, event: Event,success: Boolean, error: String?) {

    }

    override fun onScenarioStart(id: String,event: Event) {

    }

    override suspend fun start(event: Event): Pair<Boolean, String?> {
        val localMessage = messageRepository.getItemBy(id)
        val label = event?.pairwise?.their?.label
        val pairwiseDid = event?.pairwise?.their?.did
        val baseItem = LocalMessageTransform.toBaseItemMessage(localMessage)
        if(pairwiseDid != messageRepository.visiblePairwiseDid){
            NotificationsUtils.callMessageNotify(label, baseItem.getText(), label, null)
        }
        return Pair(true, null)
    }

}