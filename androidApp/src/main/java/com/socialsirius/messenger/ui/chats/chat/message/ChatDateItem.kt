package  com.socialsirius.messenger.ui.chats.chat.message



import com.socialsirius.messenger.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.Calendar

class ChatDateItem constructor(
    override var timeInMillis: Long
) : BaseItemMessage() {

    override fun getMessageId(): String {
        return "date"
    }

    override fun getType(): MessageType {
       return  MessageType.Base
    }

    override fun accept(comment: String?) {

    }

    override fun cancel() {

    }

    override fun getText(): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis.toLong()
        return SimpleDateFormat(DateUtils.PATTERN_ddMMMMyyyy).format(calendar.time)
    }

    override fun getTitle(): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis.toLong()
        return SimpleDateFormat(DateUtils.PATTERN_ddMMMMyyyy).format(calendar.time)
    }
}