package  com.socialsirius.messenger.ui.pinEnter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.AppPref
import com.socialsirius.messenger.databinding.FragmentEnterPinBinding
import com.socialsirius.messenger.service.BiometricPromptUtils.createBiometricPrompt
import com.socialsirius.messenger.ui.activities.auth.AuthActivity
import com.socialsirius.messenger.ui.activities.loader.LoaderActivity


class EnterPinFragment : BasePinFragment<FragmentEnterPinBinding, EnterPinViewModel>() {

    override fun getLayoutRes() = R.layout.fragment_enter_pin

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }

    override fun setModel() {
        dataBinding!!.viewModel = model
    }

    fun openFingerprintDialog() {


        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                val promptInfo = createPromptInfo()
                biometricPrompt.authenticate(promptInfo)
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                model.onShowToastLiveData.postValue("No biometric features available on this device.")
                // Log.e("MY_APP_TAG", "No biometric features available on this device.")
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                model.onShowToastLiveData.postValue("Biometric features are currently unavailable.")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credentials that your app accepts.
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BiometricManager.Authenticators.BIOMETRIC_WEAK
                    )
                }
                startActivityForResult(enrollIntent, 1200)
            }
        }

    }

    private fun createPromptInfo(): BiometricPrompt.PromptInfo {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.prompt_info_title))
            .setSubtitle(getString(R.string.prompt_info_subtitle))
            .setDescription(getString(R.string.prompt_info_description))
            // Authenticate without requiring the user to press a "confirm"
            // button after satisfying the biometric check
            .setConfirmationRequired(false)
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK)
            .setNegativeButtonText(getString(R.string.prompt_info_use_app_password))
            .build()
        return promptInfo
    }

    private fun createBiometricPrompt(): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(requireContext())

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.d("TAG", "$errorCode :: $errString")
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    model.onShowToastLiveData.postValue("Use Pin if you do not want use biometric")
                    // loginWithPassword() // Because in this app, the negative button allows the user to enter an account password. This is completely optional and your app doesn’t have to do it.
                }else{
                    model.openWallet(false, false)
                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                model.openWallet(false, false)
            //    model.onShowToastLiveData.postValue("Authentication failed for an unknown reason")
                //   Log.d("TAG", "Authentication failed for an unknown reason")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                model.protectUseBiometric()
                model.openWallet(false, true)
             //   showAdditionalPinCodeAlert()
                Log.d("TAG", "Authentication was successful")
                // Proceed with viewing the private encrypted message.
                //  showEncryptedMessage(result.cryptoObject)
            }
        }

        val biometricPrompt = BiometricPrompt(this, executor, callback)
        return biometricPrompt
    }


    fun showAdditionalPinCodeAlert() {
        val builder = AlertDialog.Builder(baseActivity)
        //   builder.setTitle(App.getContext().getString(R.string.user_logout_title))
        builder.setMessage(App.getContext().getString(R.string.create_pin_additional))
        builder.setPositiveButton(
            App.getContext().getString(R.string.yes), null
        )
        builder.setNegativeButton(App.getContext().getString(R.string.no), { dialog, which ->
            baseActivity.finishAffinity()
            LoaderActivity.newInstance(requireContext(), null)
        })
        builder.show()
    }

    private lateinit var biometricPrompt: BiometricPrompt

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        biometricPrompt = createBiometricPrompt()
        dataBinding.calculatorView.setOnDigitClickListener { model.onDigitClick(it) }
        dataBinding.calculatorView.setDeleteClickListener { model.onDeleteDigitClick() }
        dataBinding.calculatorView.setDeleteLongClickListener { model.onDeleteLongDigitClick() }
        dataBinding.calculatorView.setIdentifyClickListener { model.onIdentityClick() }
        model.countForDigit = dataBinding.indicatorView.countIndicatorAll
        dataBinding.calculatorView.enableAllButtons(!AppPref.getInstance().getPin().isNullOrEmpty())
        dataBinding.calculatorView.enableIdentityButton(true)
        dataBinding.calculatorView.setIdentifyClickListener {
            openFingerprintDialog()
            // Allows user to authenticate using either a Class 3 biometric or
// their lock screen credential (PIN, pattern, or password).

        }
        dataBinding.mainLayout.setOnClickListener(View.OnClickListener { })

        //  Utils.hideKeyboardWitoutAnimWithView(activity, rootView)
    }

    override fun subscribe() {
        model.indicatorCodeLiveData.observe(
            this,
            Observer { dataBinding.indicatorView.setupNumberFilled(it.length) })
        model.indicatorErrorLiveData.observe(
            this,
            Observer { dataBinding.indicatorView.setupError() })
        model.hintTextLiveData.observe(this, Observer {
            dataBinding.hintTextView.setTextColor(App.getContext().resources.getColor(R.color.errorColor))
            dataBinding.hintTextView.text = it
        })

        model.exitButtonClick.observe(this, Observer {
            try {
                val builder = AlertDialog.Builder(baseActivity)
                builder.setTitle(App.getContext().getString(R.string.user_logout_title))
                builder.setMessage(App.getContext().getString(R.string.user_logout_are_you_sure))
                builder.setPositiveButton(
                    App.getContext().getString(R.string.yes)
                ) { dialog, which ->
                    //   deletePinFragment()
                    model.logout(false)
                    baseActivity.finishAffinity()
                    AuthActivity.newInstance(requireContext())
                }
                builder.setNegativeButton(App.getContext().getString(R.string.no), null)
                builder.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        model.indicatorCodeFillLiveData.observe(this, Observer {
            if (it) {
                model.indicatorCodeFillLiveData.value = false
                model.openWallet(true, false)
            }
        })

        model.indicatorSuccesLiveData.observe(this, Observer {
            deletePinFragment()
            goToLoader()
            onWalletOpenListener?.OnWalletOpen()
        })
        model.indicatorErrorLiveData.observe(this, Observer {
            errorAction()
            model.onDeleteLongDigitClick()
        })
        model.attemptsCountLiveData.observe(this, Observer {
            dataBinding.attemptsTextView.text = it.toString()
        })
    }

    private fun errorAction() {
        (baseActivity.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator)?.vibrate(400)
        val animShake = AnimationUtils.loadAnimation(context, R.anim.shake_pf)
        dataBinding.indicatorView.startAnimation(animShake)

    }


    private fun goToLoader() {
        if (baseActivity is LoaderActivity) {
            (baseActivity as LoaderActivity).initSdk()
        }
    }


    private fun deletePinFragment() {
        baseActivity.supportFragmentManager
            .beginTransaction()
            .remove(this)
            .commit()

    }
}