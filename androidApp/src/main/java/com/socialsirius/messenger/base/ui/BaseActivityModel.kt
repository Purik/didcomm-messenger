package com.socialsirius.messenger.base.ui

import androidx.lifecycle.MutableLiveData
import com.socialsirius.messenger.base.providers.ResourcesProvider
import com.socialsirius.messenger.design.BottomNavView


abstract class BaseActivityModel() : BaseViewModel()  {

    val  bottomNavClick : MutableLiveData<BottomNavView.BottomTab> =  MutableLiveData(BottomNavView.BottomTab.Menu)
    var selectedTab = MutableLiveData(BottomNavView.BottomTab.Menu)
    val  isVisibleUnauthBottomBar   : MutableLiveData<Pair<Boolean,Boolean>> =  MutableLiveData<Pair<Boolean,Boolean>>(Pair(false,false))

    fun getOnBottomNavClickListner() : BottomNavView.OnbottomNavClickListener{
        return object : BottomNavView.OnbottomNavClickListener{
            override fun onBottomClick(tab: BottomNavView.BottomTab) {
                bottomNavClick.postValue(tab)
            }
        }
    }

}