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
import java.nio.charset.Charset
import javax.inject.Inject

class AuthViewModel @Inject constructor(val userRepository: UserRepository) : BaseViewModel() {

    val startClickLiveData = MutableLiveData<Boolean>()
    val showNowClickLiveData = MutableLiveData<Boolean>()
    var authName = MutableLiveData<String>("")

    fun saveUser() {
        userRepository.myUser.name = authName.value
        userRepository.myUser.uid = AppPref.getInstance().getDeviceId()
        userRepository.myUser.pass = createPhrase()
        userRepository.saveUserToPref()
    }

    fun onStartClick(v: View) {
        saveUser()
        startClickLiveData.postValue(true)
    }

    fun showNow(v: View) {
        showNowClickLiveData.postValue(true)
    }

    fun createPhrase(): String {
        val mnemonicCode: Mnemonics.MnemonicCode =
            Mnemonics.MnemonicCode(Mnemonics.WordCount.COUNT_12)
        val list: MutableList<String> = mutableListOf()
        mnemonicCode.words.forEachIndexed { index, chars ->
            list.add(chars.concatToString())
        }
        var string = ""
        list.forEachIndexed { index, s ->
            string = if (index == 0) {
                s
            } else {
                "$string $s"
            }
        }
        val pass = string
        println("createPhrase pass=" + pass)
        return pass

    }

}