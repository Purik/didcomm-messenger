package  com.socialsirius.messenger.ui.pinEnter

import android.os.Handler
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.socialsirius.messenger.base.providers.ResourcesProvider
import com.socialsirius.messenger.base.ui.BaseViewModel

import javax.inject.Inject

class EnterPinViewModel @Inject constructor(
    val resourcesProvider: ResourcesProvider,
/*    val pinCodeUseCase: PinCodeUseCase,
    val indyUseCase: IndyUseCase,
    val loginUseCase: LoginUseCase*/
) : BaseViewModel( ) {

    val indicatorCodeLiveData = MutableLiveData<String>()
    val indicatorCodeFillLiveData = MutableLiveData<Boolean>()
    val indicatorErrorLiveData = MutableLiveData<Boolean>()
    val attemptsCountLiveData = MutableLiveData<String>()
    val indicatorSuccesLiveData = MutableLiveData<Boolean>()
    val hintTextLiveData = MutableLiveData<String>()
    val exitButtonClick = MutableLiveData<Boolean>()
    var countForDigit: Int = 0

    override fun onViewCreated() {
        super.onViewCreated()
     /*   IndyWallet.closeMyWallet();
        AppPref.getInstance().lastWalletConnectionTimestamp = System.currentTimeMillis()
        val count = AppPref.getInstance().userCodeTryCount
        attemptsCountLiveData.value = resourceProvider.getString(R.string.lock_screen_title_try_count_pf) + " " + count*/
    }

    fun onDigitClick(digit: Int) {
      /*  if (countForDigit > indicatorCodeLiveData.value?.length ?: 0) {
            indicatorCodeLiveData.value = indicatorCodeLiveData.value.orEmpty() + digit
        }
        if (countForDigit == indicatorCodeLiveData.value?.length ?: 0) {
            indicatorCodeFillLiveData.value = true
        }*/
    }

    fun onSuccess() {
   /*     indyUseCase.setUserResoursesFromWallet()
        indicatorSuccesLiveData.postValue(true)*/
    }

    fun onFail() {
     /*   hintTextLiveData.value = (resourceProvider.getString(R.string.pin_code_entered_error))
        val countMinus = AppPref.getInstance().userCodeTryCount - 1
        if (countMinus < 0) {
            AppPref.getInstance().userCodeTryCount = 0
        } else {
            AppPref.getInstance().userCodeTryCount = countMinus
        }
        attemptsCountLiveData.value = (resourceProvider.getString(R.string.lock_screen_title_try_count_pf) + " " + AppPref.getInstance().userCodeTryCount)
        indicatorErrorLiveData.value = true
        if (AppPref.getInstance().userCodeTryCount <= 0) {
            deleteWallet()
            AppPref.getInstance().userCodeTryCount = 3
        }*/
    }


    private fun deleteWallet() {
        //  finishAffinity()
     /*   IndyWallet.deleteWallet()
        logout(true)*/
    }


    fun logout(forceLogout : Boolean){
   /*     showProgressDialog()
        loginUseCase.logout(forceLogout)*/
    }

    fun onExitButtonClick(v: View) {
        exitButtonClick.value = true
    }

    fun onIdentityClick() {
    }

    fun onDeleteLongDigitClick(): Boolean {
        if (indicatorCodeLiveData.value.isNullOrEmpty())
            return true
        indicatorCodeLiveData.value = ""
        return true
    }

    fun onDeleteDigitClick() {
        if (indicatorCodeLiveData.value.isNullOrEmpty())
            return
        indicatorCodeLiveData.value = indicatorCodeLiveData.value?.substring(0, indicatorCodeLiveData.value!!.length - 1)
    }

    fun openWallet() {
      /*  val mCode = indicatorCodeLiveData.value
        showProgressDialog()
        val handler = Handler()
        Thread(Runnable {
            var isCorrect = false
            var wallet: Wallet? = null
            try {
                IndyWallet.closeMyWallet()
                wallet = IndyWallet.openWallet(mCode)
                if (wallet != null) {
                    App.code = mCode
                    IndyWallet.myWallet = wallet
                    isCorrect = true
                    AppPref.getInstance().userCodeTryCount = 3
                }
            } catch (e1: WalletAlreadyOpenedException) {
                isCorrect = true
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

            handler.post(Runnable {
                hideProgressDialog()
                if (isCorrect) {
                    onSuccess()
                } else {
                    onFail()
                }
                indicatorCodeFillLiveData.postValue(false)
            })
        }).start()*/
    }
}