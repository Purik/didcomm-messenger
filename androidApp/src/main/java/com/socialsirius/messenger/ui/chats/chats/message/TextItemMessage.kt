package com.socialsirius.messenger.ui.chats.chats.message




import com.sirius.library.agent.aries_rfc.feature_0095_basic_message.Message
import com.sirius.library.agent.listener.Event
import com.socialsirius.messenger.repository.models.LocalMessage


class TextItemMessage: BaseItemMessage {

    constructor(event: Event?) : super(event)
    constructor(localMessage: LocalMessage?) : super(localMessage)

    override fun getType(): MessageType {
        return MessageType.Text
    }

    override fun accept(comment : String?) {

    }

    override fun cancel() {

    }

    override fun getText() : String {
        val messageBasic = message as? Message
        return messageBasic?.content ?: ""
    }

    override fun getTitle() : String {
        return ""
    }

}