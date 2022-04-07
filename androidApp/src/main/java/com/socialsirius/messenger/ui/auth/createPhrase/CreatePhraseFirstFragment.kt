package com.socialsirius.messenger.ui.auth.createPhrase

import androidx.lifecycle.Observer
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.ui.BaseFragment
import com.socialsirius.messenger.databinding.FragmentCreatePhraseFirstBinding


class CreatePhraseFirstFragment : BaseFragment<FragmentCreatePhraseFirstBinding, CreatePhraseFirstViewModel>() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_create_phrase_first
    }

    override fun subscribe() {
        model.startClickLiveData.observe(this, Observer {
            if(it){
                model.startClickLiveData.value = false
                baseActivity.pushPage(CreatePhraseSecondFragment())
            }

        })
    }

    override fun setupViews() {
        super.setupViews()


    }
    override fun setModel() {
            dataBinding.viewModel = model
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }
}