package com.socialsirius.messenger.ui.errors

import android.os.Bundle
import androidx.lifecycle.Observer
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.ui.BaseFragment
import com.socialsirius.messenger.databinding.FragmentBaseErrorBinding
import com.socialsirius.messenger.ui.activities.loader.LoaderActivity

open class BaseErrorFragment : BaseFragment<FragmentBaseErrorBinding, BaseErrorViewModel>() {

   companion object {
       fun newInstance(exception : Throwable): BaseErrorFragment {
           val args = Bundle()
           args.putSerializable("exception", exception)
           val fragment = BaseErrorFragment()
           fragment.arguments = args
           return fragment
       }
   }



    override fun getLayoutRes(): Int {
        return R.layout.fragment_base_error
    }

    override fun setupViews() {
        model.exception = arguments?.getSerializable("exception") as? Throwable
        super.setupViews()

    }
    override fun subscribe() {
        model.closeAndOpenLiveData.observe(this, Observer {
            if(it){
                model.closeAndOpenLiveData.value = false
                baseActivity.finishAffinity()
                LoaderActivity.newInstance(baseActivity, null)
            }
        })
    }

    override fun setModel() {
        dataBinding!!.viewModel = model
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }
}