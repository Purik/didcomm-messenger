package  com.socialsirius.messenger.ui.chats.chat.item



class TechMessageItem private constructor(
    val text: String,
    val dateTime: String,
    val id: String,
    override var timeInMillis: Long
) : IChatItem() {
    companion object {
  /*      @JvmStatic
        fun map(baseMessage: BaseMessageNew): TechMessageItem {
            val text = baseMessage.textByType
            return TechMessageItem(text = text, id = baseMessage.id,
                dateTime = getMessageDateTime(baseMessage),
                timeInMillis = baseMessage.created_utc_timestamp)
        }*/
    }

    override fun getMessageId(): String {
        return id
    }
}