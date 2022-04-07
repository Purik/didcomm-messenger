package  com.socialsirius.messenger.ui.chats.chat.item

enum class MessageActionItemType {
    DELETE,
    DELETE_LOCALLY,
    QUOTE,
    RESEND
}

class MessageActionItem(
    val type: MessageActionItemType,
    val message: ChatMessageItem,
    val action: (ChatMessageItem) -> Unit) {
}