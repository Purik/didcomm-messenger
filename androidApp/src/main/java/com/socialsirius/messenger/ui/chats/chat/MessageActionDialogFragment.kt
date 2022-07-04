package com.socialsirius.messenger.ui.chats.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.socialsirius.messenger.R
import com.socialsirius.messenger.ui.chats.chat.item.MessageActionItem
import com.socialsirius.messenger.ui.chats.chat.item.MessageActionItemType
import kotlinx.android.synthetic.main.layout_chat_bottom_sheet.*


class MessageActionDialogFragment(
        private val items: List<MessageActionItem>
) : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_chat_bottom_sheet, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        items.forEach { item ->
            when (item.type) {
                MessageActionItemType.DELETE -> {
                    deleteButton.visibility = View.VISIBLE
                    deleteButton.setOnClickListener {
                        item.action.invoke(item.message)
                        dismissAllowingStateLoss()
                    }
                }
                MessageActionItemType.DELETE_LOCALLY -> {
                    deleteLocallyButton.visibility = View.VISIBLE
                    deleteLocallyButton.setOnClickListener {
                        item.action.invoke(item.message)
                        dismissAllowingStateLoss()
                    }
                }
                MessageActionItemType.RESEND -> {
                    resendButton.visibility = View.VISIBLE
                    resendButton.setOnClickListener {
                        item.action.invoke(item.message)
                        dismissAllowingStateLoss()
                    }
                }
                MessageActionItemType.QUOTE -> {
                    quoteButton.visibility = View.VISIBLE
                    quoteButton.setOnClickListener {
                        item.action.invoke(item.message)
                        dismissAllowingStateLoss()}
                }
            }
        }
    }
}