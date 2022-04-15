package com.socialsirius.messenger.ui.activities.loader


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.Observer
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.AppPref
import com.socialsirius.messenger.base.ui.BaseActivity
import com.socialsirius.messenger.databinding.ActivityLoaderBinding
import com.socialsirius.messenger.databinding.ActivitySplashBinding
import com.socialsirius.messenger.ui.activities.auth.AuthActivity
import com.socialsirius.messenger.ui.activities.main.MainActivity
import com.socialsirius.messenger.ui.pinEnter.EnterPinFragment


class LoaderActivity : BaseActivity<ActivityLoaderBinding, LoaderActivityModel>() {


    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent = Intent(context, LoaderActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun getRootFragmentId(): Int {
        return R.id.mainLayout
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_loader
    }

    override fun isBottomNavigationEnabled(): Boolean {
        return false
    }
    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }



    override fun subscribe() {
        super.subscribe()
        model.initEndLiveData.observe(this, Observer {
            finishAffinity()
            MainActivity.newInstance(this)
        })
    }

    override fun setupViews() {
        super.setupViews()

    }

    fun initSdk(){
        model.initSdk(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(AppPref.getInstance().getPin().isNullOrEmpty()){
            initSdk()
        }else{
            showPage(EnterPinFragment())
        }
    }


}