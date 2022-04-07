package com.socialsirius.messenger.ui.auth.createPhrase

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.socialsirius.messenger.base.ui.BaseViewModel
import javax.inject.Inject

class CreatePhraseThirdViewModel @Inject constructor(): BaseViewModel() {

    val startClickLiveData = MutableLiveData<Boolean>()
    var authName  = MutableLiveData<String>("")


    fun onStartClick(v : View){
        startClickLiveData.postValue(true)
    }


}