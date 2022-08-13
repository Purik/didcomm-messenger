package com.socialsirius.messenger.design.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.socialsirius.messenger.R
import kotlinx.android.synthetic.main.dialog_progress.*


class ProgressDialog constructor(context: Context, val hint : String? = null) : Dialog(context, true, null) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_progress)
        setCancelable(false)
        progressBar.animate()
        if (TextUtils.isEmpty(hint)){
            progressHintTitle.visibility = View.GONE
        }else{
            progressHintTitle.visibility = View.VISIBLE
            progressHintTitle.text =hint
        }
    }

}