package com.socialsirius.messenger.ui.connections.connectionCard

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App

import com.socialsirius.messenger.base.ui.BaseFragment
import com.socialsirius.messenger.databinding.FragmentConnectionCardBinding
import com.socialsirius.messenger.ui.chats.chat.message.BaseItemMessage
import kotlinx.android.synthetic.main.fragment_connection_card.*

private const val CONNECTION_ITEM = "CONNECTION_ITEM"

class ConnectionCardFragment : BaseFragment<FragmentConnectionCardBinding, ConnectionCardViewModel>() {

    companion object {

        fun newInstance(item : BaseItemMessage, title : String?) : ConnectionCardFragment {
            return ConnectionCardFragment().apply {
                this.item = item
                this.title = title
            }
        }
    }
    var item : BaseItemMessage? = null
    var title : String? = null

    override fun getLayoutRes(): Int = R.layout.fragment_connection_card

    override fun setModel() {
        dataBinding!!.viewModel = model
    }

    private var adapter: ConnectionDetailsAdapter = ConnectionDetailsAdapter()

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }

    override fun setupViews() {
        model.item = item
        model.titleUser =  title
        super.setupViews()
        dataBinding.detailsRecyclerView.adapter = adapter
        dataBinding.detailsRecyclerView.isNestedScrollingEnabled = false
        try{
            scrollLayout.post {
                //  scrollLayout.fullScroll(View.FOCUS_DOWN)
                scrollLayout.scrollY = 0
            }
        }catch (e :Exception){
            e.printStackTrace()
        }
    }


    override fun subscribe() {
        model.onBackClickLiveData.observe(this, Observer {
            onBackPressed()
        })
        model.connectionDetailsLiveData.observe(this, Observer {
            adapter.dataList = it.toMutableList()
            adapter.notifyDataSetChanged()
            scrollLayout.post {
                //scrollLayout.fullScroll(View.FOCUS_DOWN)
                scrollLayout.scrollY = 0
            }
        })
        model.connectionDateLiveData.observe(this, Observer {
            topViewName.text = it
        })
        model.connectionUserLiveData.observe(this, Observer {
            connectionUserTextView.text = it
        })
        model.connectionTypeLiveData.observe(this, Observer {
            connectionTypeTextView.text = it
        })
        model.connectionStatusLiveData.observe(this, Observer {
            connectionSubheaderTextView.text = it
        })
        model.connectionDescriptionLiveData.observe(this, Observer {
            connectionDescriptionTextView.text = it
        })
        model.showDetailsLiveData.observe(this, Observer {
            if (it) detailsContainer.visibility = View.VISIBLE else View.GONE
        })
        model.errorLiveData.observe(this, Observer {

            warningTextView.visibility = View.VISIBLE
            warningTextView.text = it
        })

        model.commentLiveData.observe(this, Observer {
            commentTextView.visibility = View.VISIBLE
            commentTextView.text = it
        })
        model.showAction1LiveData.observe(this, Observer {
            if(it == null){
                actionButton1.visibility = View.GONE
            }else{
                actionButton1.visibility = View.VISIBLE
                actionButton1.text = it
            }

        })
        model.showAction2LiveData.observe(this, Observer {
            if(it == null){
                actionButton2.visibility = View.GONE
            }else{
                actionButton2.visibility = View.VISIBLE
                actionButton2.text = it
            }

        })
        model.showAction3LiveData.observe(this, Observer {
            if(it == null){
                actionButton3.visibility = View.GONE
            }else{
                actionButton3.visibility = View.VISIBLE
                actionButton3.text = it
            }

        })
        model.topViewColorLiveData.observe(this, Observer {
            topView.backgroundTintList = it
            connectionIconView.backgroundTintList = it
        })

        model.topIconLiveData.observe(this, Observer {
            connectionIconView.setImageDrawable(it)
        })

        model.topIconPaddingLiveData.observe(this, Observer {
            connectionIconView.setPadding(it,it,it,it)
        })

        model.detailsTitleLiveData.observe(this, Observer {
            detailsTitleTextView.text = it
        })

        model.action2LiveData.observe(this, Observer {
            if (it){
                model.action2LiveData.value = false
                baseActivity.pushPage(ConnectionRequestDetailFragment.newInstance(model.item))
            }

        })

    /*    model.messageStartObservLiveData.observe(this, Observer {
            model.messagesInDBLiveData.observe(this, Observer {
                if (it.action == MessageAction.UPDATE) {
                   val isMy =  model.messageCompare(it.data)
                    if (isMy){
                        model.messageStartObservLiveData.removeObservers(this)
                        model.messagesInDBLiveData.removeObservers(this)
                    }
                }

            })
        })*/



        model.messageSuccessLiveData.observe(this, Observer {
            it?.let {
                hideProgressDialog()
                Log.d("mylo890","messageSuccessLiveData=")
               // model.onShowToastLiveData.postValue(App.getContext().getString(R.string.proof_request_success))
                model.onBackClickLiveData.postValue(true)
                model.messageSuccessLiveData.value = null
            }

        })

        model.messageErrorLiveData.observe(this, Observer {
            it?.let {
                hideProgressDialog()
                Log.d("mylo890","messageErrorLiveData=")
                //model.onShowToastLiveData.postValue(App.getContext().getString(R.string.proof_request_error))
                model.onBackClickLiveData.postValue(true)
                model.errorLiveData.value = null
            }

        })
    }

}