package com.socialsirius.messenger.ui.chats.invitations

import android.view.View
import android.view.ViewGroup
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.ui.SimpleBaseRecyclerViewAdapter
import com.socialsirius.messenger.databinding.ItemChatBinding
import com.socialsirius.messenger.databinding.ItemInvitationsUnacceptedBinding
import com.socialsirius.messenger.models.Chats

class InvitationsAdapter(override val layoutRes: Int = R.layout.item_invitations_unaccepted) :
    SimpleBaseRecyclerViewAdapter<Chats, InvitationsAdapter.InvitationsChatsViewHolder>() {

    inner class InvitationsChatsViewHolder(itemView: View) :
        SimpleBaseRecyclerViewAdapter.SimpleViewHolder<ItemInvitationsUnacceptedBinding, Chats>(itemView) {


        override fun bind(chat: Chats, position: Int) {
            binding?.nameTextView?.text = chat.title
          //  binding?.nameTextView?.text = chat.title
        }


    }

    override fun getViewHolder(
        parent: ViewGroup?,
        layoutRes: Int,
        viewType: Int
    ): InvitationsChatsViewHolder {
      return InvitationsChatsViewHolder(getInflatedView(layoutRes,parent, false))
    }
}