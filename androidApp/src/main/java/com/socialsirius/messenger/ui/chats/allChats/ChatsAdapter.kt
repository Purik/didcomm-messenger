package  com.socialsirius.messenger.ui.chats.allChats


import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.ui.SimpleBaseRecyclerViewAdapter
import com.socialsirius.messenger.databinding.ItemChatBinding
import com.socialsirius.messenger.models.Chats
import com.socialsirius.messenger.repository.models.LocalMessage
import com.socialsirius.messenger.transform.LocalMessageTransform
import com.socialsirius.messenger.ui.chats.chat.MessagesAdapter
import com.socialsirius.messenger.utils.DateUtils

import kotlinx.android.extensions.LayoutContainer

import java.util.*

class ChatsAdapter(override val layoutRes: Int = R.layout.item_chat) : SimpleBaseRecyclerViewAdapter<Chats, ChatsAdapter.ChatsViewHolder>() {



    override fun getViewHolder(parent: ViewGroup?, layoutRes: Int, viewType: Int): ChatsViewHolder {
        return ChatsViewHolder(getInflatedView(layoutRes,parent))
    }

    override fun onBind(holder: ChatsViewHolder, item : Chats , position: Int) {
        super.onBind(holder, item, position)
        holder.bind(item)
    }


    fun updateItem(item: Chats) {
    /*    for (i in 0 until items.size) {
            if(item.id == this.items[i].id){
                this.items[i] = item
                Collections.sort(this.items, Chats.lastMessageComparator)
                notifyDataSetChanged()
                return
            }
        }
        addItem(item)*/
    }
   // var statusMap : Map<String,Boolean> = HashMap<String,Boolean>()


    fun updateActivityStatus(status: Map<String, Boolean>) {
     //   statusMap = status
        notifyDataSetChanged()
     /*   val index = items.indexOfFirst { !it.isRoom && it.user?.jid == status.first }
        if (index >= 0) {
            notifyItemChanged(index, StatusPayload(status.second))
        }*/
    }

/*    fun deleteAllItems(notify: Boolean = true) {
        this.items.clear()
        if(notify){
            notifyDataSetChanged()
        }
    }*/
    
  /*  fun deleteItem(index: Int) {
        this.items.removeAt(index)
        notifyItemRemoved(index)
    }*/

    fun addItem(item: Chats) {
  /*      this.items.add(item)
        Collections.sort(this.items, Chats.lastMessageComparator)
        notifyDataSetChanged()*/
    }

    fun addItems(items: List<Chats>) {
   /*     if(items.isEmpty()){
            return
        }
        items.forEach { item->
            var isAdd = true
            for (i in this.items.indices) {
                if(item.id == this.items[i].id){
                    this.items[i] = item
                    isAdd = false
                   break
                }
            }
            if(isAdd){
                this.items.add(item)
            }
        }
        Collections.sort(this.items, Chats.lastMessageComparator)
        notifyDataSetChanged()*/
    }



  /*  override fun onBindViewHolder(holder: ChatsViewHolder, position: Int, payloads: MutableList<Any>) {
        when (val payload = payloads.firstOrNull()) {
            is MessagesAdapter.StatusPayload -> holder.updateActivityStatus(payload.isOnline)
            else              -> holder.bind(items[position])
        }
    }*/

    inner class ChatsViewHolder(itemView: View) : SimpleViewHolder<ItemChatBinding,Chats>(itemView) {



        override fun bind(chat: Chats) {
            binding?.nameTextView?.text = chat.title
            if (chat.unreadMessageNotInDB > 0) {
                binding?.unreadTextView?.visibility = View.VISIBLE
                binding?.unreadTextView?.text = chat.unreadMessageNotInDB.toString()
            } else {
                binding?.unreadTextView?.visibility = View.GONE
            }

            binding?.typingImageView?.visibility = View.GONE
            binding?.mutedImageView?.visibility = if (chat.isInSilentMode) View.VISIBLE else View.GONE
            binding?.avatarImageView?.update(chat)
          ///  val local = LocalMessageTransform.toBaseItemMessage(chat.lastMessage)
           // binding?.senderMessageTextView?.text = local.getText()
          //  binding?.timeTextView?.text = DateUtils.dateToHHmmss(chat.lastMessage?.sentTime);
        /*
            val userName = chat.getUserFromMembers(chat.lastMessage?.msg_from)?.contactName.orEmpty()
            val showName = chat.lastMessage?.contentType != ContentType.service  //Todo Other type?
            if (userName.isNotEmpty() && chat.isRoom && showName ) {
                senderTextView?.text = "$userName: "
            }else{
                senderTextView?.text = ""
            }
            Log.d("mylog20767", "chatTtiel = "+  chat.title)
            senderMessageTextView?.text = chat.lastMessage?.textByType
            Log.d("mylog20767", "messageTRext = "+ senderMessageTextView?.text.toString())
            avatarImageView?.update(chat)
            timeTextView?.text = DateUtils.dateToChatTimeHHmm(chat.lastMessage?.created_utc_timestamp?.toLong()?.let { Date(it) });
            Log.d("mylog20767", "dateTRext = "+ timeTextView?.text.toString())
            avatarImageView?.updateStatus(statusMap[chat.id] ?:false)


            val isMine = BaseMessageNew.MessageUserType.OutComing == chat.lastMessage?.messageUserType
            if(isMine){
                sentStatusImageView?.visibility = View.VISIBLE
            }else{
                sentStatusImageView?.visibility = View.GONE
            }
            when (chat.lastMessage?.status) {
                ChatMessageStatus.sent -> {
                    sentStatusImageView?.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.color.transparent))
                }
                ChatMessageStatus.error -> {
                    sentStatusImageView?.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_send_error))
                }
                ChatMessageStatus.acknowlege -> {
                    sentStatusImageView?.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_sent_and_read))
                }
                ChatMessageStatus.received -> {
                    sentStatusImageView?.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_sent_not_read))
                }
                ChatMessageStatus.default -> {
                    sentStatusImageView?.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_watch_later))
                }
                else -> {
                    sentStatusImageView?.visibility = View.GONE
                }
            }
            mutedImageView?.visibility = if (chat.isInSilentMode) View.VISIBLE else View.GONE
            lockedImageView?.visibility = if (chat is SecretChats) View.VISIBLE else View.GONE
            //todo
            typingImageView?.visibility = View.GONE

            itemView.setOnClickListener {
                onChatClick(chat)
            }*/
        }

        fun updateActivityStatus(isOnline: Boolean) {
          //  itemView.avatarImageView.updateStatus(isOnline)
        }

    }

}