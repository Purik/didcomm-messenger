package  com.socialsirius.messenger.ui.chats.chat.message


import java.text.SimpleDateFormat
import java.util.Calendar

open class IChatItem() {

    open fun getMessageId(): String{
        return ""
    }

   open var timeInMillis: Long = 0

    companion object {
/*
        fun getMessageDateTime(messageRef: BaseMessageNew): String {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = messageRef.created_utc_timestamp.toLong()
            return SimpleDateFormat(DateUtils.PATTERN_HHmm).format(calendar.time)
        }*/
    }
}