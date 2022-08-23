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
import com.socialsirius.messenger.models.ChatMessageStatus
import com.socialsirius.messenger.ui.chats.chat.item.*
import com.socialsirius.messenger.ui.chats.chat.message.*

import com.socialsirius.messenger.utils.DateUtils

import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_chat_message.view.*

import kotlinx.android.synthetic.main.item_chat_message_date.view.*
import kotlinx.android.synthetic.main.item_chat_message_tech.view.*
import kotlinx.android.synthetic.main.item_chat_secret_invite.view.*
import java.util.*


enum class MessageType {
    CHAT_MESSAGE_ITEM,
    TECH_MESSAGE_ITEM,
    DATE_MESSAGE_ITEM,
    SECRET_INVITE_MESSAGE_ITEM,
    CONNECTION_MESSAGE_ITEM,
    HOLDER_MESSAGE_ITEM,
    PROVER_MESSAGE_ITEM
}

private const val MAX_AUDIO_CACHE_SIZE = 200

class MessagesAdapter() :
    BaseRecyclerViewAdapter<BaseItemMessage, BaseMessagesViewHolder<BaseItemMessage>>() {

    val audioMessages = mutableMapOf<String, AudioMessageView>()

    companion object {
        val readActionId = -10
        val localReadActionId = -11
    }


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
            is TechMessageItem -> MessageType.TECH_MESSAGE_ITEM.ordinal
            is ChatDateItem -> MessageType.DATE_MESSAGE_ITEM.ordinal
            //   is SecretInviteItem -> SECRET_INVITE_MESSAGE_ITEM
            is TextItemMessage -> MessageType.CHAT_MESSAGE_ITEM.ordinal
            is ConnectItemMessage -> MessageType.SECRET_INVITE_MESSAGE_ITEM.ordinal
            //is ChatConnectionItem -> CONNECTION_MESSAGE_ITEM
            is OfferItemMessage -> MessageType.HOLDER_MESSAGE_ITEM.ordinal
            is ProverItemMessage -> MessageType.PROVER_MESSAGE_ITEM.ordinal
            else -> 0
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseMessagesViewHolder<BaseItemMessage> {
        return when (viewType) {
            MessageType.CHAT_MESSAGE_ITEM.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_message, parent, false)
                MessagesViewHolder(view)
            }

            MessageType.SECRET_INVITE_MESSAGE_ITEM.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_secret_invite, parent, false)
                SecretInviteViewHolder(view)
            }
            MessageType.DATE_MESSAGE_ITEM.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_message_date, parent, false)
                DateMessagesViewHolder(view)
            }
            MessageType.HOLDER_MESSAGE_ITEM.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_connection, parent, false)
                HolderViewHolder(view)
            }
            MessageType.PROVER_MESSAGE_ITEM.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_connection, parent, false)
                ProverViewHolder(view)
            }
            /*    CONNECTION_MESSAGE_ITEM -> {
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_connection, parent, false)
                    ChatConnectionViewHolder(view)
                }*/
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_message_tech, parent, false)
                TechMessagesViewHolder(view)
            }
        }
    }


    override fun onBindViewHolder(
        holder: BaseMessagesViewHolder<BaseItemMessage>,
        position: Int
    ) {
        val item = getItem(position)
        holder.onCustomBtnClick = onCustomBtnClick
        holder.onAdapterItemClick = onAdapterItemClick
        holder.bind(item, position)
     /*   when (holder) {
            is MessagesViewHolder -> {
                val item = item as TextItemMessage
                holder.bind(item, position)
            }
            is TechMessagesViewHolder -> holder.bind(item as TechMessageItem)
            is DateMessagesViewHolder -> holder.bind(item as ChatDateItem)
            is SecretInviteViewHolder -> holder.bind(item as ConnectItemMessage)

        }*/
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


    class PercentPayload
    class CornerPayload
    class StatusPayload(val isOnline: Boolean)
}