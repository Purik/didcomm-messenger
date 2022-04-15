package  com.socialsirius.messenger.ui.chats.chat.item



class SecretInviteItem private constructor(
        val id: String,
        val text: String,
        override var timeInMillis: Double
) : IChatItem() {

    companion object {
    /*    @JvmStatic
        fun map(baseMessage: BaseMessageNew, chats: Chats?): SecretInviteItem {
            var text = App.getContext().getString(R.string.secret_invite_message_request)
            if (chats != null) {
                if (chats is SecretChats) {
                    if (chats?.indyDidTo != null) {
                        text = App.getContext().getString(R.string.secret_invite_message_request_write_first)
                    }
                }
            }


            if (baseMessage.messageUserType == BaseMessageNew.MessageUserType.OutComing) {
                text = App.getContext().getString(R.string.secret_invite_message_send)
                if (chats != null) {
                    if (chats is SecretChats) {
                        if (chats?.indyDidTo != null) {
                            text = App.getContext().getString(R.string.secret_invite_message_send_write_first)
                        }
                    }
                }

            }
            return SecretInviteItem(
                    id = baseMessage.id,
                    text = text,
                    timeInMillis = baseMessage.created_utc_timestamp
            )
        }*/
    }

    override fun getMessageId(): String {
        return id
    }
}