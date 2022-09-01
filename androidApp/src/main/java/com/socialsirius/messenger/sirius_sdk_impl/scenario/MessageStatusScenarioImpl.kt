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
import com.socialsirius.messenger.sirius_sdk_impl.SDKUseCase

import com.socialsirius.messenger.transform.LocalMessageTransform
import com.socialsirius.messenger.use_cases.EventUseCase
import com.socialsirius.messenger.utils.NotificationsUtils
import kotlin.reflect.KClass

class MessageStatusScenarioImpl(val sdkUseCase: SDKUseCase) : BaseScenario() {

    override fun initMessages(): List<KClass<out Message>> {
        return listOf(com.sirius.library.agent.aries_rfc.feature_0095_basic_message.Message::class)
    }



    override fun onScenarioEnd(id: String,success: Boolean, error: String?) {


    }

    override fun onScenarioStart(id: String) {

    }

    override fun start(event: Event): Pair<Boolean, String?> {
        val pairwiseDid = event?.pairwise?.their?.did
        sdkUseCase.sendStatusFoMessage(id, pairwiseDid ?: "", Ack.Status.PENDING)
        return Pair(true, null)
    }

}