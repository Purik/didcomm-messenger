package com.socialsirius.messenger.ui.activities.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.ui.BaseActivity
import com.socialsirius.messenger.databinding.ActivitySimpleBinding

class SimpleActivity : BaseActivity<ActivitySimpleBinding, SimpleActivityModel>() {

    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent = Intent(context, SimpleActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun isBottomNavigationEnabled(): Boolean {
        return false
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_simple
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }

    override fun getRootFragmentId(): Int {
        return R.id.mainFrameAuth
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  showPage(AuthFragment())
    }




    override fun subscribe() {
        super.subscribe()

    }


}