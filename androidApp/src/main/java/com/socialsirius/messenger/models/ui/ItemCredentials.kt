package com.socialsirius.messenger.models.ui

import com.sirius.library.mobile.models.CredentialsRecord
import com.socialsirius.messenger.ui.chats.chat.message.BaseItemMessage
import com.socialsirius.messenger.ui.chats.chat.message.MessageType
import java.io.Serializable
import java.util.*

class ItemCredentials : BaseItemMessage, Serializable {

    constructor() {

    }

    constructor(name: String, date: Date, isActionExist: Boolean, credRecord: CredentialsRecord?) {
        this.name = name
        this.date = date
        this.isActionExist = isActionExist
        this.credRecord = MyCredentialsRecord.map(credRecord)
    }

    var name = ""
    var userName = ""
    var textComment = ""
    override fun getType(): MessageType {
        return MessageType.Base
    }

    override fun accept(comment: String?) {

    }

    override fun cancel() {

    }

    override fun getText(): String {
        return textComment
    }

    override fun getTitle(): String {
        return name
    }

    var isActionExist: Boolean = false
    var detailList: List<ItemCredentialsDetails> = listOf()
    var credRecord: MyCredentialsRecord? = null

}