/*
package com.socialsirius.messenger.ui.scan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.google.zxing.Result
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.ui.BaseFragment
import com.socialsirius.messenger.databinding.FragmentMenuHandleInviteBinding
import com.socialsirius.messenger.ui.activities.main.MainActivity

import kotlinx.android.synthetic.main.fragment_menu_scan_qr.mScannerView

class HandleWebInviteFragment : BaseFragment<FragmentMenuHandleInviteBinding, HandleWebInviteViewModel>() {

    companion object {
        @JvmStatic
        fun newInstance(rawValue: String): HandleWebInviteFragment {
            val args = Bundle()
            args.putString("rawValue",rawValue)
            val fragment = HandleWebInviteFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun getLayoutRes(): Int = R.layout.fragment_menu_handle_invite

    override fun setModel() {

    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rawValue = arguments?.getString("rawValue")
        model.onCodeScanned(rawValue.orEmpty())
    }

    override fun subscribe() {
        model.goToNewSecretChatLiveData.observe(this, Observer {
            it?.let {
                Log.d("mylog2080", "goToNewSecretChatLiveData=" + it);
              */
/*  (baseActivity as? MainActivity)?.showChats()
                val messageIntent = Intent(context, MessageActivity::class.java).apply {
                    putExtra(StaticFields.BUNDLE_CHAT, it)
                }
                startActivity(messageIntent)
                model.goToNewSecretChatLiveData.postValue(null)*//*

            }
        })


    }

}*/
