package  com.socialsirius.messenger.ui.chats.chat.message


class TechMessageItem constructor(
    override var timeInMillis: Long
) : BaseItemMessage() {
    companion object {
        /*      @JvmStatic
              fun map(baseMessage: BaseMessageNew): TechMessageItem {
                  val text = baseMessage.textByType
                  return TechMessageItem(text = text, id = baseMessage.id,
                      dateTime = getMessageDateTime(baseMessage),
                      timeInMillis = baseMessage.created_utc_timestamp)
              }*/
    }


    override fun getType(): MessageType {
        return MessageType.Base
    }

    override fun accept(comment: String?) {

    }

    override fun cancel() {

    }

    override fun getText(): String {
        return ""
    }

    override fun getTitle(): String {
        return ""
    }
}