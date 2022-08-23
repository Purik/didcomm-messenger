package com.socialsirius.messenger.ui.connections.connectionCard

import android.view.LayoutInflater
import android.view.ViewGroup
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.ui.BaseRecyclerViewAdapter
import com.socialsirius.messenger.models.ui.ItemCredentialsDetails

import com.socialsirius.messenger.ui.connections.connectionCard.items.*



enum class DetailType {
    RADIO,
    CHECKBOX,
    SUCCESS,
    ERROR,
    INFO,
    PHOTO
}


class ConnectionDetailsAdapter() :
    BaseRecyclerViewAdapter<DetailsBaseItem, BaseDetailViewHolder<DetailsBaseItem>>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseDetailViewHolder<DetailsBaseItem> {
        return when (viewType) {
            DetailType.RADIO.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_details_radio, parent, false)
                RadioViewHolder(view)
            }
            DetailType.CHECKBOX.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_details_checkbox, parent, false)
                CheckBoxViewHolder(view)
            }
            DetailType.SUCCESS.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_details_success, parent, false)
                SuccessViewHolder(view)
            }
            DetailType.ERROR.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_details_error, parent, false)
                ErrorViewHolder(view)
            }
            DetailType.PHOTO.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_details_photo, parent, false)
                PhotoViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_details_data, parent, false)
                InfoViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(
        holder: BaseDetailViewHolder<DetailsBaseItem>,
        position: Int
    ) {
        val item = getItem(position)
        holder.onCustomBtnClick = onCustomBtnClick
        holder.onAdapterItemClick = onAdapterItemClick
        holder.bind(item, position)

    }


    override fun getItemViewType(position: Int): Int {
        var type =  DetailType.INFO
        val item = getItem(position)
        type = when (item) {
            is DetailsSuccessItem ->  DetailType.SUCCESS
            is DetailsErrorItem ->  DetailType.ERROR
           // is DetailsInfoItem ->  DetailType.INFO
            is ItemCredentialsDetails ->  DetailType.INFO
            is DetailsCheckboxItem ->  DetailType.CHECKBOX
            is DetailsRadioItem -> DetailType. RADIO
            else ->  DetailType.INFO
        }
       /* if (item is DetailsInfoItem) {
            if (item.title?.lowercase() == "photo") {
                type =  DetailType.PHOTO
            }
        }*/
        return type.ordinal
    }


}