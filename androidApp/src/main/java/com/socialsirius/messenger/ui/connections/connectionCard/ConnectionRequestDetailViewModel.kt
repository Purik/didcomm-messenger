package com.socialsirius.messenger.ui.connections.connectionCard

import android.content.res.ColorStateList
import android.content.res.loader.ResourcesProvider
import android.graphics.drawable.Drawable
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.ui.BaseViewModel
import com.socialsirius.messenger.ui.chats.chat.BaseMessagesViewHolder
import com.socialsirius.messenger.ui.chats.chat.MessagesAdapter
import com.socialsirius.messenger.ui.chats.chat.message.BaseItemMessage
import com.socialsirius.messenger.ui.chats.chat.message.OfferItemMessage
import com.socialsirius.messenger.ui.chats.chat.message.ProverItemMessage
import com.socialsirius.messenger.utils.DateUtils
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_chat_connection.view.*

import javax.inject.Inject

class ConnectionRequestDetailViewModel @Inject constructor() : BaseViewModel() {


    val connectionDetailsLiveData = MutableLiveData<String>()
    val connectionDateLiveData = MutableLiveData<String>()
    val connectionColorLiveData = MutableLiveData<ColorStateList>()
    val connectionImageLiveData = MutableLiveData<Drawable>()
    var item: BaseItemMessage? = null


    override fun setupViews() {
        super.setupViews()
        fillAll()
    }


    fun fillAll() {
        item?.let {
            fillBase(it)
            if (it is OfferItemMessage) {
                fillOffer(it)
            } else if (it is ProverItemMessage) {
                fillProver(it)
            }
        }
    }

    fun fillBase(item: BaseItemMessage) {
        connectionDateLiveData.value = item?.getTitle()
        connectionDetailsLiveData.value = item?.message?.serialize()
    }

    fun fillProver(item: ProverItemMessage) {
        connectionColorLiveData.postValue(App.getContext().resources.getColorStateList(R.color.cardConnectionCredentials))
        connectionImageLiveData.postValue(App.getContext().resources.getDrawable(R.drawable.ic_connection_verification))
    }

    fun fillOffer(item: OfferItemMessage) {
        connectionColorLiveData.postValue(App.getContext().resources.getColorStateList(R.color.cardOrange))
        connectionImageLiveData.postValue(App.getContext().resources.getDrawable(R.drawable.ic_connection_credentials2))
    }


}