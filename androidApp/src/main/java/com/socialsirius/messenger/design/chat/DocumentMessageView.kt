package com.socialsirius.messenger.design.chat

import android.content.Context
import android.graphics.PorterDuff
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.socialsirius.messenger.R


class DocumentMessageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

   private var  documentIcon : ImageView
   private var  documentName : TextView
   var  progressBar : ProgressBar

    init {
        val view = View.inflate(context, R.layout.view_message_document, this)
        documentIcon = view.findViewById(R.id.documentIcon)
        documentName = view.findViewById(R.id.documentName)
        progressBar = view.findViewById(R.id.progressBar)
    }

    fun setIsOnDarkBackground(darkBackground: Boolean) {
        if (darkBackground) {
            documentIcon.setColorFilter(ContextCompat.getColor(this.context, R.color.backgroundColor), PorterDuff.Mode.SRC_ATOP)
            documentName.setTextColor(ContextCompat.getColor(this.context, R.color.backgroundColor))
            progressBar.progressDrawable = ContextCompat.getDrawable(this.context, R.drawable.bg_progress_circle_light)
        } else {
            documentIcon.setColorFilter(ContextCompat.getColor(this.context, R.color.defaultColor), PorterDuff.Mode.SRC_ATOP)
            documentName.setTextColor(ContextCompat.getColor(this.context, R.color.defaultColor))
            progressBar.progressDrawable = ContextCompat.getDrawable(this.context, R.drawable.bg_progress_circle_dark)
        }
    }


    fun updateProgress(percent: Int, isDownload: Boolean) {
        progressBar.progress = percent
        documentIcon.setImageResource(R.drawable.ic_file)
        if (percent < 100) {
            if (percent == 0 && isDownload){
                documentIcon.setImageResource(R.drawable.ic_download_arrow)
                progressBar.visibility = View.INVISIBLE
                documentIcon.setPadding(4, 4, 4, 4)
            } else{
                progressBar.visibility = View.VISIBLE
                documentIcon.setPadding(4, 4, 4, 4)
            }
        } else {
            progressBar.visibility = View.INVISIBLE
            documentIcon.setPadding(0, 0, 0, 0)
        }
    }

    fun setDocument(url: String?, filename : String? ) {
        if (url.isNullOrEmpty()) {
            return
        }
        val uri = Uri.parse(url)
        if(filename.isNullOrEmpty()){
            documentName.text = uri.lastPathSegment
        }else{
            documentName.text = filename
        }
    }
}