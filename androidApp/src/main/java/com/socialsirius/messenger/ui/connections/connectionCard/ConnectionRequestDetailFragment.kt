package com.socialsirius.messenger.ui.connections.connectionCard

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.ui.BaseFragment
import com.socialsirius.messenger.databinding.FragmentConnectionRequestDetailBinding
import com.socialsirius.messenger.ui.chats.chat.message.BaseItemMessage

import kotlinx.android.synthetic.main.fragment_connection_request_detail.*


private const val CONNECTION_ITEM = "CONNECTION_ITEM"

class ConnectionRequestDetailFragment :
    BaseFragment<FragmentConnectionRequestDetailBinding, ConnectionRequestDetailViewModel>() {

    companion object {
        @JvmStatic
        fun newInstance(item: BaseItemMessage?) = ConnectionRequestDetailFragment().apply {
            this.item = item
        }
    }

    var item: BaseItemMessage? = null

    override fun getLayoutRes(): Int = R.layout.fragment_connection_request_detail

    override fun setModel() {
        dataBinding!!.viewModel = model
    }


    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }

    override fun setupViews() {
        model.item = item
        super.setupViews()

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /*       val details =  arguments?.getSerializable(CONNECTION_ITEM) as ConnectionDetailsWrapper
               model.details = details*/
        super.onViewCreated(view, savedInstanceState)

    }

    override fun subscribe() {
        model.onBackClickLiveData.observe(this, Observer {
            onBackPressed()
        })

        model.connectionDateLiveData.observe(this, Observer {
            topViewName.text = it
        })

        model.connectionDetailsLiveData.observe(this, Observer {
            connectionDescriptionTextView.text = it
        })

        model.connectionColorLiveData.observe(this, Observer {
            topView.backgroundTintList = it
            connectionIconView.backgroundTintList = it
        })

        model.connectionImageLiveData.observe(this, Observer {
            connectionIconView.setImageDrawable(it)
        })
    }
}