package com.socialsirius.messenger.design.chat

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.socialsirius.messenger.R

import kotlinx.android.synthetic.main.view_link_preview.view.headerTextView
import kotlinx.android.synthetic.main.view_link_preview.view.descTextView
import kotlinx.android.synthetic.main.view_link_preview.view.lineView
import kotlinx.android.synthetic.main.view_link_preview.view.logoView
import kotlinx.android.synthetic.main.view_link_preview.view.linkTextView

class LinkPreviewView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_link_preview, this)
    }

    fun setIsOnDarkBackground(darkBackground: Boolean) {
        if (darkBackground) {
            headerTextView.setTextColor(ContextCompat.getColor(this.context, R.color.backgroundColor))
            linkTextView.setTextColor(ContextCompat.getColor(this.context, R.color.backgroundColor))
            linkTextView.setLinkTextColor(ContextCompat.getColor(this.context, R.color.backgroundColor))
            descTextView.setTextColor(ContextCompat.getColor(this.context, R.color.backgroundColorWithAlpha))
            lineView.setBackgroundColor(ContextCompat.getColor(this.context, R.color.backgroundColor))
        } else {
            headerTextView.setTextColor(ContextCompat.getColor(this.context, R.color.defaultColor))
            linkTextView.setTextColor(ContextCompat.getColor(this.context, R.color.defaultColor))
            linkTextView.setLinkTextColor(ContextCompat.getColor(this.context, R.color.defaultColor))
            descTextView.setTextColor(ContextCompat.getColor(this.context, R.color.hintColor))
            lineView.setBackgroundColor(ContextCompat.getColor(this.context, R.color.defaultColor))
        }
    }

/*    fun updateLink(link: String, updateMetadataAction: (MetaData?) -> Unit) {
        if (link.isEmpty()){
            updateMetadataAction(null)
            return
        }
        val richPreview = RichPreview(object : ResponseListener {
            override fun onData(metaData: MetaData) {
                updateMetadataAction(metaData)
            }

            override fun onError(e: Exception) {
                updateMetadataAction(null)
            }
        })
        richPreview.getPreview(link)
    }*/

    /* fun updateWithMetadata(metaData: MetaData?) {
        if (metaData == null) {
            this@LinkPreviewView.visibility = View.GONE
            return
        }
        if (metaData.title.isNullOrEmpty()) {
            this@LinkPreviewView.visibility = View.GONE
        } else {
            this@LinkPreviewView.visibility = View.VISIBLE
            headerTextView.text = metaData.title
            linkTextView.text = metaData.url
            descTextView.text = metaData.description
            if (metaData.description.isNullOrEmpty()) {
                descTextView.visibility = View.GONE
            } else {
                descTextView.visibility = View.VISIBLE
            }

            if (!metaData.imageurl.isNullOrEmpty()) {
                logoView.visibility = View.VISIBLE
                try {
                    Glide.with(logoView).load(metaData.imageurl).apply(RequestOptions().fitCenter()).into(logoView)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                logoView.visibility = View.GONE
            }
        }
    }*/
}