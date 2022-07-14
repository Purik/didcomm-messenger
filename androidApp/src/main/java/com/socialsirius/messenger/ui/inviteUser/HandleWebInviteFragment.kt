package com.socialsirius.messenger.ui.inviteUser

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.messages.Invitation
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.ui.BaseFragment
import com.socialsirius.messenger.databinding.FragmentMenuHandleInviteBinding
import com.socialsirius.messenger.databinding.ViewErrorBootomSheetBinding
import com.socialsirius.messenger.databinding.ViewInvitationBootomSheetBinding
import com.socialsirius.messenger.design.InvitationBottomSheet
import com.socialsirius.messenger.ui.activities.main.MainActivity


class HandleWebInviteFragment :
    BaseFragment<FragmentMenuHandleInviteBinding, HandleWebInviteViewModel>() {

    companion object {
        @JvmStatic
        fun newInstance(rawValue: String): HandleWebInviteFragment {
            val args = Bundle()
            args.putString("rawValue", rawValue)
            val fragment = HandleWebInviteFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun getLayoutRes(): Int = R.layout.fragment_menu_handle_invite

    override fun setModel() {
        dataBinding.viewModel = model
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rawValue = arguments?.getString("rawValue")
        model.onCodeScanned(rawValue.orEmpty(), 2)
    }

    override fun subscribe() {
        model.goToNewSecretChatLiveData.observe(this, Observer {
            /*      it?.let {
                      Log.d("mylog2080", "goToNewSecretChatLiveData=" + it);
                      (baseActivity as? MainActivity)?.showChats()
                      val messageIntent = Intent(context, MessageActivity::class.java).apply {
                          putExtra(StaticFields.BUNDLE_CHAT, it)
                      }
                      startActivity(messageIntent)
                      model.goToNewSecretChatLiveData.postValue(null)
                  }*/
        })



        model.invitationStartLiveData.observe(this, Observer {
            if (it != null) {
                model.invitationStartLiveData.value = null
                val builder = AlertDialog.Builder(requireContext())
            }
        })

        model.invitationErrorLiveData.observe(this, Observer {
            if (it != null) {
                model.invitationErrorLiveData.value = null
                showErrorSheet(it.second ?: "")
               // model.setError(it.second ?: "")
            }

        })

        model.invitationSuccessLiveData.observe(this, Observer {
            if (it != null) {
                model.invitationSuccessLiveData.value = null
                val item = model.getMessage(it)
                MainActivity.newInstance(requireContext(),item)
                baseActivity.finish()
            }
        })

        model.showInvitationBottomSheetLiveData.observe(this, Observer {
            it?.let {
                model.showInvitationBottomSheetLiveData.value = null
                showInvitationSheet(it)
            }
        })

        model.showErrorBottomSheetLiveData.observe(this, Observer {
            it?.let {
                model.showErrorBottomSheetLiveData.value = null
               // model.setError(it)
                showErrorSheet(it ?: "")
            }
        })
    }

 /*   fun showInvitationSheet(invitation: Invitation) {
        model.loadingVisibilityLiveData.postValue(View.GONE)
        dataBinding?.connectionLayout?.visibility = View.VISIBLE
        dataBinding?.labelText?.text = invitation.label()
        dataBinding?.receipentKeyText?.text = invitation.recipientKeys().getOrNull(0)
        dataBinding?.endpointText?.text = invitation.endpoint()
        dataBinding?.connectButton?.setOnClickListener {
            model.connectToInvitation(invitation)
            dataBinding?.connectionLayout?.visibility = View.GONE
            model.loadingVisibilityLiveData.postValue(View.VISIBLE)
        }
    }
*/
    fun showErrorSheet(text : String) {
        model.loadingVisibilityLiveData.postValue(View.GONE)
        val invitationSheet = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.view_error_bootom_sheet, null,false)
        val binding =  DataBindingUtil.bind<ViewErrorBootomSheetBinding>(view)
        binding?.errorText?.text = text
        binding?.connectButton?.setOnClickListener {
            invitationSheet.dismiss()
        }
        invitationSheet.setContentView(view)
        invitationSheet.show()
        invitationSheet.setOnDismissListener {
            baseActivity.finish()
        }
    }


    fun showInvitationSheet(invitation : Invitation) {
        model.loadingVisibilityLiveData.postValue(View.GONE)
        val invitationSheet = InvitationBottomSheet(requireContext())
        val view = layoutInflater.inflate(R.layout.view_invitation_bootom_sheet, null,false)
        val binding =  DataBindingUtil.bind<ViewInvitationBootomSheetBinding>(view)
        binding?.labelText?.text = invitation.label()
        binding?.receipentKeyText?.text = invitation.recipientKeys().getOrNull(0)
        binding?.endpointText?.text = invitation.endpoint()

        binding?.connectButton?.setOnClickListener {
            model.loadingVisibilityLiveData.postValue(View.VISIBLE)
            model.connectToInvitation(invitation)
            invitationSheet.dismiss()
        }

        invitationSheet.setContentView(view)
        invitationSheet.show()

        invitationSheet.setOnDismissListener {
            if(!model.isConnecting){
                baseActivity.finish()
            }
        }
    }
}