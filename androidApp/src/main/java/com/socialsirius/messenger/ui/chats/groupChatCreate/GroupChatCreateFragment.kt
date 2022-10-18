package com.socialsirius.messenger.ui.chats.groupChatCreate

import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.ui.BaseFragment
import com.socialsirius.messenger.databinding.FragmentGroupCreateBinding

class GroupChatCreateFragment  :
    BaseFragment<FragmentGroupCreateBinding, GroupChatCreateViewModel>() {


    override fun getLayoutRes(): Int = R.layout.fragment_group_create

    override fun subscribe() {

    }



    override fun setModel() {
        dataBinding!!.viewModel = model
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }
}