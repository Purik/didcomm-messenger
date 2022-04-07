package  com.socialsirius.messenger.ui.chats.chat.item


import java.text.SimpleDateFormat
import java.util.Calendar

interface IChatItem {

    fun getMessageId(): String

    val timeInMillis: Double

    companion object {
/*
        fun getMessageDateTime(messageRef: BaseMessageNew): String {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = messageRef.created_utc_timestamp.toLong()
            return SimpleDateFormat(DateUtils.PATTERN_HHmm).format(calendar.time)
        }*/
    }
}