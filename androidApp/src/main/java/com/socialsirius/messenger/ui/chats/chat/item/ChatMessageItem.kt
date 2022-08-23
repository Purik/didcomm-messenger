package  com.socialsirius.messenger.ui.chats.chat.item

import android.R.id.message
import android.util.Log
import com.google.gson.Gson
import com.socialsirius.messenger.models.ChatMessageStatus
import com.socialsirius.messenger.ui.chats.chat.message.IChatItem

@Deprecated("")
class ChatMessageItem private constructor(
       // private val baseMessageRef: BaseMessageNew,
     //   val user: RosterUser?,
        val isGroupMessage: Boolean
) : IChatItem() {

    companion object {
       // @JvmStatic
      /*  fun map(baseMessage: BaseMessageNew, chat: Chats? = null): ChatMessageItem {
            var user: RosterUser? = chat?.getUserFromMembers(baseMessage.msg_from)
            return ChatMessageItem(
                    baseMessageRef = baseMessage,
                    user = user,
                    isGroupMessage = chat?.isRoom ?: false
            )
        }*/
    }
    override var timeInMillis: Long = 0
/*
    val id = baseMessageRef.id.orEmpty()
    val isMine = baseMessageRef.messageUserType == BaseMessageNew.MessageUserType.OutComing
    val text = getTextByType()
    val comment = getCommentByType()
    val expireTimeText = getExpireTime()
    val expireTextTitle = getExpireText()
    val messExpire = isMessExpire() && !baseMessageRef.accepted
    val html = baseMessageRef.html.orEmpty()
    val url = baseMessageRef.url.orEmpty()
    val isCanceled = baseMessageRef.isCanceled
    val previewUrl = baseMessageRef.preview_url.orEmpty()
    val messageType = baseMessageRef.contentType ?: ContentType.text
    val dateTime = getMessageDateTime(baseMessageRef)
    override val timeInMillis = baseMessageRef.created_utc_timestamp
    var urlMetadata: MetaData? = null

    val userIdFrom = baseMessageRef.msg_from
    val userIdTo = baseMessageRef.msg_to

    val uploadPercent = baseMessageRef.uploadPercent
    val downloadPercent = baseMessageRef.downloadPercent

*/
val status: ChatMessageStatus = ChatMessageStatus.acknowlege
    fun getTextByType(): String {
       // var text = baseMessageRef.text.orEmpty()
    /*    if (baseMessageRef.contentType == ContentType.services) {
            if (!baseMessageRef.json.isNullOrEmpty()) {
                val gson = Gson()
                val questionMessage = gson.fromJson(baseMessageRef.json, QuestionMessage::class.java)
                text = questionMessage.question_text.orEmpty()
            }
        } else if (baseMessageRef.contentType == ContentType.answer) {
            if (!baseMessageRef.json.isNullOrEmpty()) {
                val gson = Gson()
                val answerMessage = gson.fromJson(baseMessageRef.json, AnswerMessage::class.java)
                text = answerMessage.response.orEmpty()
            }
        }*/
     //   return text
        return ""
    }

    fun getCommentByType(): String? {
        var text: String? = null
      /*  if (baseMessageRef.contentType == ContentType.services) {
            if (!baseMessageRef.json.isNullOrEmpty()) {
                val gson = Gson()
                val questionMessage = gson.fromJson(baseMessageRef.json, QuestionMessage::class.java)
                text = questionMessage.question_detail
            }
        }*/
        return text
    }

    fun getExpireTime(): String? {
        var text: String? = null
   /*     if (baseMessageRef.contentType == ContentType.services) {
            if (!baseMessageRef.json.isNullOrEmpty()) {
                val gson = Gson()
                val questionMessage = gson.fromJson(baseMessageRef.json, QuestionMessage::class.java)
                if (questionMessage.timing?.isDateExpiries == false && !baseMessageRef.accepted) {
                    text = questionMessage.timing?.getStringFromDate(DateUtils.PATTERN_ddMMyyyyHHmm)
                }
            }
        }*/
        return text
    }

    fun isMessExpire(): Boolean {

    /*    if (baseMessageRef.contentType == ContentType.services) {
            if (!baseMessageRef.json.isNullOrEmpty()) {
                val gson = Gson()
                val questionMessage = gson.fromJson(baseMessageRef.json, QuestionMessage::class.java)
                return questionMessage.timing?.isDateExpiries == true
            }
        }*/
        return false
    }


    fun getExpireText(): String? {
        var text: String? = null
      /*  if (baseMessageRef.contentType == ContentType.services) {
            if (!baseMessageRef.json.isNullOrEmpty()) {
                val gson = Gson()
                val questionMessage = gson.fromJson(baseMessageRef.json, QuestionMessage::class.java)
                if (questionMessage.timing?.isDateExpiries == true) {
                    text = App.getContext().getString(R.string.question_answer_time_expired)
                } else {
                    text = App.getContext().getString(R.string.question_answer_time_expire)
                }
                if (baseMessageRef.accepted) {
                    text = null
                }
            }
        }*/
        return text
    }

/*    fun isDownloaded(): Boolean {
        return downloadPercent >= 100
    }

    fun isUploaded(): Boolean {
        return uploadPercent >= 100
    }

    fun getUploadedPercent(): Int {
        if (isMine) {
            return uploadPercent
        } else {
            return 100
        }
    }

    fun needToShowAvatar(): Boolean {
        return isGroupMessage && !isMine
    }*/

 /*   fun canBeRead(): Boolean {
        val canBeRead =  !isMine && baseMessageRef.status != ChatMessageStatus.acknowlege &&
                baseMessageRef.status != ChatMessageStatus.error
        Log.d("mylog209000","baseMessageRef.status ="+baseMessageRef.status +" isMine="+isMine+" text="+text)
        return  canBeRead
    }*/

   /* fun needToShowCorner(prevItem: ChatMessageItem?, nextItem: ChatMessageItem?): Boolean {
        //TODO если отличается статусы надо разделить
        return when {
            nextItem == null -> true
            //сверху свое, снизу свое
            nextItem?.userIdFrom == userIdFrom && prevItem?.userIdFrom == userIdFrom -> needToShowCornerForMyMessage(nextItem)
            //сверху свое, снизу не свое
            nextItem?.userIdFrom != userIdFrom && prevItem?.userIdFrom == userIdFrom -> true
            //сверху не свое, снизу свое
            nextItem?.userIdFrom == userIdFrom && prevItem?.userIdFrom != userIdFrom -> needToShowCornerForMyMessage(nextItem)
            //сверху не свое, снизу свое
            else -> true
        }
    }*/

    //снизу - свое, но нужно проверить статусы
/*    private fun needToShowCornerForMyMessage(nextItem: ChatMessageItem?): Boolean {
        return nextItem?.let {
            when {
                it.status != this.status -> true
                (it.timeInMillis - this.timeInMillis) >= 1000 * 60 * 2 -> true //2 минуты
                else -> false
            }
        } ?: false
    }*/

    override fun getMessageId(): String {
     //   return id
        return ""
    }

    override fun toString(): String {
        return ""
     //   return "{text=" + text + "}"
    }
}