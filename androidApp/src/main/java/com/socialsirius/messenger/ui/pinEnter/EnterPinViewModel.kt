package  com.socialsirius.messenger.ui.pinEnter

import android.os.Handler
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.sirius.library.mobile.helpers.WalletHelper
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.AppPref
import com.socialsirius.messenger.base.providers.ResourcesProvider
import com.socialsirius.messenger.base.ui.BaseViewModel
import com.socialsirius.messenger.repository.UserRepository
import com.socialsirius.messenger.sirius_sdk_impl.SDKUseCase
import com.socialsirius.messenger.ui.activities.auth.AuthActivity

import javax.inject.Inject

class EnterPinViewModel @Inject constructor(
    val resourceProvider: ResourcesProvider,
    val userRepository: UserRepository,
     val sdkUseCase: SDKUseCase
/*    val pinCodeUseCase: PinCodeUseCase,
    val indyUseCase: IndyUseCase,
    val loginUseCase: LoginUseCase*/
) : BaseViewModel() {

    val indicatorCodeLiveData = MutableLiveData<String>()
    val indicatorCodeFillLiveData = MutableLiveData<Boolean>()
    val indicatorErrorLiveData = MutableLiveData<Boolean>()
    val attemptsCountLiveData = MutableLiveData<String>()
    val indicatorSuccesLiveData = MutableLiveData<Boolean>()
    val hintTextLiveData = MutableLiveData<String>()
    val exitButtonClick = MutableLiveData<Boolean>()
    val gotoAuthActivity = MutableLiveData<Boolean>()
    var countForDigit: Int = 0

    override fun onViewCreated() {
        super.onViewCreated()
        //   IndyWallet.closeMyWallet();
        //   AppPref.getInstance().lastWalletConnectionTimestamp = System.currentTimeMillis()
           val count = AppPref.getInstance().getUserCodeTryCount()
           attemptsCountLiveData.value = resourceProvider.getString(R.string.lock_screen_title_try_count_pf) + " " + count
    }

    fun onDigitClick(digit: Int) {
        if (countForDigit > indicatorCodeLiveData.value?.length ?: 0) {
            indicatorCodeLiveData.value = indicatorCodeLiveData.value.orEmpty() + digit
        }
        if (countForDigit == indicatorCodeLiveData.value?.length ?: 0) {
            indicatorCodeFillLiveData.value = true
        }
    }

    fun onSuccess() {
        AppPref.getInstance().setUserCodeTryCount(3)
        indicatorSuccesLiveData.postValue(true)
        /*     indyUseCase.setUserResoursesFromWallet()
             indicatorSuccesLiveData.postValue(true)*/
    }

    fun onFail() {
        indicatorErrorLiveData.value = true
        hintTextLiveData.value = (resourceProvider.getString(R.string.pin_code_entered_error))
         val countMinus = AppPref.getInstance().getUserCodeTryCount() - 1
         if (countMinus < 0) {
             AppPref.getInstance().setUserCodeTryCount( 0)
         } else {
             AppPref.getInstance().setUserCodeTryCount(countMinus)
         }
         attemptsCountLiveData.value = (resourceProvider.getString(R.string.lock_screen_title_try_count_pf) + " " + AppPref.getInstance().getUserCodeTryCount())
         indicatorErrorLiveData.value = true
         if (AppPref.getInstance().getUserCodeTryCount() <= 0) {
             logout(true)
             AppPref.getInstance().setUserCodeTryCount(3)
             gotoAuthActivity.postValue(true)

         }
    }


    private fun deleteWallet() {
        //  finishAffinity()
        /*   IndyWallet.deleteWallet()
           logout(true)*/
    }


    fun logout(forceLogout: Boolean) {
        userRepository.logout()

        sdkUseCase.logoutFromSDK()

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
        indicatorCodeLiveData.value =
            indicatorCodeLiveData.value?.substring(0, indicatorCodeLiveData.value!!.length - 1)
    }

    fun openWallet(fromPin: Boolean, isSuccessFromBiometric: Boolean) {
        if (fromPin) {
            val mCode = indicatorCodeLiveData.value
            if (AppPref.getInstance().getPin() == mCode) {
                onSuccess()
            } else {
                onFail()
            }
        } else {
            if (isSuccessFromBiometric) {
                onSuccess()
            } else {
              //  onFail()
            }
        }

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

    fun protectUseBiometric() {
        AppPref.getInstance().setUseBiometric(true)
    }
}