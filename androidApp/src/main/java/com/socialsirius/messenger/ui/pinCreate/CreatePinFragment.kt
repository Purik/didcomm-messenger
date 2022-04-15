package  com.socialsirius.messenger.ui.pinCreate


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.databinding.FragmentCreatePinBinding
import com.socialsirius.messenger.ui.activities.loader.LoaderActivity
import com.socialsirius.messenger.ui.activities.main.MainActivity
import com.socialsirius.messenger.ui.pinEnter.BasePinFragment



class CreatePinFragment : BasePinFragment<FragmentCreatePinBinding, CreatePinViewModel>() {

    override fun getLayoutRes() = R.layout.fragment_create_pin

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }

    override fun setModel() {
        dataBinding!!.viewModel = model
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       dataBinding.calculatorView.setOnDigitClickListener { model.onDigitClick(it) }
        dataBinding. calculatorView.setDeleteClickListener { model.onDeleteDigitClick() }
        dataBinding. calculatorView.setDeleteLongClickListener { model.onDeleteLongDigitClick() }
        dataBinding.  calculatorView.setIdentifyClickListener { model.onIdentityClick() }
        model.countForDigit = dataBinding.indicatorView.countIndicatorAll
        dataBinding.  calculatorView.enableIdentityButton(false)
        dataBinding.   mainLayout.setOnClickListener(View.OnClickListener {  })
       // Utils.hideKeyboardWitoutAnimWithView(activity, rootView)
    }

    override fun subscribe() {
       model.indicatorCodeLiveData.observe(this, Observer { dataBinding.indicatorView.setupNumberFilled(it.length) })
        model.indicatorErrorLiveData.observe(this, Observer { dataBinding.indicatorView.setupError() })
        model.nextButtonClick.observe(this, Observer {
            model.createWallet()
            baseActivity.finishAffinity()
            LoaderActivity.newInstance(requireContext())
        })
        model.indicatorCodeFillLiveData.observe(this, Observer { dataBinding.nextButton.isEnabled = it })
        model.indicatorSuccessLiveData.observe(this, Observer {
            if (it) {
               // onWalletOpenListener?.OnWalletOpen()
                deletePinFragment()
            } else {
                Toast.makeText(App.getContext(), App.getContext().getString(R.string.error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deletePinFragment() {
        baseActivity.supportFragmentManager
            .beginTransaction()
            .remove(this)
            .commit()
    }
}