package com.socialsirius.messenger.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.repository.local.BaseDatabase
import com.socialsirius.messenger.repository.local.MessageDatabase
import com.socialsirius.messenger.repository.models.LocalMessage
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

    fun updateErrorAccepted(
        id: String,
        isAccepted: Boolean,
        canceled: Boolean,
        error: String?,
        comment: String?
    ) {
        getDatabase().updateErrorAccepted(id, isAccepted, canceled, error, comment)
    }

    override fun createItem(item: LocalMessage) {
        super.createItem(item)
        eventStoreLiveData.postValue(item.id)
    }

}