package com.socialsirius.messenger.ui.main

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.socialsirius.messenger.base.providers.ResourcesProvider
import com.socialsirius.messenger.base.ui.BaseViewModel
import com.socialsirius.messenger.repository.UserRepository

import javax.inject.Inject



open class MainViewModel @Inject constructor(
    val userRepository: UserRepository,
    resourcesProvider: ResourcesProvider
) : BaseViewModel() {

    val registerHintLiveData = MutableLiveData<String>("")
    val registerFieldHintLiveData = MutableLiveData<String>("")
    val registerBtnTextLiveData = MutableLiveData<String>("")
    val alreadyExistAccountTextLiveData = MutableLiveData<CharSequence>()
    val goToNextScreenLiveData = MutableLiveData<Boolean>()
    val nextVisibilityLiveData = MutableLiveData<Int>()



    fun onRegisterClick(v: View) {
        goToNextScreenLiveData.postValue(true)
    }

    override fun setupViews() {
        super.setupViews()
        isNextEnabled()
    }


    fun isNextEnabled()  {
        val isNextEnabled = !userRepository.myUser.name.isNullOrEmpty() && !userRepository.myUser.pass.isNullOrEmpty()
        if(isNextEnabled){
            nextVisibilityLiveData.postValue( View.VISIBLE)
        }else{
            nextVisibilityLiveData.postValue(  View.INVISIBLE)
        }

    }

    fun setUserName(name: String) {
        userRepository.myUser.name = name
    }

    fun setUserPass(pass: String) {
        userRepository.myUser.pass = pass
    }

    fun saveUser() {
        userRepository.saveUserToPref()
    }

}


