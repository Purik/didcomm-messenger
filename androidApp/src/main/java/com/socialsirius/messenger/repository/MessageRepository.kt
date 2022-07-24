package com.socialsirius.messenger.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.repository.local.BaseDatabase
import com.socialsirius.messenger.repository.local.MessageDatabase
import com.socialsirius.messenger.repository.models.LocalMessage
import com.socialsirius.messenger.repository.models.MessageStatus
import java.sql.SQLException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepository @Inject constructor() : BaseRepository<LocalMessage, String>() {


    val eventStartLiveData: MutableLiveData<String> = MutableLiveData()
    val eventStoreLiveData: MutableLiveData<String> = MutableLiveData()
    val eventStopLiveData: MutableLiveData<String> = MutableLiveData()
    val invitationStartLiveData: MutableLiveData<String?> = MutableLiveData()
    val invitationErrorLiveData: MutableLiveData<Pair<Boolean, String?>?> = MutableLiveData()
    val invitationSuccessLiveData: MutableLiveData<String?> = MutableLiveData()
    val invitationPolicemanSuccessLiveData: MutableLiveData<String?> = MutableLiveData()


    override fun createDatabase(): BaseDatabase<LocalMessage, String> {
        return MessageDatabase(App.getContext())
    }


    fun getMessagesForPairwiseDid(did: String): LiveData<List<LocalMessage>> {
        return getItemsBy("pairwiseDid", did)
    }


    fun deleteAllForPairwiseDid(did: String, deleteInvite: Boolean) {
        if (deleteInvite) {
            deleteAllFor("pairwiseDid", did)
        } else {
            deleteForPairwiseDidExceptInvite(did)
        }

        eventStoreLiveData.postValue(did)
    }


    fun deleteForPairwiseDidExceptInvite(did : String) {
        try {
            val builder = getDatabase().getDeleteBuilder()
            builder.where().eq("pairwiseDid", did).and().not().like("message","%/invitation%")
            builder.delete()
        } catch (throwables: SQLException) {
            throwables.printStackTrace()
        }
    }


    fun getLastMessagesForPairwiseDid(did: String): LiveData<LocalMessage?> {
        val livedata = MutableLiveData<LocalMessage?>()
        livedata.postValue(getDatabase().getLastMessagesForDid(did))
        return livedata
    }

    fun getLastMessagesForPairwiseDid2(did: String): LocalMessage? {
        return getDatabase().getLastMessagesForDid(did)
    }


    fun getAllUnacceptedMessages(): LiveData<List<LocalMessage>> {
        val livedata = MutableLiveData<List<LocalMessage>>()
        livedata.postValue(getDatabase().getMainActionsMessages())
        return livedata
        /*   val map = HashMap<String, Boolean>()
           map["isAccepted"] = false
           map["isCanceled"] = false
           map["type"] = false
           return getItemsBy(map)*/
    }

    fun getUnreadcountForDid(did: String): Int {
        return getDatabase().getUnreadMessages(did).toInt()

    }


    override fun getDatabase(): MessageDatabase {
        return super.getDatabase() as MessageDatabase
    }

    override fun createOrUpdateItem(item: LocalMessage) {
        super.createOrUpdateItem(item)
        eventStoreLiveData.postValue(item.id)
    }

    fun startLoading(id: String) {
        getDatabase().updateLoading(id, true)
        eventStoreLiveData.postValue(id)
    }

    fun deleteMessage(id: String?) {
        getDatabase().removeBy(id = id)
        eventStoreLiveData.postValue(id)
    }


    fun updateErrorAccepted(
        id: String,
        isAccepted: Boolean,
        canceled: Boolean,
        error: String?,
        comment: String?
    ) {
        getDatabase().updateErrorAccepted(id, isAccepted, canceled, error, comment)
    }


    fun updateStatus(
        id: String,
        status: MessageStatus
    ) {
        getDatabase().updateStatus(id, status)
    }

    override fun createItem(item: LocalMessage) {
        super.createItem(item)
        eventStoreLiveData.postValue(item.id)
    }

}