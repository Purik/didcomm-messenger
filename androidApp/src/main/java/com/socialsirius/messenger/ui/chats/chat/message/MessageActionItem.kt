package  com.socialsirius.messenger.ui.chats.chat.message


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