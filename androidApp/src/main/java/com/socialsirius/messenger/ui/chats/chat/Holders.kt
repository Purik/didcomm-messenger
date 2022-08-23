package com.socialsirius.messenger.ui.chats.chat

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.ui.OnAdapterItemClick
import com.socialsirius.messenger.base.ui.OnCustomBtnClick
import com.socialsirius.messenger.design.chat.AudioMessageView
import com.socialsirius.messenger.models.ChatMessageStatus
import com.socialsirius.messenger.ui.chats.chat.item.ChatMessageItem
import com.socialsirius.messenger.ui.chats.chat.message.*
import com.socialsirius.messenger.ui.connections.connectionCard.BaseViewHolder
import com.socialsirius.messenger.ui.connections.connectionCard.ConnectionCardFragment
import com.socialsirius.messenger.utils.DateUtils
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_chat_connection.view.*
import kotlinx.android.synthetic.main.item_chat_message.view.*
import kotlinx.android.synthetic.main.item_chat_message_date.view.*
import kotlinx.android.synthetic.main.item_chat_message_tech.view.*
import kotlinx.android.synthetic.main.item_chat_secret_invite.view.*

abstract class BaseMessagesViewHolder<out T : BaseItemMessage>(itemView: View) : BaseViewHolder<@UnsafeVariance T>(itemView),
    LayoutContainer {
}


class TechMessagesViewHolder(itemView: View) : BaseMessagesViewHolder<TechMessageItem>(itemView),
    LayoutContainer {


    override fun bind(item: TechMessageItem, position: Int) {

        itemView.techTextView.text = item.getText()
        itemView.timeTechTextView.text = DateUtils.parseDateToHhmmString(item.date)
    }

}

class DateMessagesViewHolder(itemView: View) : BaseMessagesViewHolder<ChatDateItem>(itemView),
    LayoutContainer {
    override fun bind(item: ChatDateItem, position: Int) {
        itemView.dateTextView.text = item.getText()
    }

}

class SecretInviteViewHolder(itemView: View) : BaseMessagesViewHolder<ConnectItemMessage>(itemView),
    LayoutContainer {

    override fun bind(message: ConnectItemMessage, position: Int) {
        itemView.infoTextView.text = message.getText()
    }

}


class MessagesViewHolder(itemView: View) : BaseMessagesViewHolder<TextItemMessage>(itemView),
    LayoutContainer {

    override fun bind(item: TextItemMessage, position: Int) {
        itemView.chatMessageView.isMine = item.isMine
        itemView.chatMessageView.setMessage(item.getText(), item)
        val isShowCorner = true
        itemView.chatMessageView.showCorner = isShowCorner
        itemView.chatMessageView.setStatus(item.status ?: ChatMessageStatus.default)
        // itemView.chatMessageView.updateOwnerAndCorners()
        itemView.chatMessageView.setDateTime(DateUtils.parseDateToHhmmString(item.date))


        if (item.canBeRead()) {
            //отправляем статус что прочитано
            onCustomBtnClick?.onBtnClick(MessagesAdapter.readActionId, item, position)
            //  messageReadAction.invoke(message.id, ConnectionsWrapper.ConnectionType.defaultType)
        }


        // itemView.chatMessageView.setMessageType(message.messageType)
        //   val nextItem = getItem(adapterPosition + 1) as? TextItemMessage
        //  val prevItem = getItem(adapterPosition - 1) as? TextItemMessage
        /*   val user = message.user ?: RosterUser()

           itemView.chatMessageView.isMine = message.isMine
           itemView.chatMessageView.isGray = message.messExpire
           val isShowCorner =  message.needToShowCorner(prevItem, nextItem)
           itemView.chatMessageView.showCorner = isShowCorner
           itemView.chatMessageView.setStatus(message.status)
           val showAvatar = message.needToShowAvatar()
           itemView.chatMessageView.showAvatarAndName(showAvatar)
           if (showAvatar) {
               itemView.chatMessageView.avatarImageView.update(user)
           }
           itemView.chatMessageView.setDateTime(message.dateTime)
           itemView.chatMessageView.setMessageType(message.messageType)
           itemView.chatMessageView.setMessage(message.text, message, updateMetadataAction = {
               recycler.post {
                   notifyItemChanged(adapterPosition,message)
               }
           })
           itemView.chatMessageView.setComment(message.comment)
           itemView.chatMessageView.setMessageExpireTimeText(message.expireTextTitle,message.expireTimeText)
           itemView.chatMessageView.setHtml(message.html)
           itemView.chatMessageView.setName(user.contactNameFull)
           var isShowDownload = false
           if (message.messageType == ContentType.audio) {
               getOrCreateAudioView(message).let {
                   itemView.chatMessageView.addAudioView(it)
                   val filePath = messagesUseCase.getDownloadedFilePathFromUrl(message.url)
                   if(message.downloadPercent == 100 || message.isMine){
                       it.updateAudioUrl(filePath)
                   }
               }
           } else {
              val downloadedPath =  messagesUseCase.getDownloadedFilePathFromUrl(message.url)
              val downloadedPathPreview =  messagesUseCase.getDownloadedFilePathFromUrl(message.url)
               if(message.downloadPercent == 0 && !message.isMine){
                   isShowDownload = true
               }
               itemView.chatMessageView.setUrlAndPreviewUrl(downloadedPath,downloadedPathPreview,message.text)
           }
           updateProgress(message,  isShowDownload)
           itemView.chatMessageView.imageVideoView.cancelBtn.setOnClickListener(View.OnClickListener {
               messageCancelUploadClickAction.invoke(message)
           })
           if (message.canBeRead()) {
               //отправляем статус что прочитано
               messageReadAction.invoke(message.id, ConnectionsWrapper.ConnectionType.defaultType)
           }

           itemView.chatMessageView.getImageContainer().setOnClickListener {
               imageClickAction.invoke(message)
           }

           itemView.chatMessageView.getFileContainer().setOnClickListener {
               documentClickAction.invoke(message)
           }

         */
        itemView.chatMessageView.getMessageContainer().setOnClickListener {
            onCustomBtnClick?.onBtnClick(it.id, item, absoluteAdapterPosition)
        }
        itemView.chatMessageView.getMessageContainer().setOnLongClickListener {
            onCustomBtnClick?.onLongBtnClick(it.id, item, absoluteAdapterPosition)

            true
        }
    }

    fun updateCornerAndStatus(message: ChatMessageItem) {
        /* val nextItem = getItem(adapterPosition + 1) as? ChatMessageItem
         val prevItem = getItem(adapterPosition - 1) as? ChatMessageItem
         itemView.chatMessageView.showCorner = message.needToShowCorner(prevItem, nextItem)
         itemView.chatMessageView.setStatus(message.status)*/
    }

    fun updateProgress(message: ChatMessageItem, isShowDownload: Boolean = false) {
        //    itemView.chatMessageView.updateProgress(message, audioMessages[message.id],isShowDownload)
    }

    fun updateActivityStatus(isOnline: Boolean) {
        //   itemView.chatMessageView.updateActivityStatus(isOnline)
    }

    private fun getOrCreateAudioView(message: ChatMessageItem): AudioMessageView? {
        return null
        //   return audioMessages[""]!!
        /*        if (!audioMessages.containsKey(message.id)) {
                    val audioMessageView = itemView.chatMessageView.createAudioView(message.id,
                            this@MessagesAdapter::stopAllMediaPlayers)

                    if (audioMessages.size >= MAX_AUDIO_CACHE_SIZE) {
                        val notPlayingView = audioMessages.filter { !it.value.isPlaying() }.entries.firstOrNull()
                        notPlayingView?.let {
                            it.value.releasePlayer()
                            audioMessages.remove(it.key)
                        }
                    }
                    audioMessages[message.id] = audioMessageView!!
                }
                return audioMessages[message.id]!!*/
    }
}


/*
    inner class ChatConnectionViewHolder(itemView: View) : ViewHolder(itemView), LayoutContainer {

        override val containerView: View?
            get() = itemView

        fun bind(message: ChatConnectionItem) {
            itemView.connectionTitleView.text = message.credName

            itemView.connectionTextView.text = message.comment
            itemView.timeTextView.text = message.dateTime
            itemView.openConnectionButton.setOnClickListener { connectionClickAction.invoke(message) }

            if (message.type == ConnectionsWrapper.ConnectionType.proofrequest) {
                itemView.connectionMessage.backgroundTintList = App.getContext().resources.getColorStateList(R.color.cardConnectionCredentials)
                itemView.connectionIcon.setImageDrawable(App.getContext().resources.getDrawable(R.drawable.ic_connection_verification))
                itemView.connectionIcon.backgroundTintList = App.getContext().resources.getColorStateList(R.color.cardConnectionCredentials)
            } else if (message.type == ConnectionsWrapper.ConnectionType.credentilas) {
                itemView.connectionMessage.backgroundTintList = App.getContext().resources.getColorStateList(R.color.cardOrange)
                itemView.connectionIcon.setImageDrawable(App.getContext().resources.getDrawable(R.drawable.ic_connection_credentials2))
                itemView.connectionIcon.backgroundTintList = App.getContext().resources.getColorStateList(R.color.cardOrange)
            }else if(message.type == ConnectionsWrapper.ConnectionType.workspaces){
                itemView.connectionMessage.backgroundTintList = App.getContext().resources.getColorStateList(R.color.cardConnectionGreen)
                itemView.connectionIcon.setImageDrawable(App.getContext().resources.getDrawable(R.drawable.ic_connection_chat))
                itemView.connectionIcon.backgroundTintList = App.getContext().resources.getColorStateList(R.color.cardConnectionGreen)
            }else if(message.type == ConnectionsWrapper.ConnectionType.orders){
                itemView.connectionMessage.backgroundTintList = App.getContext().resources.getColorStateList(R.color.cardBlue)
                itemView.connectionIcon.setImageDrawable(App.getContext().resources.getDrawable(R.drawable.ic_connection_credentials2))
                itemView.connectionIcon.backgroundTintList = App.getContext().resources.getColorStateList(R.color.cardBlue)
            } else if(message.type == ConnectionsWrapper.ConnectionType.proposeCredentials){
                itemView.connectionMessage.backgroundTintList = App.getContext().resources.getColorStateList(R.color.cardViolet)
                itemView.connectionIcon.setImageDrawable(App.getContext().resources.getDrawable(R.drawable.send_credentials_mini))
                itemView.connectionIcon.backgroundTintList = App.getContext().resources.getColorStateList(R.color.cardViolet)
            }
            //Для proofrequest комментарий при отказе
            *//*credStatus.setText(R.string.proof_request_error);
            String commnet = "";
            if (!TextUtils.isEmpty(messages.getCommentCode())) {
                commnet = messages.getCommentCode();
            }
            if (!TextUtils.isEmpty(commnet) && !TextUtils.isEmpty(messages.getComment())) {
                commnet = commnet + " : " + messages.getComment();
            }
            credName.setText(commnet);
            *//*

            Log.d("mylog2080", "message.canBeRead()=" + message.canBeRead() + " message=" + message)
            if (message.canBeRead()) {
                //отправляем статус что прочитано
                messageReadAction.invoke(message.wrapper.id, message.wrapper.type)
            }

            var status = ""
            if (message.status == ConnectionsWrapper.ConnectionStatus.not_accepted) {

                if (message.type == ConnectionsWrapper.ConnectionType.proofrequest) {
                    status = App.getContext().getString(R.string.proof_request_error)
                } else {
                    status = App.getContext().getString(R.string.accepted_not)
                }
            } else if (message.status == ConnectionsWrapper.ConnectionStatus.accepted) {

                if (message.type == ConnectionsWrapper.ConnectionType.proofrequest) {
                    status = App.getContext().getString(R.string.proof_request_success)
                } else {
                    status = App.getContext().getString(R.string.accepted)
                }
            } else if (message.status == ConnectionsWrapper.ConnectionStatus.canceled) {

                if (message.type == ConnectionsWrapper.ConnectionType.proofrequest) {
                    status = App.getContext().getString(R.string.proof_request_error)
                } else {
                    status = App.getContext().getString(R.string.canceled)
                }
                itemView.connectionMessage.backgroundTintList = App.getContext().resources.getColorStateList(R.color.errorColor)
                itemView.connectionIcon.backgroundTintList = App.getContext().resources.getColorStateList(R.color.errorColor)
            }
            itemView.connectionStatusView.text = status
            //todo comment uncomment
            if (message.isTimeOut()) {
                itemView.openConnectionButton.visibility = View.GONE
                itemView.connectionMessage.backgroundTintList = App.getContext().resources.getColorStateList(R.color.cardBlackTransparent)
                itemView.connectionIcon.backgroundTintList = App.getContext().resources.getColorStateList(R.color.transparent)
            } else {
                itemView.openConnectionButton.visibility = View.VISIBLE
            }
        }
    }*/


class HolderViewHolder(itemView: View) : BaseMessagesViewHolder<OfferItemMessage>(itemView),
    LayoutContainer {


    override fun bind(item: OfferItemMessage, position: Int) {
     //   itemView.infoTextView.text = item.getText()
        itemView.connectionTitleView.text = item.getTitle()

        itemView.connectionTextView.text = item.getText()
        itemView.timeTextView.text = DateUtils.parseDateToHhmmString(item.date)
        itemView.openConnectionButton.setOnClickListener { onAdapterItemClick?.onItemClick(item) }
        itemView.connectionStatusView.text = item.getStatusString()
        itemView.connectionMessage.backgroundTintList = App.getContext().resources.getColorStateList(
            R.color.cardOrange)
        itemView.connectionIcon.setImageDrawable(App.getContext().resources.getDrawable(R.drawable.ic_connection_credentials2))
        itemView.connectionIcon.backgroundTintList = App.getContext().resources.getColorStateList(R.color.cardOrange)

        if (item.canBeRead()){
            onCustomBtnClick?.onBtnClick(MessagesAdapter.localReadActionId, item, position)
        }
    }

}

class ProverViewHolder(itemView: View) : BaseMessagesViewHolder<ProverItemMessage>(itemView),
    LayoutContainer {


    override fun bind(item: ProverItemMessage, position: Int) {
        itemView.connectionTitleView.text = item.getTitle()

        itemView.connectionTextView.text = item.getText()
        itemView.timeTextView.text = DateUtils.parseDateToHhmmString(item.date)
        itemView.openConnectionButton.setOnClickListener { onAdapterItemClick?.onItemClick(item) }
        itemView.connectionStatusView.text = item.getStatusString()
        itemView.connectionMessage.backgroundTintList = App.getContext().resources.getColorStateList(R.color.cardConnectionCredentials)
        itemView.connectionIcon.setImageDrawable(App.getContext().resources.getDrawable(R.drawable.ic_connection_verification))
        itemView.connectionIcon.backgroundTintList = App.getContext().resources.getColorStateList(R.color.cardConnectionCredentials)
        if (item.canBeRead()){
            onCustomBtnClick?.onBtnClick(MessagesAdapter.localReadActionId, item, position)
        }
    }

}