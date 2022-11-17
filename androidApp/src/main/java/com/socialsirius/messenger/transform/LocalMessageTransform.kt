package com.socialsirius.messenger.transform

import androidx.lifecycle.LiveData
import com.sirius.library.agent.aries_rfc.feature_0036_issue_credential.messages.OfferCredentialMessage
import com.sirius.library.agent.aries_rfc.feature_0037_present_proof.messages.RequestPresentationMessage
import com.sirius.library.agent.aries_rfc.feature_0095_basic_message.Message
import com.sirius.library.agent.aries_rfc.feature_0113_question_answer.messages.QuestionMessage
import com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.messages.ConnRequest
import com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.messages.Invitation
import com.sirius.library.agent.listener.Event
import com.socialsirius.messenger.models.ui.ItemActions

import com.socialsirius.messenger.repository.MessageRepository
import com.socialsirius.messenger.repository.models.LocalMessage

import com.sirius.library.agent.aries_rfc.feature_0036_issue_credential.messages.ProposeCredentialMessage
import com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.messages.ConnResponse
import com.socialsirius.messenger.models.Chats
import com.socialsirius.messenger.ui.chats.chat.message.*


import java.util.*

class LocalMessageTransform {

    companion object {
        fun toItemContacts(localMessage: LocalMessage?): Chats {
            if (localMessage == null) {
                return Chats()
            }
            val message = localMessage.message()
            var title = ""
            if (message is Invitation) {
                title = message.label() ?:""
            }
            var id = localMessage.id
            val pairwise = localMessage.restorePairwise()
            pairwise?.let {
                id = localMessage.pairwiseDid
                title = pairwise.their.label ?:""
            }
            val contact = Chats(id ?: "", title)
            return contact
        }

        fun toBaseItemMessage(localMessage: LocalMessage?): BaseItemMessage {
            if (localMessage == null) {
                return TextItemMessage(localMessage = null)
            }
            val message = localMessage.message()
            if (message is Invitation) {
                val message = ConnectItemMessage(localMessage)
                return message
            }
             if(message is ConnRequest){
                 val message = ConnectItemMessage(localMessage)
                 return message
             }
            if(message is ConnResponse){
                val message = ConnectItemMessage(localMessage)
                return message
            }
            if (message is OfferCredentialMessage) {
                return OfferItemMessage(localMessage)
            }
            if (message is RequestPresentationMessage) {
                return ProverItemMessage(localMessage)
            }
            if (message is QuestionMessage) {
                return QuestionItemMessage(localMessage)
            }

            if (message is ProposeCredentialMessage) {
                return ProposeCredentialItemMessage(localMessage)
            }

            if (message is Message) {
                return TextItemMessage(localMessage)
            }
            return TextItemMessage(localMessage = null)
        }

        fun toItemActions(localMessage: LocalMessage?): ItemActions {
            if (localMessage == null) {
                return ItemActions()
            }
            val message = localMessage.message()
            var type: ItemActions.ActionType? = null
            var hint = ""
            if (message is OfferCredentialMessage) {
                type = ItemActions.ActionType.Offer
            }
            if (message is Invitation) {
                type = ItemActions.ActionType.Connect
                hint = message.label() ?: ""
            }

            if (message is OfferCredentialMessage) {
                type = ItemActions.ActionType.Offer
               // hint = message.label() ?: ""
            }
            if (message is RequestPresentationMessage) {
                type = ItemActions.ActionType.Request
                // hint = message.label() ?: ""
            }
            if (message is QuestionMessage) {
                type = ItemActions.ActionType.Question
                // hint = message.label() ?: ""
            }
            var id = localMessage.id
            val pairwise = localMessage.restorePairwise()
            pairwise?.let {
              //  id = localMessage.pairwiseDid
                hint = pairwise.their.label ?:""
            }
            return ItemActions(id ?: "", type, hint)
        }


        fun toLocalMessage(
            itemContacts: Chats?,
            messageRepository: MessageRepository
        ): LiveData<LocalMessage?> {
            return messageRepository.getItemById(itemContacts?.id ?: "")
        }

        fun toLocalMessage(
            itemActions: ItemActions?,
            messageRepository: MessageRepository
        ): LiveData<LocalMessage?> {
            return messageRepository.getItemById(itemActions?.id ?: "")
        }
    }
}