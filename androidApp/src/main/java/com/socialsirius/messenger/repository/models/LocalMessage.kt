package com.socialsirius.messenger.repository.models

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import com.sirius.library.agent.pairwise.Pairwise
import com.sirius.library.messaging.Message
import com.sirius.library.mobile.helpers.PairwiseHelper
import com.socialsirius.messenger.models.ChatMessageStatus
import java.io.Serializable


import java.util.*

@DatabaseTable(tableName = "messages")
class LocalMessage : DatabaseIdModel , Serializable{
    override fun getId(): Any? {
        return id
    }

    constructor()

    constructor(id: String? = null, pairwiseDid: String?) {
        this.id = id
        if (id == null) {
            this.id = UUID.randomUUID().toString()
        }
        this.pairwiseDid = pairwiseDid
    }

    @DatabaseField(columnName = "id", id = true)
    var id: String? = null

    @DatabaseField(columnName = "pairwiseDid")
    var pairwiseDid: String? = null

    @DatabaseField(columnName = "pairwiseLabel")
    var label: String? = null

    @DatabaseField(columnName = "isLoading")
    var isLoading: Boolean? = null

    @DatabaseField(columnName = "message")
    var message: String? = null

    @DatabaseField(columnName = "isMine")
    var isMine: Boolean = false

    @DatabaseField(columnName = "sentTime")
    var sentTime: Date? = null

    @DatabaseField(columnName = "type")
    var type: String? = null

    @DatabaseField(columnName = "isAccepted")
    var isAccepted: Boolean = false

    @DatabaseField(columnName = "isCanceled")
    var isCanceled: Boolean = false

    @DatabaseField(columnName = "acceptedComment")
    var acceptedComment: String? = null

    @DatabaseField(columnName = "canceledComment")
    var canceledCause: String? = null

    @DatabaseField(columnName = "status")
    var status : ChatMessageStatus = ChatMessageStatus.default

    fun message(): Message? {
        var restored: Message? = null
        try {
            restored =  Message.restoreMessageInstance(message).second
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return restored
    }

    fun restorePairwise() : Pairwise?{
       return PairwiseHelper.getInstance().getPairwise(pairwiseDid)
    }
}