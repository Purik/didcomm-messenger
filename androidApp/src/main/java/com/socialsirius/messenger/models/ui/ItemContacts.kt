package com.socialsirius.messenger.models.ui

import java.io.Serializable
import java.util.*

class ItemContacts : Serializable{

    constructor() {

    }

    constructor(id : String,title: String, date: Date) {
        this.title = title
        this.date = date
        this.id = id
       // this.isActionExist = isActionExist
    }


    var title: String? = null
    var id: String? = null
    var date: Date? = null
    var isActionExist: Boolean = false


}