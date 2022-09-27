package com.socialsirius.messenger.ui.activities.invite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.AppPref
import com.socialsirius.messenger.base.ui.BaseActivity
import com.socialsirius.messenger.databinding.ActivityAuthBinding
import com.socialsirius.messenger.databinding.ActivityInviteBinding
import com.socialsirius.messenger.ui.auth.AuthFragment
import com.socialsirius.messenger.ui.inviteUser.InviteUserFragment
/*import com.socialsirius.messenger.ui.auth.auth_first.AuthFirstFragment
import com.socialsirius.messenger.ui.auth.auth_second_second.AuthSecondSecondFragment*/
import com.socialsirius.messenger.utils.PermissionHelper


class InviteActivity : BaseActivity<ActivityInviteBinding, InviteActivityModel>() {

    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent = Intent(context, InviteActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun isBottomNavigationEnabled(): Boolean {
        return false
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_invite
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }

    override fun getRootFragmentId(): Int {
        return R.id.mainFrameAuth
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showPage(InviteUserFragment())
    }

}