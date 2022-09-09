package com.socialsirius.messenger.ui.auth


import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.AppPref
import com.socialsirius.messenger.base.ui.BaseFragment
import com.socialsirius.messenger.databinding.FragmentAuthBinding
import com.socialsirius.messenger.ui.activities.main.MainActivity
import com.socialsirius.messenger.ui.auth.createPhrase.CreatePhraseFirstFragment
import com.socialsirius.messenger.ui.auth.createPhrase.CreatePhraseSecondFragment


class AuthFragment : BaseFragment<FragmentAuthBinding,AuthViewModel>() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_auth
    }

    override fun subscribe() {
        model.startClickLiveData.observe(this, Observer {
            if(it){
                model.startClickLiveData.value  = false
                baseActivity.pushPage(AuthSecurityFragment())
            }
        })


        model.showNowClickLiveData.observe(this, Observer {
            if(it){
                model.showNowClickLiveData.value = false
                baseActivity.pushPage(CreatePhraseSecondFragment())
            }
        })

        model.authName.observe(this, Observer {
            dataBinding.nextBtn.isEnabled = !it.isNullOrEmpty()
        })
    }

    override fun setupViews() {
        super.setupViews()
        dataBinding.nameEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                model.onNameChanged(s.toString())
            }

        })
    }
    override fun setModel() {
            dataBinding.viewModel = model
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }
}