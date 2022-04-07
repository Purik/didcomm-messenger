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


class EnterPinFragment : BasePinFragment<FragmentEnterPinBinding, EnterPinViewModel>() {

    override fun getLayoutRes() = R.layout.fragment_enter_pin

    override fun initDagger() {
     //   App.getInstance().appComponent.inject(this)
    }

    override fun setModel() {
       // dataBinding!!.viewModel = model
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    /*    calculatorView.setOnDigitClickListener { model.onDigitClick(it) }
        calculatorView.setDeleteClickListener { model.onDeleteDigitClick() }
        calculatorView.setDeleteLongClickListener { model.onDeleteLongDigitClick() }
        calculatorView.setIdentifyClickListener { model.onIdentityClick() }
        model.countForDigit = indicatorView.countIndicatorAll
        calculatorView.enableIdentityButton(false)
        mainLayout.setOnClickListener(View.OnClickListener {  })
        Utils.hideKeyboardWitoutAnimWithView(activity, rootView)*/
    }

    override fun subscribe() {
   /*     model.indicatorCodeLiveData.observe(this, Observer { indicatorView.setupNumberFilled(it.length) })
        model.indicatorErrorLiveData.observe(this, Observer { indicatorView.setupError() })
        model.hintTextLiveData.observe(this, Observer {
            hintTextView.setTextColor(App.getContext().resources.getColor(R.color.errorColor))
            hintTextView.text = it
        })

        model.exitButtonClick.observe(this, Observer {
            try {
                val builder = AlertDialog.Builder(baseActivity)
                builder.setTitle(App.getContext().getString(R.string.user_logout_title))
                builder.setMessage(App.getContext().getString(R.string.user_logout_are_you_sure))
                builder.setPositiveButton(App.getContext().getString(R.string.yes)) { dialog, which ->
                    deletePinFragment()
                    model.logout(false)
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
            onWalletOpenListener?.OnWalletOpen()
        })
        model.indicatorErrorLiveData.observe(this, Observer {
            errorAction()
            model.onDeleteLongDigitClick()
        })
        model.attemptsCountLiveData.observe(this, Observer {
            attemptsTextView.text = it.toString()
        })*/
    }

    private fun errorAction() {
  /*      (baseActivity.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator)?.vibrate(400)
        val animShake = AnimationUtils.loadAnimation(context, R.anim.shake_pf)
        indicatorView.startAnimation(animShake)
*/
    }

    private fun deletePinFragment() {
        baseActivity.supportFragmentManager
            .beginTransaction()
            .remove(this)
            .commit()
    }
}