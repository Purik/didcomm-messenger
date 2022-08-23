package com.socialsirius.messenger.models.ui

import com.socialsirius.messenger.ui.connections.connectionCard.items.DetailsBaseItem
import java.io.Serializable
import java.util.*

class ItemCredentialsDetails : DetailsBaseItem,  Serializable{


    constructor(title: String?, value: String?, mimeType: String? = null) : super(title,value) {
        this.mimeType = mimeType
    }

    var isExistInWallet = false
    var mimeType : String? = null

    override fun toString(): String {
        return "title=$title "
    }
}