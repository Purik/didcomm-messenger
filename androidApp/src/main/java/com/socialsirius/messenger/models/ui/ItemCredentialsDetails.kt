package com.socialsirius.messenger.models.ui

import java.io.Serializable
import java.util.*

class ItemCredentialsDetails  : Serializable{

    constructor() {

    }

    constructor(name: String, value: String, mimeType: String) {
        this.name = name
        this.value = value
        this.mimeType = mimeType
    }

    var name: String? = null
    var value: String? = null
    var isExistInWallet = false
    var mimeType : String = ""

    override fun toString(): String {
        return "name=$name "
    }
}