package com.socialsirius.messenger.ui.auth.createPhrase

import android.view.View
import androidx.lifecycle.MutableLiveData
import cash.z.ecc.android.bip39.Mnemonics
import cash.z.ecc.android.bip39.toSeed
import com.socialsirius.messenger.base.AppPref
import com.socialsirius.messenger.base.ui.BaseViewModel
import com.socialsirius.messenger.databinding.ItemPassphraseBinding
import com.socialsirius.messenger.models.ui.PassPhraseItem
import com.socialsirius.messenger.repository.UserRepository
import java.nio.charset.Charset
import javax.inject.Inject

class CreatePhraseSecondViewModel @Inject constructor(val userRepository: UserRepository) : BaseViewModel() {

    val startClickLiveData = MutableLiveData<Boolean>()
    val passPhraseListLiveData = MutableLiveData<List<PassPhraseItem>>()
    val mnemonicCode: Mnemonics.MnemonicCode = Mnemonics.MnemonicCode(userRepository.myUser.pass!!.toByteArray(Charset.defaultCharset()))

    fun onStartClick(v: View) {
        startClickLiveData.postValue(true)
    }

    override fun setupViews() {
        super.setupViews()
        val list = createList()
        passPhraseListLiveData.postValue(list)
    }


    fun createList(): List<PassPhraseItem> {
        val list: MutableList<PassPhraseItem> = mutableListOf()
        mnemonicCode.words.forEachIndexed { index, chars ->
           val int =  index+1
            list.add(PassPhraseItem(int.toString(), chars.concatToString()))
        }
        return list
    }


}