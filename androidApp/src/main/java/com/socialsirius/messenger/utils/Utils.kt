package com.socialsirius.messenger.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.WindowManager
import com.socialsirius.messenger.ui.auth.AuthSecurityFragment
import java.io.FileOutputStream
import java.io.InputStream


class Utils {
    companion object {

        fun makeScreenshotUnavailable(activity: Activity?) {
            if (activity != null) {
                val window = activity.window
                window?.setFlags(
                    WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE
                )
            }
        }

        fun copyRawFile(context: Context, rawRes: Int, filepath: String) {
            val `in`: InputStream = context.getResources().openRawResource(rawRes)
            val out = FileOutputStream(filepath)
            val buff = ByteArray(1024)
            var read = 0

            try {
                while (`in`.read(buff).also { read = it } > 0) {
                    out.write(buff, 0, read)
                }
            } finally {
                `in`.close()
                out.close()
            }
        }


        fun logLongText(TAG: String?, sb: String?) {
            if (sb == null) {
                Log.v(TAG, sb ?: "")
                return
            }
            if (sb.length > 2900) {
                Log.v(TAG, "sb.length = " + sb.length)
                val chunkCount = sb.length / 2900 // integer division
                for (i in 0..chunkCount) {
                    val max = 2900 * (i + 1)
                    if (max >= sb.length) {
                        Log.v(
                            TAG,
                            "chunk " + i + " of " + chunkCount + ":" + sb.substring(2900 * i)
                        )
                    } else {
                        Log.v(
                            TAG,
                            "chunk " + i + " of " + chunkCount + ":" + sb.substring(2900 * i, max)
                        )
                    }
                }
            } else {
                Log.v(TAG, sb)
            }
        }
    }
}

