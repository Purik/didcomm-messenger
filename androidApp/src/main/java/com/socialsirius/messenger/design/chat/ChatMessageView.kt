package com.socialsirius.messenger.design.chat

import android.content.Context
import android.text.util.Linkify
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.Gravity.END
import android.view.Gravity.START
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.webkit.WebView
import android.widget.*
import androidx.core.content.ContextCompat
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.design.AvatarView
import com.socialsirius.messenger.models.ChatMessageStatus
import com.socialsirius.messenger.ui.chats.chat.item.ChatMessageItem
import com.socialsirius.messenger.ui.chats.chat.message.TextItemMessage


import java.io.File


class ChatMessageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

   // private var messageType: ContentType? = null

    /**
     * To updateCorners call updateOwnerAndCorners() after set, or  showCorner.setValue
     */
    var isMine: Boolean = true
        set(value) {
            field = value
        }

    var isGray: Boolean = false
    var showCorner: Boolean = true
        set(value) {
            field = value
            updateOwnerAndCorners()
        }

    var mainPanelView: RelativeLayout
    var messageStatusView: LinearLayout
    var rightSpace: Space
    var leftSpace: Space
  //  var messageExpireTitleTextView: TextView
 //   var messageCommentTextView: TextView
    var messageTextView: TextView
    var messageStatusImageView: ImageView
    var mainView: LinearLayout
    var typingImageView: ImageView
    var userNameTextView: TextView
    var avatarImageView: AvatarView
    var avatarSpace: Space
   // var linkPreviewView: LinkPreviewView
   // var messageExpireLayout: LinearLayout
    var htmlView: WebView
   // var messageExpireTimeTextView: TextView
    var messageStatus2TextView: TextView
   // var imageVideoView: VideoImageView
  //  var audioView: FrameLayout
 //   var fileView: DocumentMessageView


    init {
        val view = View.inflate(context, R.layout.view_message_chat, this)
        mainPanelView = view.findViewById(R.id.mainPanelView)
        messageStatusView = view.findViewById(R.id.messageStatusView)
        leftSpace = view.findViewById(R.id.leftSpace)
        rightSpace = view.findViewById(R.id.rightSpace)
     //   messageExpireTitleTextView = view.findViewById(R.id.messageExpireTitleTextView)
        messageTextView = view.findViewById(R.id.messageTextView)
     //   messageCommentTextView = view.findViewById(R.id.messageCommentTextView)
        messageStatusImageView = view.findViewById(R.id.messageStatusImageView)
        mainView = view.findViewById(R.id.mainView)
        typingImageView = view.findViewById(R.id.typingImageView)
        userNameTextView = view.findViewById(R.id.userNameTextView)
        avatarImageView = view.findViewById(R.id.avatarImageView)
        avatarSpace = view.findViewById(R.id.avatarSpace)
   //     linkPreviewView = view.findViewById(R.id.linkPreviewView)
    //    messageExpireLayout = view.findViewById(R.id.messageExpireLayout)
        htmlView = view.findViewById(R.id.htmlView)
    //    messageExpireTimeTextView = view.findViewById(R.id.messageExpireTimeTextView)
        messageStatus2TextView = view.findViewById(R.id.messageStatus2TextView)
      //  imageVideoView = view.findViewById(R.id.imageVideoView)
     //   audioView = view.findViewById(R.id.audioView)
    //    fileView = view.findViewById(R.id.fileView)
    }

    private fun updateCorners() {
        when {
            isGray && showCorner && isMine -> {
                mainPanelView.background = ContextCompat.getDrawable(this.context, R.drawable.bg_message_rounded_right_corner_gray)
                messageStatusView.gravity = Gravity.END
                messageStatusView.visibility = View.VISIBLE

            }
            isGray && !showCorner && isMine -> {
                mainPanelView.background = ContextCompat.getDrawable(this.context, R.drawable.bg_message_rounded_right_gray)
                messageStatusView.visibility = View.GONE
            }
            isGray && showCorner && !isMine -> {
                mainPanelView.background = ContextCompat.getDrawable(this.context, R.drawable.bg_message_rounded_left_corner_gray)
                messageStatusView.gravity = Gravity.START
                messageStatusView.visibility = View.VISIBLE
            }
            isGray && !showCorner && !isMine -> {
                mainPanelView.background = ContextCompat.getDrawable(this.context, R.drawable.bg_message_rounded_left_gray)
                messageStatusView.visibility = View.GONE
            }

            isMine && showCorner -> {
                mainPanelView.background = ContextCompat.getDrawable(this.context, R.drawable.bg_message_rounded_right_corner)
                messageStatusView.gravity = Gravity.END
                messageStatusView.visibility = View.VISIBLE
                messageStatus2TextView.setTextColor(App.getContext().resources.getColor(R.color.backgroundColorWithAlpha))
            }
            isMine && !showCorner -> {
                mainPanelView.background = ContextCompat.getDrawable(this.context, R.drawable.bg_message_rounded_right)
                messageStatusView.visibility = View.GONE
                messageStatus2TextView.setTextColor(App.getContext().resources.getColor(R.color.backgroundColorWithAlpha))
            }
            !isMine && showCorner -> {
                mainPanelView.background = ContextCompat.getDrawable(this.context, R.drawable.bg_message_rounded_left_corner)
                messageStatusView.gravity = Gravity.START
                messageStatusView.visibility = View.VISIBLE
            }
            !isMine && !showCorner -> {
                mainPanelView.background = ContextCompat.getDrawable(this.context, R.drawable.bg_message_rounded_left)
                messageStatusView.visibility = View.GONE
            }
        }
    }

    fun updateOwnerAndCorners() {
        updateCorners()
        rightSpace.visibility = View.GONE
        leftSpace.visibility = View.GONE
        when {
            isMine -> {
              /*  if (isGray) {
                    messageExpireTitleTextView.setTextColor(ContextCompat.getColor(this.context, R.color.backgroundColor))
                    messageCommentTextView.setTextColor(ContextCompat.getColor(this.context, R.color.backgroundColor))
                } else {
                    messageExpireTitleTextView.setTextColor(ContextCompat.getColor(this.context, R.color.hintColor))
                    messageCommentTextView.setTextColor(ContextCompat.getColor(this.context, R.color.hintColor))
                }*/
                messageTextView.setTextColor(ContextCompat.getColor(this.context, R.color.backgroundColor))
               // messageStatusImageView.visibility = View.VISIBLE
                rightSpace.visibility = View.GONE
                leftSpace.visibility = View.VISIBLE
                mainView.setHorizontalGravity(END)
            }
            else -> {
               /* if (isGray) {
                    messageExpireTitleTextView.setTextColor(ContextCompat.getColor(this.context, R.color.backgroundColor))
                    messageCommentTextView.setTextColor(ContextCompat.getColor(this.context, R.color.backgroundColor))
                } else {
                    messageExpireTitleTextView.setTextColor(ContextCompat.getColor(this.context, R.color.hintColor))
                    messageCommentTextView.setTextColor(ContextCompat.getColor(this.context, R.color.hintColor))
                }*/
                messageTextView.setTextColor(ContextCompat.getColor(this.context, R.color.defaultColor))
                messageStatusImageView.visibility = View.GONE
                rightSpace.visibility = View.VISIBLE
                leftSpace.visibility = View.GONE
                mainView.setHorizontalGravity(START)
            }
        }
    }

    fun setTypingStatus(isTyping: Boolean) {
        typingImageView.visibility = if (isTyping) View.VISIBLE else View.GONE
    }

    fun setName(name: String?) {
        userNameTextView.text = name
    }

    fun showAvatarAndName(show: Boolean) {
        avatarImageView.visibility = if (show) View.VISIBLE else View.GONE
        avatarSpace.visibility = if (show) View.VISIBLE else View.GONE
        userNameTextView.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun updateActivityStatus(isOnline: Boolean) {
        avatarImageView.updateStatus(isOnline)
    }

   fun setMessage(message: String?, parent: TextItemMessage) {
    //   if (parent.messageType == ContentType.text || parent.messageType == ContentType.services || parent.messageType == ContentType.answer) {
            messageTextView.text = message
            messageTextView.setLinkTextColor(ContextCompat.getColor(this.context,
                    if (isMine) R.color.backgroundColor else R.color.defaultColor))
            Linkify.addLinks(messageTextView, Linkify.ALL)
            mainPanelView.layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)

    /*        val firstUrl = parent.urlMetadata?.url
                    ?: messageTextView.urls?.firstOrNull { it.url.startsWith("http") }?.url.orEmpty()
            Log.d("mylog29999", "message=" + message + " firstUrl=" + firstUrl)*/
            //linkPreviewView.setIsOnDarkBackground(isMine)
            /*if(firstUrl.isEmpty()){
                linkPreviewView.updateWithMetadata(null)
            }else if (parent.urlMetadata != null) {
                linkPreviewView.updateWithMetadata(parent.urlMetadata)
            } else {
                linkPreviewView.updateLink(firstUrl) {
                    parent.urlMetadata = it
                    if(it!=null){
                        updateMetadataAction(it)
                    }
                }
            }*/
      //  }
    }


    fun setComment(comment: String?) {
       /* messageCommentTextView.text = comment
        if (comment == null) {
            messageCommentTextView.visibility = View.GONE
        } else {
            messageCommentTextView.visibility = View.VISIBLE
        }*/
    }

    fun setMessageExpireTimeText(text: String?, time: String?) {
     /*   if (time == null && text == null) {
            messageExpireLayout.visibility = View.GONE
        } else {
            messageExpireTitleTextView.text = text
            messageExpireTimeTextView.text = time
            messageExpireLayout.visibility = View.VISIBLE
        }*/
    }

    fun setHtml(html: String?) {
        htmlView.settings.javaScriptEnabled = true
        htmlView.loadData(html ?:"", "text/html; charset=utf-8", "UTF-8")
    }

    fun setStatus(status: ChatMessageStatus) {
        if (isMine) {
            messageStatus2TextView.setTextColor(ContextCompat.getColor(this.context, when (status) {
                ChatMessageStatus.error -> R.color.errorColor
                else -> R.color.hintColor
            }))
            messageStatusImageView.setImageDrawable(ContextCompat.getDrawable(this.context, when (status) {
                ChatMessageStatus.sent -> R.color.transparent
                ChatMessageStatus.received -> R.drawable.ic_sent_not_read
                ChatMessageStatus.acknowlege -> R.drawable.ic_sent_and_read
                ChatMessageStatus.default -> R.drawable.ic_watch_later
                ChatMessageStatus.error -> R.drawable.ic_send_error
            }))
        } else {
            messageStatus2TextView.setTextColor(ContextCompat.getColor(this.context, R.color.hintColor))
        }
    }

    fun setDateTime(dateTime: String) {
        messageStatus2TextView.text = dateTime
    }

    fun createAudioView(messageId: String, onStartPlaying: (String) -> Unit): AudioMessageView? {
      /*  if (messageType != ContentType.audio)
            return null
*/
        val audioMessageView = AudioMessageView(this.context)
        audioMessageView.layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT)

        audioMessageView.setIsOnDarkBackground(isMine)
        audioMessageView.setStartPlayingCallback(messageId, onStartPlaying)
        return audioMessageView
    }

    fun addAudioView(audioMessageView: AudioMessageView) {
      /*  (audioMessageView.parent as? FrameLayout)?.removeAllViews()
        audioView.addView(audioMessageView)*/
    }

    fun setUrlAndPreviewUrl(url: String?, previewUrl: String?, text: String? = null) {
       /* when (messageType) {
            ContentType.image -> {
                imageVideoView.setUrlAndPreviewUrl(url, previewUrl, false)
            }
            ContentType.video -> {
                imageVideoView.setUrlAndPreviewUrl(url, previewUrl, true)
            }
            ContentType.doc -> {
                fileView.setDocument(url!!, text)
            }
            else -> {

            }
        }*/
    }

  //  fun getImageContainer() = imageVideoView
   // fun getFileContainer() = fileView
    fun getMessageContainer() = mainPanelView

  /*  fun setMessageType(messageType: ContentType?) {
        this.messageType = messageType

        htmlView.visibility = View.GONE
        messageTextView.visibility = View.GONE
        imageVideoView.visibility = View.GONE
        audioView.visibility = View.GONE
        fileView.visibility = View.GONE
        linkPreviewView.visibility = View.GONE
        audioView.removeAllViews()
        mainPanelView.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

        when (messageType) {
            ContentType.text -> {
                messageTextView.visibility = View.VISIBLE
            }
            ContentType.audio -> {
                audioView.visibility = View.VISIBLE
            }
            ContentType.image -> {
                imageVideoView.visibility = View.VISIBLE
            }
            ContentType.video -> {
                imageVideoView.visibility = View.VISIBLE
            }
            ContentType.doc -> {
                fileView.visibility = View.VISIBLE
                fileView.setIsOnDarkBackground(isMine)
                //     mainPanelView.layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            }
            ContentType.html -> {
                htmlView.visibility = View.VISIBLE
            }
            ContentType.secretChatRequest,
            ContentType.secretChatCredential,
            ContentType.secretChatPresentProof -> messageTextView.visibility = View.VISIBLE
            else -> {
                messageTextView.visibility = View.VISIBLE
            }
        }
    }*/

    fun updateProgress(message: ChatMessageItem, audioMessageView: AudioMessageView?, isShowDownload: Boolean) {
      /*  var isDownload = false
        var percent = when {
            message.uploadPercent in 1..100 -> {
                isDownload = false
                message.uploadPercent
            }
            message.downloadPercent in 1..100 -> {
                isDownload = true
                message.downloadPercent
            }

            else -> {
                101
            }
        }*/

      /*  if (isShowDownload) {
            percent = 0
            isDownload = true
        }*/
      //  when (message.messageType) {
         /*   ContentType.audio -> audioMessageView?.updateProgress(percent, isDownload)
            ContentType.image -> {
                imageVideoView?.updateProgress(percent, isDownload, message.isCanceled)
            }
            ContentType.video -> {
                imageVideoView?.updateProgress(percent, isDownload, message.isCanceled)
            }
            ContentType.doc -> fileView.updateProgress(percent, isDownload)
            else -> {
            }*/
            /*   ContentType.html -> { }
               ContentType.text,
               ContentType.secretChatRequest,
               ContentType.secretChatCredential,
               ContentType.secretChatPresentProof -> { }
               ContentType.service -> {}
               ContentType.services -> {}
               ContentType.indytransport -> {}
               ContentType.indytransportchunked -> { }
               ContentType.answer -> { }*/
     //   }
    }

}