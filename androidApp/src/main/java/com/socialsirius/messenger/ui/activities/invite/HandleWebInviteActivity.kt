package com.socialsirius.messenger.ui.activities.invite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.ui.BaseActivity
import com.socialsirius.messenger.databinding.ActivityBaseBinding
import com.socialsirius.messenger.ui.activities.main.MainActivity
import com.socialsirius.messenger.ui.inviteUser.HandleWebInviteFragment
import com.socialsirius.messenger.ui.inviteUser.HandleWebInviteFragment.Companion.newInstance

class HandleWebInviteActivity :
    BaseActivity<ActivityBaseBinding, HandleWebInviteActivityModel>() {

    companion object {
        @JvmStatic
        fun newInstance(context: Context, invitation : String?) {
            val intent = Intent(context, HandleWebInviteActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("invitation",invitation)
            context.startActivity(intent)
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_base
    }

    override fun getRootFragmentId(): Int {
        return R.id.mainFrame
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rawvalue = intent.getStringExtra("invitation") ?:""
        showPage(HandleWebInviteFragment.newInstance(rawvalue))
    }
}
