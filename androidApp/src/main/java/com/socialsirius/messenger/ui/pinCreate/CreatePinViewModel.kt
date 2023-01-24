package  com.socialsirius.messenger.ui.pinCreate

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.socialsirius.messenger.base.AppPref
import com.socialsirius.messenger.base.providers.ResourcesProvider
import com.socialsirius.messenger.base.ui.BaseViewModel
import com.socialsirius.messenger.ui.activities.main.MainActivity

import javax.inject.Inject

class CreatePinViewModel @Inject constructor(
    val resourcesProvider: ResourcesProvider
 //   val pinCodeUseCase: PinCodeUseCase,
 //   val indyUseCase: IndyUseCase,
  //  val walletUseCase: WalletUseCase
    ) : BaseViewModel( ) {

    val indicatorCodeLiveData = MutableLiveData<String>()
    val indicatorCodeFillLiveData = MutableLiveData<Boolean>()
    val indicatorErrorLiveData = MutableLiveData<Boolean>()
    val indicatorSuccessLiveData = MutableLiveData<Boolean>()
    val nextButtonClick = MutableLiveData<Boolean>()
    var fromSettings : Boolean = false
    var countForDigit: Int = 0

    override fun onViewCreated() {
        super.onViewCreated()
/*
        IndyWallet.closeMyWallet();
        AppPref.getInstance().lastWalletConnectionTimestamp = System.currentTimeMillis()

       */
        indicatorCodeFillLiveData.postValue(false)
    }

    fun onDigitClick(digit: Int) {
        if (countForDigit > indicatorCodeLiveData.value?.length ?: 0) {
            indicatorCodeLiveData.value = indicatorCodeLiveData.value.orEmpty() + digit
        }
        indicatorCodeFillLiveData.value = countForDigit == indicatorCodeLiveData.value?.length ?: 0
    }


    fun protectUseBiometric(){
        AppPref.getInstance().setUseBiometric(true)
    }

    fun protectUsePinCode() {
        val mCode = indicatorCodeLiveData.value
        AppPref.getInstance().setPin(mCode)

   /*     showProgressDialog()

        Thread(Runnable {
            var isOk = false
            try {
                isOk = walletUseCase.createWallet(mCode)
                val wallet = IndyWallet.openWallet(mCode)
                IndyWallet.myWallet = wallet
                indyUseCase.restoreWallet()
                AppPref.getInstance().userCodeTryCount = 3
                App.code = mCode

            } catch (e: Exception) {
                e.printStackTrace()
            }
            indyUseCase.setUserResoursesFromWallet()
            indicatorSuccessLiveData.postValue(isOk)
            hideProgressDialog()
        }).start()*/
    }


    fun onNextButtonClick(v: View) {
        nextButtonClick.value = true
    }

    fun onIdentityClick() {
    }

    fun onDeleteLongDigitClick(): Boolean {
        if (indicatorCodeLiveData.value.isNullOrEmpty())
            return true
        indicatorCodeLiveData.value = ""
        indicatorCodeFillLiveData.value = countForDigit == indicatorCodeLiveData.value?.length ?: 0
        return true
    }

    fun onDeleteDigitClick() {
        if (indicatorCodeLiveData.value.isNullOrEmpty())
            return
        indicatorCodeLiveData.value = indicatorCodeLiveData.value?.substring(0, indicatorCodeLiveData.value!!.length - 1)
        indicatorCodeFillLiveData.value = countForDigit == indicatorCodeLiveData.value?.length ?: 0
    }
}