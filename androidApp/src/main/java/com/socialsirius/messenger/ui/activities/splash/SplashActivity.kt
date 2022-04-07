package com.socialsirius.messenger.ui.activities.splash


import android.os.Bundle
import android.os.Handler
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.AppPref
import com.socialsirius.messenger.base.ui.BaseActivity
import com.socialsirius.messenger.databinding.ActivitySplashBinding
import com.socialsirius.messenger.ui.activities.auth.AuthActivity
import com.socialsirius.messenger.ui.activities.loader.LoaderActivity
import com.socialsirius.messenger.ui.activities.main.MainActivity
import com.socialsirius.messenger.ui.activities.tutorial.TutorialActivity
import com.socialsirius.messenger.ui.tutorial.SplashFragment


class SplashActivity : BaseActivity<ActivitySplashBinding, SplashActivityModel>() {
    override fun getLayoutRes(): Int {
        return R.layout.activity_splash
    }

    override fun getRootFragmentId(): Int {
        return R.id.mainFrame
    }

    override fun isBottomNavigationEnabled(): Boolean {
        return false
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //   if (AppPref.getInstance().isLoggedIn()) {
        //      LoaderActivity.newInstance(this, false)
        //       } else {
        //showPage(SplashFragment())
        showPage(SplashFragment())
        Handler().postDelayed({
            finishAffinity()
          //  MainActivity.newInstance(this)
            TutorialActivity.newInstance(this)
        }, 1000)
        //       if(AppPref.getInstance().isTutorialDone()){

        //      }else{
        //          AuthActivity.newInstance(this)
        //       }
        //    }
        //  finish()
    }

}