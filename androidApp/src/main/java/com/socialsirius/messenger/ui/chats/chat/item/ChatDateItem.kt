package  com.socialsirius.messenger.ui.chats.chat.item


import com.socialsirius.messenger.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.Calendar

class ChatDateItem constructor(
    override val timeInMillis: Double
) : IChatItem {

    override fun getMessageId(): String {
        return "date"
    }

    val date: String
        get() {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timeInMillis.toLong()
            return SimpleDateFormat(DateUtils.PATTERN_ddMMMMyyyy).format(calendar.time)
        }
}