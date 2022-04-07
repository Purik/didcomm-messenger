package com.socialsirius.messenger.utils.webview

import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient

class MyWebViewClient : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return super.shouldOverrideUrlLoading(view, request)
    }


    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {
        return super.shouldInterceptRequest(view, request)
    }
}