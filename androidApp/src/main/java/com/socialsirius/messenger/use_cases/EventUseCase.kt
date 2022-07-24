package com.socialsirius.messenger.use_cases

import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventUseCase @Inject constructor() {

    val pongMutableLiveData = MutableLiveData<Pair<Boolean, String>?>()


}