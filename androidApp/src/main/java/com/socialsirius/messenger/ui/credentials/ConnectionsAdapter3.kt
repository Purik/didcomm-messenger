/*
package com.socialsirius.messenger.ui.credentials

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sirius.R
import com.sirius.base.App
import com.sirius.ui.chats.chat.item.ChatDateItem
import com.sirius.ui.connections.allConnections.item.ConnectionItem
import com.sirius.utils.Utils
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_connection.view.*
import kotlinx.android.synthetic.main.item_connection_title.view.titleTextView

private const val CONNECTION_ITEM = 1
private const val TITLE_ITEM = 2

class ConnectionsAdapter(private val onConnectionClick: (ConnectionItem) -> Unit) : Adapter<ViewHolder>() {

    private val items: MutableList<Any> = mutableListOf()

    fun setItems(items: List<Any>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == TITLE_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_connection_title, parent, false)
            ConnectionsTitleViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_connection, parent, false)
            ConnectionsViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is ConnectionsTitleViewHolder -> holder.bind(items[position] as String)
            is ConnectionsViewHolder      -> holder.bind(items[position] as ConnectionItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ConnectionItem -> CONNECTION_ITEM
            is String         -> TITLE_ITEM
            else              -> 0
        }
    }

    inner class ConnectionsViewHolder(itemView: View) : ViewHolder(itemView), LayoutContainer {

        override val containerView: View?
            get() = itemView

        fun bind(connectionItem: ConnectionItem) {
            itemView.topTextView.text = connectionItem.type
            itemView.bottomTextView.text = connectionItem.name
            itemView.attributesTextView.text = connectionItem.attrsCount
            itemView.iconView.setImageResource(connectionItem.icon)
            itemView.iconView.backgroundTintList = App.getContext().resources.getColorStateList(connectionItem.color)
            itemView.setOnClickListener {
                onConnectionClick(connectionItem)
            }
        }
    }

    inner class ConnectionsTitleViewHolder(itemView: View) : ViewHolder(itemView), LayoutContainer {

        override val containerView: View?
            get() = itemView

        fun bind(title: String) {
            itemView.titleTextView.text = title
        }
    }
}*/
