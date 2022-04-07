package com.socialsirius.messenger.ui.auth.createPhrase

import androidx.lifecycle.Observer
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.ui.BaseFragment

import com.socialsirius.messenger.databinding.FragmentCreatePhraseThirdBinding
import com.socialsirius.messenger.ui.activities.main.MainActivity


class CreatePhraseThirdFragment : BaseFragment<FragmentCreatePhraseThirdBinding, CreatePhraseThirdViewModel>() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_create_phrase_third
    }

    override fun subscribe() {
        model.startClickLiveData.observe(this, Observer {
            if(it){
                model.startClickLiveData.value = false
                MainActivity.newInstance(requireContext())
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