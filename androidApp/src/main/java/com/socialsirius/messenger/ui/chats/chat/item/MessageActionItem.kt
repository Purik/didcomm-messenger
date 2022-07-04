package  com.socialsirius.messenger.ui.chats.chat.item

import com.socialsirius.messenger.ui.chats.chats.message.TextItemMessage

enum class MessageActionItemType {
    DELETE,
    DELETE_LOCALLY,
    QUOTE,
    RESEND
}

class MessageActionItem(
    val type: MessageActionItemType,
    val message: TextItemMessage,
    val action: (TextItemMessage) -> Unit) {
}