package com.socialsirius.messenger.models.ui

import com.sirius.library.mobile.models.CredentialsRecord
import java.io.Serializable
import java.util.*

class ItemCredentials : Serializable {

    constructor() {

    }

    constructor(title: String, date: Date, isActionExist: Boolean,credRecord : CredentialsRecord? ) {
        this.title = title
        this.date = date
        this.isActionExist = isActionExist
        this.credRecord = MyCredentialsRecord.map(credRecord)
    }

    var title: String? = null
    var date: Date? = null
    var isActionExist: Boolean = false
    var detailList: List<ItemCredentialsDetails> = listOf()
    var credRecord : MyCredentialsRecord? =null

}