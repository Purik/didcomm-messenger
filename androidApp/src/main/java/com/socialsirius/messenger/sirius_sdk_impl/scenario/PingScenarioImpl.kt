package com.socialsirius.messenger.sirius_sdk_impl.scenario

import android.util.Log
import com.sirius.library.agent.aries_rfc.feature_0048_trust_ping.Ping
import com.sirius.library.agent.listener.Event
import com.sirius.library.messaging.Message
import com.sirius.library.mobile.scenario.BaseScenario
import com.socialsirius.messenger.service.SiriusWebSocketListener

import com.socialsirius.messenger.sirius_sdk_impl.SDKUseCase
import kotlin.reflect.KClass

class PingScenarioImpl(val sdkUseCase: SDKUseCase) : BaseScenario() {
    override fun initMessages(): List<KClass<out Message>> {
        return listOf(Ping::class)
    }

    override suspend fun start(event: Event): Pair<Boolean, String?> {
        Log.d("mylog2090","TRUST PING PingScenarioImpl event="+event.messageObj)
        val pingMessageId = event.message()?.getId()
        if (SiriusWebSocketListener.isForeground) {
            Log.d("mylog2090","TRUST PING PingScenarioImpl SiriusWebSocketListener.isForeground=")
            sdkUseCase.sendTrustPingMessageForPairwise(event.pairwise?.their?.did ?: "", pingMessageId)
        }
        return Pair(true, null)
    }

    override fun onScenarioStart(id: String,event: Event) {

    }

    override fun onScenarioEnd(id: String,event: Event, success: Boolean, error: String?) {

    }
}