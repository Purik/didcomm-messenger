package com.socialsirius.messenger.use_cases

import androidx.lifecycle.MutableLiveData
import com.sirius.library.agent.aries_rfc.feature_0015_ack.Ack
import com.socialsirius.messenger.models.ChatMessageStatus
import com.socialsirius.messenger.repository.MessageRepository

import com.socialsirius.messenger.sirius_sdk_impl.SDKUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventUseCase @Inject constructor(
    val messageRepository: MessageRepository,
    val sdkUseCase: SDKUseCase
) {



    fun readMessage(id: String, pairwiseDid:String) {
        sdkUseCase.sendStatusFoMessage(id,pairwiseDid, Ack.Status.OK)
        messageRepository.updateStatus(id, ChatMessageStatus.acknowlege)
    }

    fun receiveMessage(id: String, pairwiseDid:String) {
        sdkUseCase.sendStatusFoMessage(id,pairwiseDid, Ack.Status.PENDING)
        messageRepository.updateStatus(id, ChatMessageStatus.received)
    }
}