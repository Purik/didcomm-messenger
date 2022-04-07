package com.socialsirius.messenger.base.providers

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import com.socialsirius.messenger.base.App


import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourcesProvider @Inject constructor() {

    fun getString(resId: Int): String {
        return App.getInstance().getString(resId)
    }

    fun getString(resId: Int, formatArg: Any): String {
        return App.getInstance().getString(resId, formatArg)
    }


    fun getPluralsString(resId: Int, quantity :Int ,formatArg: Any?): String {
        return App.getInstance().resources.getQuantityString(resId,quantity,formatArg)
    }

    fun getLocale(): Locale {
        return if (VERSION.SDK_INT >= VERSION_CODES.N) {
            App.getContext().resources.configuration.locales[0]
        } else {
            App.getContext().resources.configuration.locale
        }
    }

    fun getColor(color : Int) : Int {
       return   App.getContext().resources.getColor(color)
    }




}