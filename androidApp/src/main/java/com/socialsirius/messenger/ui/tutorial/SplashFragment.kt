package com.socialsirius.messenger.ui.tutorial

import android.text.Editable
import android.text.TextWatcher

import androidx.lifecycle.Observer
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.ui.BaseFragment
import com.socialsirius.messenger.databinding.FragmentSplashBinding


class SplashFragment : BaseFragment<FragmentSplashBinding, SplashViewModel>() {



    override fun setupViews() {
        super.setupViews()
    //    Thread.sleep(1000)
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_splash
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }

    override fun subscribe() {

    }

    override fun setModel() {
   //     dataBinding.viewModel = model
    }



}