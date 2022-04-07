package com.socialsirius.messenger.transform

import com.socialsirius.messenger.models.ui.ItemContacts


interface Transformation  {

    fun toItemContacts() : ItemContacts? {
        return null
    }
}