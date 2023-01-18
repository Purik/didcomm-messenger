package com.socialsirius.messenger.ui.errors

import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.ui.BaseFragment
import com.socialsirius.messenger.databinding.FragmentBaseErrorBinding

open class BaseErrorFragment : BaseFragment<FragmentBaseErrorBinding, BaseErrorViewModel>() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_base_error
    }

    override fun subscribe() {

    }

    override fun setModel() {

    }

    override fun initDagger() {

    }
}