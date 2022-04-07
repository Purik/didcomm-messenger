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

class AuthViewModel @Inject constructor( val userRepository: UserRepository): BaseViewModel() {

    val startClickLiveData = MutableLiveData<Boolean>()
    val showNowClickLiveData = MutableLiveData<Boolean>()
    var authName  = MutableLiveData<String>("")

    fun saveUserName(){
        userRepository.myUser.name = authName.value
        userRepository.saveUserToPref()
    }

    fun onStartClick(v : View){
        startClickLiveData.postValue(true)
        saveUserName()
    }


    val passPhraseListLiveData = MutableLiveData<List<PassPhraseItem>>()


    override fun setupViews() {
        super.setupViews()
        createPhrase()
       // val list = createList()
   //     passPhraseListLiveData.postValue(list)
    }

    fun showNow(v : View){
        showNowClickLiveData.postValue(true)
    }

    fun createPhrase() {
        val mnemonicCode: Mnemonics.MnemonicCode = Mnemonics.MnemonicCode(AppPref.getInstance().getDeviceId().toByteArray())
        val list: MutableList<String> = mutableListOf()
        mnemonicCode.words.forEach {
            list.add(it.concatToString())
        }
        //t=[coral, private, atom, hover, glory, box, remain, era, curtain, offer, escape, skill, adapt, bird, this, town, glimpse, hawk, flip, idea, addict, craft, sheriff, club, goddess, ranch]
      //  list=[coral, private, atom, hover, glory, box, remain, era, curtain, offer, escape, skill, adapt, bird, this, town, glimpse, hawk, flip, idea, addict, craft, sheriff, club, goddess, ranch]
      Log.d("mylog2090","list="+list)
      Log.d("mylog2090","AppPref.getInstance().getDeviceId()="+AppPref.getInstance().getDeviceId())
    }

   // fun createList(): List<PassPhraseItem> {

     /*   val list: MutableList<PassPhraseItem> = mutableListOf()
        mnemonicCode.words.forEachIndexed { index, chars ->
            val int =  index+1
            list.add(PassPhraseItem(int.toString(), chars.concatToString()))
        }
        return list*/
 //   }


}