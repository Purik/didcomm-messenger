package  com.socialsirius.messenger.ui.chats.userProfile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.ui.BaseFragment
import com.socialsirius.messenger.databinding.FragmentUserProfileBinding

import kotlinx.android.synthetic.main.fragment_user_profile.avatarView
import kotlinx.android.synthetic.main.fragment_user_profile.nameView
import kotlinx.android.synthetic.main.fragment_user_profile.nicknameView
import kotlinx.android.synthetic.main.fragment_user_profile.phoneView

const val USER = "USER"

class UserProfileFragment : BaseFragment<FragmentUserProfileBinding, UserProfileViewModel>() {

    companion object {
       /* @JvmStatic
        fun newInstance(user: RosterUser) = UserProfileFragment().apply {
            arguments = Bundle().apply {
                putSerializable(USER, user)
            }
        }*/
    }

    override fun getLayoutRes() = R.layout.fragment_user_profile

    override fun initDagger() {
       // App.getInstance().appComponent.inject(this)
    }

    override fun setModel() {
        //dataBinding!!.viewModel = model
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       // model.setUser(arguments?.getSerializable(USER) as RosterUser)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun subscribe() {
       /* model.onBackClickLiveData.observe(this, Observer { baseActivity.popPage() })
        model.nameLiveData.observe(this, Observer {
            nameView.text = it
        })
        model.phoneLiveData.observe(this, Observer {
            phoneView.text = it
        })
        model.nicknameLiveData.observe(this, Observer {
            nicknameView.text = it
        })
        model.avatarLiveData.observe(this, Observer {
            avatarView.update(it)
        })
        model.chatClickLiveData.observe(this, Observer {
            //todo ?
        })*/
    }
}