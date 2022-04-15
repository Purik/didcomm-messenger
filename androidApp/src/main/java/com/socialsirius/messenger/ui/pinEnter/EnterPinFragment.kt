package  com.socialsirius.messenger.ui.pinEnter

import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.databinding.FragmentEnterPinBinding
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.calculatorView.setOnDigitClickListener { model.onDigitClick(it) }
        dataBinding.calculatorView.setDeleteClickListener { model.onDeleteDigitClick() }
        dataBinding.calculatorView.setDeleteLongClickListener { model.onDeleteLongDigitClick() }
        dataBinding.calculatorView.setIdentifyClickListener { model.onIdentityClick() }
        model.countForDigit = dataBinding.indicatorView.countIndicatorAll
        dataBinding.calculatorView.enableIdentityButton(false)
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
                model.openWallet()
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