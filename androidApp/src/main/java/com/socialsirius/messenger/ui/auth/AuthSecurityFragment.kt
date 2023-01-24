package com.socialsirius.messenger.ui.auth

import android.os.Bundle
import androidx.lifecycle.Observer
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.ui.BaseFragment
import com.socialsirius.messenger.databinding.FragmentAuthSecurityBinding
import com.socialsirius.messenger.ui.activities.loader.LoaderActivity
import com.socialsirius.messenger.ui.pinCreate.CreatePinFragment
import com.socialsirius.messenger.utils.Utils


class AuthSecurityFragment : BaseFragment<FragmentAuthSecurityBinding,AuthSecurityViewModel>() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_auth_security
    }

    override fun subscribe() {
        model.startClickLiveData.observe(this, Observer {
            if(it){
                model.startClickLiveData.value  = false
                baseActivity.finishAffinity()
                LoaderActivity.newInstance(requireContext(),null)
            }
        })


        model.showNowClickLiveData.observe(this, Observer {
            if(it){
                model.showNowClickLiveData.value = false
                PhraseListFragment().show(baseActivity.supportFragmentManager, "PhraseDialog")
             //   baseActivity.pushPage(CreatePhraseSecondFragment())
            }

        })

        model.createPinClickLiveData.observe(this, Observer {
            if(it){
                model.createPinClickLiveData.value = false
                baseActivity.pushPage(CreatePinFragment.newInstance())
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.makeScreenshotUnavailable(baseActivity)
    }
}