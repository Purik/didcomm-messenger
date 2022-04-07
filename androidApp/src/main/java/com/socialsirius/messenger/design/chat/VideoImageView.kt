package com.socialsirius.messenger.design.chat

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.utils.FileUtils

import kotlinx.android.synthetic.main.view_message_document.view.*
import kotlinx.android.synthetic.main.view_message_image_video.view.*
import kotlinx.android.synthetic.main.view_message_image_video.view.progressBar
import java.io.File

class VideoImageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_message_image_video, this)
    }

    var isVideo: Boolean =false

    fun setUrlAndPreviewUrl(url: String?, previewUrl: String?, isVideo: Boolean) {
        playBtn.visibility = View.GONE
        this.isVideo = isVideo
        photoImageView.setBackgroundColor(App.getContext().resources.getColor(R.color.dividerColor))
        photoImageView.setImageDrawable(null)
        if (url.isNullOrEmpty() && previewUrl.isNullOrEmpty()) {
            return
        }
        // photoImageView.setBackgroundColor(App.getContext().resources.getColor(R.color.black))
        if (isVideo) {
            playBtn.visibility = View.VISIBLE
            photoImageView.setBackgroundColor(App.getContext().resources.getColor(R.color.black))
        } else {
            playBtn.visibility = View.GONE
            photoImageView.setBackgroundColor(App.getContext().resources.getColor(R.color.dividerColor))
           /* if (previewUrl?.contains("http") == true) {
                Glide.with(photoImageView)
                        .load(ApiUtil.getGlideUrlWithAuthorize(previewUrl))
                        .apply(RequestOptions().fitCenter())
                        .into(photoImageView)
            } else {
                if (FileUtils.isFileExist(previewUrl)) {
                    Glide.with(photoImageView)
                            .load(File(previewUrl!!))
                            .apply(RequestOptions().fitCenter())
                            .into(photoImageView)
                }
            }*/
        }
    }


    fun updateProgress(percent: Int, isDownload: Boolean, isCanceled : Boolean) {
        progressBar.progress = percent
        cancelIcon.setImageResource(R.drawable.ic_close)
        if (percent < 100) {
            progressBar.visibility = View.VISIBLE
            cancelBtn.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
            cancelBtn.visibility = View.GONE
        }

        if(isCanceled){
            progressBar.visibility = View.VISIBLE
            cancelBtn.visibility = View.VISIBLE
       //     cancelIcon.setImageResource(R.drawable.fetch_notification_retry)
        }

        if(percent==101 && isVideo ){
            progressBar.visibility = View.VISIBLE
            cancelBtn.visibility = View.VISIBLE 
            cancelIcon.setImageResource(R.drawable.ic_download_arrow)
        }
    }

}