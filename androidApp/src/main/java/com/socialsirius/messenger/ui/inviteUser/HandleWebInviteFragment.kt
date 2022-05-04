package com.socialsirius.messenger.ui.inviteUser

import android.app.AlertDialog
import android.app.TaskStackBuilder
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.ui.BaseFragment
import com.socialsirius.messenger.databinding.FragmentMenuHandleInviteBinding
import com.socialsirius.messenger.ui.activities.main.MainActivity
import com.socialsirius.messenger.ui.activities.message.MessageActivity


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
        model.onCodeScanned(rawValue.orEmpty())
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
                model.setError(it.second)
            }

        })

        model.invitationSuccessLiveData.observe(this, Observer {
            if (it != null) {
                model.invitationSuccessLiveData.value = null
                val item = model.getMessage(it)
                val intent = MessageActivity.createIntent(requireContext(), item)
                TaskStackBuilder.create(requireContext())
                    .addNextIntentWithParentStack(intent)
                    .startActivities();

                baseActivity.finish()

            }
        })
    }

}