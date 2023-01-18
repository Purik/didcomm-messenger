package com.socialsirius.messenger.ui.errors

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.sirius.library.errors.IndyException
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.ui.BaseViewModel
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class BaseErrorViewModel  @Inject constructor() : BaseViewModel() {
   val errorTextLiveData =  MutableLiveData<String>("")
    val closeAndOpenLiveData =  MutableLiveData<Boolean>()
   val tryAgainBtnTextLiveData =  MutableLiveData<String>(App.getContext().resources.getString(R.string.try_again_btn))
    var exception : Throwable? = null
    fun onTryAgainBtnClick(v : View){
        closeAndOpenLiveData.postValue(true)
    }

    override fun setupViews() {
        super.setupViews()
        if(exception is TimeoutException){
            errorTextLiveData.postValue("Timed out waiting for a response. Please try another time.")
            //onShowAlertDialogLiveData.postValue(Pair("An error has occurred" , "Timed out waiting for a response. Please try another time."))
        }else if(exception  is IndyException){
            errorTextLiveData.postValue(exception?.message)
        }else {
            errorTextLiveData.postValue("Unknown error has occurred. Please contact with technical support. " +
                    "Information about error:\n${exception?.message}")
        }
    }
}