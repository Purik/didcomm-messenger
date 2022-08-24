package com.socialsirius.messenger.ui.credentials

import android.view.View
import androidx.lifecycle.Observer
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.ui.BaseFragment
import com.socialsirius.messenger.base.ui.OnAdapterItemClick
import com.socialsirius.messenger.databinding.FragmentCredentialsBinding
import com.socialsirius.messenger.models.ui.ItemCredentials
import com.socialsirius.messenger.ui.activities.scan.ScanActivity
import com.socialsirius.messenger.ui.connections.connectionCard.ConnectionCardFragment

class CredentialsFragment : BaseFragment<FragmentCredentialsBinding, CredentialsViewModel>() {

    private var adapter: ConnectionsAdapter? = null

    override fun getLayoutRes(): Int {
        return R.layout.fragment_credentials
    }

    private fun updateAdapter(data: List<ItemCredentials>) {
        adapter?.dataList = data.toMutableList()
        adapter?.notifyDataSetChanged()
        if (data.isEmpty()) {
            dataBinding.emptyStateView.visibility = View.VISIBLE
        } else {
            dataBinding.emptyStateView.visibility = View.GONE
        }
    }


    override fun subscribe() {
        model.adapterListLiveData.observe(this, Observer {
            it?.let {
                updateAdapter(it)
            }
        })

        model.scanQrLiveData.observe(this, Observer {
            if(it){
                model.scanQrLiveData.value = false
                ScanActivity.newInstance(requireContext())
            }
        })

    }

    override fun setupViews() {
        super.setupViews()
        adapter = ConnectionsAdapter()
        adapter!!.onAdapterItemClick = object : OnAdapterItemClick<ItemCredentials> {
            override fun onItemClick(item: ItemCredentials) {
                baseActivity.pushPage(ConnectionCardFragment.newInstance(item,item.userName))
            }
        }
        dataBinding.chatsRecyclerView.adapter = adapter
    }

    override fun setModel() {
        dataBinding!!.viewModel = model
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }
}