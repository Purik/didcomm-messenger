package com.socialsirius.messenger.ui.chats.groupChatCreate

import android.view.View
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.providers.ResourcesProvider
import com.socialsirius.messenger.base.ui.BaseViewModel
import javax.inject.Inject

class GroupChatCreateViewModel  @Inject constructor(val resourcesProvider: ResourcesProvider) : BaseViewModel(){

    override fun setTitle(title: String?) {
        super.setTitle(title)
    }


    override fun onCreateview() {
        super.onCreateview()
        setTitle(resourcesProvider.getString(R.string.create_group_title))
    }

    fun onCreateBtnClick(v : View){

    }
}