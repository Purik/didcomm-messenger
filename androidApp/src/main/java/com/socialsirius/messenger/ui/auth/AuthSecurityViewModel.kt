package com.socialsirius.messenger.ui.auth

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import cash.z.ecc.android.bip39.Mnemonics
import cash.z.ecc.android.bip39.toSeed
import com.socialsirius.messenger.base.AppPref
import com.socialsirius.messenger.base.ui.BaseViewModel
import com.socialsirius.messenger.models.ui.PassPhraseItem
import com.socialsirius.messenger.repository.UserRepository
import javax.inject.Inject

class AuthSecurityViewModel @Inject constructor(val userRepository: UserRepository): BaseViewModel() {

    val startClickLiveData = MutableLiveData<Boolean>()
    val showNowClickLiveData = MutableLiveData<Boolean>()
    val createPinClickLiveData = MutableLiveData<Boolean>()

    fun onStartClick(v : View){
        startClickLiveData.postValue(true)
    }

    override fun setupViews() {
        super.setupViews()

    }

    fun showNow(v : View){
        showNowClickLiveData.postValue(true)
    }

    fun createPinClick(v : View){
        createPinClickLiveData.postValue(true)
    }





}