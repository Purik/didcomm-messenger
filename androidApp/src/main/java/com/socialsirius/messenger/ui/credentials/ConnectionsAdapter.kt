@file:JvmName("ConnectionsAdapterKt")

package com.socialsirius.messenger.ui.credentials
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.socialsirius.messenger.R

import com.socialsirius.messenger.base.ui.SimpleBaseRecyclerViewAdapter
import com.socialsirius.messenger.models.ui.ItemCredentials
import com.socialsirius.messenger.ui.connections.connectionCard.BaseViewHolder



class ConnectionsAdapter(override val layoutRes: Int = R.layout.item_connection) :
    SimpleBaseRecyclerViewAdapter<ItemCredentials, ConnectionsAdapter.ConnectionsViewHolder>() {

    override fun onBind(holder: ConnectionsViewHolder, item: ItemCredentials, position: Int) {
     //   super.onBind(holder, item, position)
        holder.onCustomBtnClick = onCustomBtnClick
        holder.onAdapterItemClick = onAdapterItemClick
        holder.bind(item, position)
    }

    inner class ConnectionsViewHolder(itemView: View) : BaseViewHolder<ItemCredentials>(itemView) {


       val topTextView : TextView =  itemView.findViewById(R.id.topTextView)
       val bottomTextView : TextView =  itemView.findViewById(R.id.bottomTextView)
       val attributesTextView : TextView =  itemView.findViewById(R.id.attributesTextView)

        override fun bind(item: ItemCredentials, position: Int) {
            topTextView.text = item.getTitle()
            bottomTextView.text = item.userName

            val size = item.detailList.size ?: 0
            var text = "no attributes"
            if (size != 0) {
                text = "$size attributes"
            }
            attributesTextView.text = text
       //     itemView.iconView.setImageResource(item.icon)
     //       itemView.iconView.backgroundTintList =
     //           App.getContext().resources.getColorStateList(connectionItem.color)
            itemView.setOnClickListener {
                onAdapterItemClick?.onItemClick(item)
            }
        }
    }

    override fun getViewHolder(
        parent: ViewGroup?,
        layoutRes: Int,
        viewType: Int
    ): ConnectionsViewHolder {
        return ConnectionsViewHolder(getInflatedView(layoutRes,parent,false))
    }


}
