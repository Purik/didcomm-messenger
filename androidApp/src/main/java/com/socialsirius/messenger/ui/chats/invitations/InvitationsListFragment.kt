package com.socialsirius.messenger.ui.chats.invitations

import androidx.lifecycle.Observer
import com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.messages.ConnRequest
import com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.messages.Invitation
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.ui.BaseFragment
import com.socialsirius.messenger.base.ui.OnAdapterItemClick
import com.socialsirius.messenger.databinding.FragmentInvitationsListBinding
import com.socialsirius.messenger.models.Chats
import com.socialsirius.messenger.ui.activities.message.MessageActivity
import com.socialsirius.messenger.ui.chats.allChats.ChatsAdapter

class InvitationsListFragment :
    BaseFragment<FragmentInvitationsListBinding, InvitationsListViewModel>() {

    private var adapter: InvitationsAdapter? = null


    override fun getLayoutRes(): Int {
        return R.layout.fragment_invitations_list
    }

    override fun setupViews() {
        super.setupViews()
        adapter = InvitationsAdapter()
        adapter!!.onAdapterItemClick = object : OnAdapterItemClick<Chats> {
            override fun onItemClick(item: Chats) {
                model.onSelectChat(item)

            }
        }
        dataBinding.invitationsList.adapter = adapter
    }

    override fun subscribe() {
        model.chatsListLiveData.observe(this, Observer {
            adapter?.dataList = it.toMutableList()
            adapter?.notifyDataSetChanged()
        })
        model.chatsSelectLiveData.observe(this, Observer { item ->
            if (item != null) {
                model.chatsSelectLiveData.value = null
                val message = item.lastMessage?.message()
                if (message is Invitation) {
                    baseActivity.model.showInvitationBottomSheetLiveData.postValue(message)
                } else if (message is ConnRequest) {
                    baseActivity.model.invitationStartLiveData.postValue(message)
                } else {
                    model.onShowToastLiveData.postValue("Please wait response or cancel connection")
                }
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