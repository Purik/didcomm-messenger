package com.socialsirius.messenger.ui.activities.tutorial


import android.view.View
import androidx.lifecycle.MutableLiveData
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.providers.ResourcesProvider
import com.socialsirius.messenger.base.ui.BaseActivityModel
import com.socialsirius.messenger.models.ui.ItemTutorial
import javax.inject.Inject

class TutorialActivityModel @Inject constructor(
    val resourceProvider: ResourcesProvider
) :
    BaseActivityModel() {


    val itemListLiveData: MutableLiveData<List<ItemTutorial>> = MutableLiveData()
    val nextClickLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val startClickLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val skipVisibilityLiveData: MutableLiveData<Int> = MutableLiveData(View.VISIBLE)
    val nextTextLiveData: MutableLiveData<String> = MutableLiveData("resourceProvider.getString(R.string.auth_next)")

    fun onSkipBtnClick(v: View) {
        startClickLiveData.postValue(true)
    }

    fun onNextBtnClick(v: View) {
        if(skipVisibilityLiveData.value == View.INVISIBLE){
            startClickLiveData.postValue(true)
        }else{
            nextClickLiveData.postValue(true)
        }

    }

    fun createList() {
        val list : MutableList<ItemTutorial> = mutableListOf()
        val item1= ItemTutorial(1,resourceProvider.getString(R.string.tutorial_title_1),
            resourceProvider.getString(R.string.tutorial_text_1), R.drawable.logo_sirius)
        val item2= ItemTutorial(2,resourceProvider.getString(R.string.tutorial_title_2),resourceProvider.getString(R.string.tutorial_text_2), R.drawable.tutorial_2)
        val item3= ItemTutorial(3,resourceProvider.getString(R.string.tutorial_title_3),resourceProvider.getString(R.string.tutorial_text_3), R.drawable.tutorial_3)
        val item4= ItemTutorial(4,resourceProvider.getString(R.string.tutorial_title_4),resourceProvider.getString(R.string.tutorial_text_4), R.drawable.tutorial_4)
        list.add(item1)
        list.add(item2)
       list.add(item3)
       list.add(item4)
        itemListLiveData.postValue(list)
    }
}