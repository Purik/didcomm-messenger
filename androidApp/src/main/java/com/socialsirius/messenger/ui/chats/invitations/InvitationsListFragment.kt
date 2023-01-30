package com.socialsirius.messenger.ui.chats.invitations

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.messages.ConnRequest
import com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.messages.Invitation
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.ui.BaseFragment
import com.socialsirius.messenger.base.ui.OnAdapterItemClick
import com.socialsirius.messenger.base.ui.OnCustomBtnClick
import com.socialsirius.messenger.databinding.FragmentInvitationsListBinding
import com.socialsirius.messenger.databinding.ViewInvitationBootomSheetBinding
import com.socialsirius.messenger.design.InvitationBottomSheet
import com.socialsirius.messenger.models.Chats

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
        adapter!!.onCustomBtnClick = object : OnCustomBtnClick<Chats> {


            override fun onBtnClick(btnId: Int, item: Chats?, position: Int) {
                model.readUnread(item)
            }

            override fun onLongBtnClick(btnId: Int, item: Chats?, position: Int) {

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
                    model.showInvitationBottomSheetLiveData.postValue(message)
                } else if (message is ConnRequest) {
                    baseActivity.model.invitationStartLiveData.postValue(message)
                } else {
                    model.onShowToastLiveData.postValue("Please wait response or cancel connection")
                }
            }

        })

        model.showInvitationBottomSheetLiveData.observe(this,
            Observer<Invitation?> { invitation ->
                if (invitation != null) {
                    // mScannerView ?.stopCameraPreview()
                    model.showInvitationBottomSheetLiveData.setValue(null)
                    showInvitationSheet(invitation)
                }
            })

        baseActivity.model.invitationSheetDismissLiveData.observe(this, Observer {
            if (it) {
                baseActivity.model.invitationSheetDismissLiveData.value = false
                model.createList()
            }

        })
    }

    fun showInvitationSheet(invitation: Invitation) {
        val invitationSheet = InvitationBottomSheet(requireContext())
        //  currentBottomSheet = invitationSheet
        val view = layoutInflater.inflate(R.layout.view_invitation_bootom_sheet, null, false)
        val binding: ViewInvitationBootomSheetBinding? = DataBindingUtil.bind(view)
        binding?.labelText?.setText(invitation.label())
        //   binding?.invitationTitle?.text = "I"
        binding?.receipentLabel?.setText(
            String.format(
                App.getContext().getString(R.string.recipient_keys), invitation.label()
            )
        )
        if (invitation.recipientKeys().size > 0) {
            binding?.receipentKeyText?.setText(invitation.recipientKeys()[0])
        }
        binding?.deleteBtn?.setVisibility(View.GONE)

        //   if (invitation.endpoint() == model.getMyEndpoint()) {
        binding?.connectButton?.setVisibility(View.GONE)
        //}
        binding?.connectButton?.setOnClickListener(View.OnClickListener {
            invitationSheet.dismiss()
            // model.connectToInvitation(invitation)
        })
        binding?.moreBtn?.setOnClickListener(
            View.OnClickListener {
                binding?.receipentLabel?.setVisibility(View.VISIBLE)
                binding?.receipentKeyText?.setVisibility(View.VISIBLE)
                //    binding?.endpointLabel?.visibility = View.VISIBLE
                //    binding?.endpointText?.visibility = View.VISIBLE
                binding?.moreBtn.setVisibility(View.GONE)
            }
        )
        invitationSheet.setContentView(view)
        invitationSheet.show()
        //invitationSheet.setOnDismissListener { model.invitationSheetDismissLiveData.postValue(true) }
    }

    override fun setModel() {
        dataBinding!!.viewModel = model
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }
}