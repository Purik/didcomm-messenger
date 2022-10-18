package com.socialsirius.messenger.ui.connections.connectionCard

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sirius.library.utils.Base64

import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.ui.OnAdapterItemClick
import com.socialsirius.messenger.base.ui.OnCustomBtnClick
import com.socialsirius.messenger.models.ui.ItemCredentialsDetails
import com.socialsirius.messenger.ui.chats.chat.message.BaseItemMessage
import com.socialsirius.messenger.ui.connections.connectionCard.items.*
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_details_checkbox.view.*
import kotlinx.android.synthetic.main.item_details_data.view.*
import kotlinx.android.synthetic.main.item_details_error.view.*
import kotlinx.android.synthetic.main.item_details_photo.view.*
import kotlinx.android.synthetic.main.item_details_radio.view.*
import kotlinx.android.synthetic.main.item_details_success.view.*


abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView),
    LayoutContainer {
    override val containerView: View?
        get() = itemView
    var onCustomBtnClick: OnCustomBtnClick<T>? = null
    var onAdapterItemClick: OnAdapterItemClick<T>? = null


    abstract fun bind(item: @UnsafeVariance T, position: Int)
}

abstract class BaseDetailViewHolder<out T : DetailsBaseItem>(itemView: View) :
    BaseViewHolder<@UnsafeVariance T>(itemView) {


}

class CheckBoxViewHolder(itemView: View) : BaseDetailViewHolder<DetailsCheckboxItem>(itemView),
    LayoutContainer {

    override fun bind(item: DetailsCheckboxItem, position: Int) {
        itemView.itemNameCheckboxTextView.text = item.title
    }

}

class RadioViewHolder(itemView: View) : BaseDetailViewHolder<DetailsRadioItem>(itemView),
    LayoutContainer {


    override fun bind(item: DetailsRadioItem, position: Int) {
        itemView.itemNameRadioTextView.text = item.title
    }

}

class SuccessViewHolder(itemView: View) : BaseDetailViewHolder<DetailsSuccessItem>(itemView),
    LayoutContainer {


    override fun bind(item: DetailsSuccessItem, position: Int) {
        itemView.itemNameSuccesTextView.text = item.title
    }

}

class ErrorViewHolder(itemView: View) : BaseDetailViewHolder<DetailsErrorItem>(itemView),
    LayoutContainer {

    override fun bind(item: DetailsErrorItem, position: Int) {
        itemView.itemNameTextView.text = item.title
    }

}

class InfoViewHolder(itemView: View) : BaseDetailViewHolder<ItemCredentialsDetails>(itemView),
    LayoutContainer {

    val itemTitleTextView: TextView = itemView.findViewById(R.id.itemTitleTextView)
    val itemDescTextView: TextView = itemView.findViewById(R.id.itemDescTextView)
    val imageView: ImageView = itemView.findViewById(R.id.imageView)

    override fun bind(item: ItemCredentialsDetails, position: Int) {
        itemTitleTextView.text = item.title
        if (item.mimeType?.contains("image") == true) {
            item.value?.let {
                try {
                    imageView.visibility = View.VISIBLE
                    itemDescTextView.visibility = View.GONE
                    val imageBytes = Base64.getUrlDecoder().decode(item.value)
                    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    imageView.setImageBitmap(decodedImage)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {

            itemDescTextView.text = item.value
        }


    }

}

class PhotoViewHolder(itemView: View) : BaseDetailViewHolder<DetailsInfoItem>(itemView),
    LayoutContainer {


    override fun bind(item: DetailsInfoItem, position: Int) {
        /*    itemView.itemTitleTextView.text = item.title
            val imageBytes =  java.util.Base64.getUrlDecoder().decode(item.value);
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            itemView?.imageView?.setImageBitmap(decodedImage)*/
    }

}