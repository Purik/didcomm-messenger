package  com.socialsirius.messenger.ui.chats.chat

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.ui.BaseRecyclerViewAdapter
import com.socialsirius.messenger.design.chat.AudioMessageView
import com.socialsirius.messenger.ui.chats.chat.item.*
import com.socialsirius.messenger.ui.chats.chats.message.ConnectItemMessage
import com.socialsirius.messenger.ui.chats.chats.message.TextItemMessage
import com.socialsirius.messenger.utils.DateUtils

import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_chat_message.view.*

import kotlinx.android.synthetic.main.item_chat_message_date.view.*
import kotlinx.android.synthetic.main.item_chat_message_tech.view.*
import kotlinx.android.synthetic.main.item_chat_secret_invite.view.*

private const val CHAT_MESSAGE_ITEM = 1
private const val TECH_MESSAGE_ITEM = 2
private const val DATE_MESSAGE_ITEM = 3
private const val SECRET_INVITE_MESSAGE_ITEM = 4
private const val CONNECTION_MESSAGE_ITEM = 5
private const val MAX_AUDIO_CACHE_SIZE = 200

class MessagesAdapter () : BaseRecyclerViewAdapter<IChatItem,MessagesAdapter.BaseMessagesViewHolder>() {

    val audioMessages = mutableMapOf<String, AudioMessageView>()

    fun setItems() {
        releaseAllMediaPlayers()
        notifyDataSetChanged()
    }

    fun addRange(index: Int, size: Int) {
        //Какие то проблемы с TECHMESSAGEHOLDER
        //   notifyItemRangeInserted(index, size)
        notifyDataSetChanged()

    }

    fun update(indexes: List<Int>) {
        //Какие то проблемы с TECHMESSAGEHOLDER
        //   indexes.forEach { notifyItemChanged(it, CornerPayload()) }
     /*   indexes.forEach {
            val items = dataList?[it]
            Log.d("mylog209000","items="+items.getMessageId())
        }

        notifyDataSetChanged()*/
    }

    fun updateActivityStatus(idStatusPair: Pair<String, Boolean>) {
    /*    getItems().toTypedArray().forEachIndexed { index, item ->
            if (item is ChatMessageItem
                    && item.userIdFrom == idStatusPair.first
                    && item.needToShowAvatar()) {
                notifyItemChanged(index, StatusPayload(idStatusPair.second))
            }
        }*/
    }

    /*fun getItem(index: Int): IChatItem? {
        return try {
            getItems()[index]
        } catch (e: Exception) {
            null
        }
    }
*/
    fun releaseAllMediaPlayers() {
        stopAllMediaPlayers("")
        audioMessages.forEach {
            it.value.releasePlayer()
        }
        audioMessages.clear()
    }

    fun stopAllMediaPlayers(callerId: String) {
        audioMessages.forEach {
            if (it.key != callerId)
                it.value.pausePlayer()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
           // is ChatMessageItem -> CHAT_MESSAGE_ITEM
            is TechMessageItem -> TECH_MESSAGE_ITEM
            is ChatDateItem -> DATE_MESSAGE_ITEM
         //   is SecretInviteItem -> SECRET_INVITE_MESSAGE_ITEM
            is TextItemMessage ->CHAT_MESSAGE_ITEM
            is ConnectItemMessage -> SECRET_INVITE_MESSAGE_ITEM
            //is ChatConnectionItem -> CONNECTION_MESSAGE_ITEM
            else -> 0
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseMessagesViewHolder {
        return when (viewType) {
            CHAT_MESSAGE_ITEM -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_message, parent, false)
                MessagesViewHolder(view)
            }

            SECRET_INVITE_MESSAGE_ITEM -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_secret_invite, parent, false)
                SecretInviteViewHolder(view)
            }
            DATE_MESSAGE_ITEM -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_message_date, parent, false)
                DateMessagesViewHolder(view)
            }
        /*    CONNECTION_MESSAGE_ITEM -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_connection, parent, false)
                ChatConnectionViewHolder(view)
            }*/
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_message_tech, parent, false)
                TechMessagesViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseMessagesViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is MessagesViewHolder -> {
                val item = item as TextItemMessage
            //    Log.d("mylog20031","onBindViewHolder item"+item.text +" item.status="+item.status)
                holder.bind(item)
            }
            is TechMessagesViewHolder -> holder.bind(item as TechMessageItem)
            is DateMessagesViewHolder -> holder.bind(item as ChatDateItem)
            is SecretInviteViewHolder -> holder.bind(item as ConnectItemMessage)
          //  is ChatConnectionViewHolder -> holder.bind(getItems()[position] as ChatConnectionItem)
        }
    }

  /*  override fun onBindViewHolder(holder: BaseMessagesViewHolder, position: Int, payloads: MutableList<Any>) {
        val item = getItem(position)
        when (holder) {
            is MessagesViewHolder -> {
                Log.d("mylog20031","onBindViewHolder Payloads ")
                val item = item as TextItemMessage
           //     Log.d("mylog2003","onBindViewHolder item"+item.text +" item.status="+item.status)
                holder.bind(item)
                when (val payload = payloads.firstOrNull()) {
                 *//*   is CornerPayload -> {
                        val items =getItems()[position] as ChatMessageItem
                        holder.updateCornerAndStatus(items)

                    }
                    is PercentPayload -> holder.updateProgress(getItems()[position] as ChatMessageItem)*//*
                    is StatusPayload -> holder.updateActivityStatus(payload.isOnline)
                    *//*else -> {
                        val item = getItems()[position] as ChatMessageItem
                        Log.d("mylog2003","onBindViewHolder item"+item.text +" item.status="+item.status)
                        holder.bind(item)
                    }*//*
                }
            }
            is TechMessagesViewHolder -> holder.bind(item as TechMessageItem)
            is DateMessagesViewHolder -> holder.bind(item as ChatDateItem)
            is SecretInviteViewHolder -> holder.bind(item as SecretInviteItem)
         //   is ChatConnectionViewHolder -> holder.bind(getItems()[position] as ChatConnectionItem)
        }
    }*/

    inner class MessagesViewHolder(itemView: View) : BaseMessagesViewHolder(itemView), LayoutContainer {

        override val containerView: View?
            get() = itemView

        fun bind(message: TextItemMessage) {
            itemView.chatMessageView.isMine = message.isMine
            itemView.chatMessageView.setMessage(message.getText(), message)
            val isShowCorner =  false
            itemView.chatMessageView.showCorner = isShowCorner
            itemView.chatMessageView.setDateTime(DateUtils.dateToHHmmss(message.date))


           // if (message.canBeRead()) {
                //отправляем статус что прочитано
         //       messageReadAction.invoke(message.id, ConnectionsWrapper.ConnectionType.defaultType)
            //}


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

            itemView.chatMessageView.getMessageContainer().setOnClickListener { shortClickAction.invoke(message) }
            itemView.chatMessageView.getMessageContainer().setOnLongClickListener {
                longClickAction.invoke(message)
                true
            }*/
        }

        fun updateCornerAndStatus(message: ChatMessageItem) {
           /* val nextItem = getItem(adapterPosition + 1) as? ChatMessageItem
            val prevItem = getItem(adapterPosition - 1) as? ChatMessageItem
            itemView.chatMessageView.showCorner = message.needToShowCorner(prevItem, nextItem)
            itemView.chatMessageView.setStatus(message.status)*/
        }

        fun updateProgress(message: ChatMessageItem, isShowDownload : Boolean = false) {
        //    itemView.chatMessageView.updateProgress(message, audioMessages[message.id],isShowDownload)
        }

        fun updateActivityStatus(isOnline: Boolean) {
         //   itemView.chatMessageView.updateActivityStatus(isOnline)
        }

        private fun getOrCreateAudioView(message: ChatMessageItem): AudioMessageView {
            return audioMessages[""]!!
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

    open inner class BaseMessagesViewHolder(itemView: View) : ViewHolder(itemView), LayoutContainer {
        override val containerView: View?
            get() = itemView
    }

    inner class TechMessagesViewHolder(itemView: View) : BaseMessagesViewHolder(itemView), LayoutContainer {

        override val containerView: View?
            get() = itemView

        fun bind(message: TechMessageItem) {
            itemView.techTextView.text = message.text
            itemView.timeTechTextView.text = message.dateTime
        }
    }

    inner class DateMessagesViewHolder(itemView: View) : BaseMessagesViewHolder(itemView), LayoutContainer {

        override val containerView: View?
            get() = itemView

        fun bind(message: ChatDateItem) {
            itemView.dateTextView.text = message.date
        }
    }

    inner class SecretInviteViewHolder(itemView: View) : BaseMessagesViewHolder(itemView), LayoutContainer {

        override val containerView: View?
            get() = itemView

        fun bind(message: ConnectItemMessage) {
            itemView.infoTextView.text = message.getText()
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

    class PercentPayload
    class CornerPayload
    class StatusPayload(val isOnline: Boolean)
}