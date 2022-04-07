package com.socialsirius.messenger.design


import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer



class BottomNavView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    enum class BottomTab {
        Contacts,
        Menu,
        Credentials,
        Profile
    }
   public  val selectedTabLiveData = MutableLiveData<BottomTab>(BottomTab.Contacts)

    var onBottomNavClickListener : OnbottomNavClickListener?  = null
  //  var selectedTab = BottomTab.Home
    interface OnbottomNavClickListener{
        fun onBottomClick(tab : BottomTab)
    }

    fun showActionsBtn(show : Boolean){
        if (show){
         //   actionsBtn.visibility = VISIBLE

        }else{
           // actionsBtn.visibility = INVISIBLE
        }

    }



    init {
     /*  val view =  View.inflate(context, R.layout.view_navigation_bottom, this)

        requestsBtn.isClickable = true
        notificationsBtn.isClickable = true
        cabinetBtn.isClickable = true

        requestsBtn.setOnClickListener { onButtonClick(BottomTab.Contacts) }
        notificationsBtn.setOnClickListener { onButtonClick(BottomTab.Menu) }
        cabinetBtn.setOnClickListener {  onButtonClick(BottomTab.Credentials) }
        showActionsBtn(false)
        setImagesForTabs()
        selectedTabLiveData.observeForever( Observer {tab->
            setImagesForTabs()
            onBottomNavClickListener?.onBottomClick(tab)
        })*/
    }

    private fun onButtonClick(tab : BottomTab){
        selectedTabLiveData.postValue(tab)
    }

    private fun setAllButtonUnselected(){
       // requestsBtn.alpha = 0.7F
      //  notificationsBtn.alpha = 0.7F
     //   cabinetBtn.alpha = 0.7F
    //    requestsBtnText.setTextSize(TypedValue.COMPLEX_UNIT_SP,10F)
    //    notificationsBtnText.setTextSize(TypedValue.COMPLEX_UNIT_SP,10F)
    //    cabinetBtnText.setTextSize(TypedValue.COMPLEX_UNIT_SP,10F)
    }
    private fun setImagesForTabs(){
       /* when(selectedTabLiveData.value){
            BottomTab.Contacts->{
                setAllButtonUnselected()
                viewBack.backgroundTintList = ColorStateList.valueOf(App.getContext().resources.getColor(R.color.grey3))
                requestsBtnText.visibility = View.INVISIBLE
                cabinetBtnText.visibility = View.INVISIBLE
            }
            BottomTab.Menu -> {
                setAllButtonUnselected()
                viewBack.backgroundTintList =null
                requestsBtnText.visibility = View.VISIBLE
                cabinetBtnText.visibility = View.VISIBLE
            }
            BottomTab.Credentials -> {
                setAllButtonUnselected()
                viewBack.backgroundTintList = ColorStateList.valueOf(App.getContext().resources.getColor(R.color.grey3))
                requestsBtnText.visibility = View.INVISIBLE
                cabinetBtnText.visibility = View.INVISIBLE
            }
            BottomTab.Profile -> {
                setAllButtonUnselected()
                viewBack.backgroundTintList = ColorStateList.valueOf(App.getContext().resources.getColor(R.color.grey3))
                requestsBtnText.visibility = View.INVISIBLE
                cabinetBtnText.visibility = View.INVISIBLE
            }

        }*/
    }
}