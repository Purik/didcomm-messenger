package com.socialsirius.messenger.ui.activities.loader


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import com.sirius.library.errors.IndyException
import com.sirius.library.errors.indy_exceptions.WalletAlreadyOpenedException
import com.sirius.library.utils.ExceptionHandler
import com.sirius.library.utils.ExceptionListener
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.AppPref
import com.socialsirius.messenger.base.ui.BaseActivity
import com.socialsirius.messenger.databinding.ActivityLoaderBinding
import com.socialsirius.messenger.databinding.ActivitySplashBinding
import com.socialsirius.messenger.ui.activities.auth.AuthActivity
import com.socialsirius.messenger.ui.activities.invite.HandleWebInviteActivity
import com.socialsirius.messenger.ui.activities.main.MainActivity
import com.socialsirius.messenger.ui.activities.message.MessageActivity
import com.socialsirius.messenger.ui.pinEnter.EnterPinFragment
import java.util.concurrent.TimeoutException


class LoaderActivity : BaseActivity<ActivityLoaderBinding, LoaderActivityModel>() {


    companion object {
        @JvmStatic
        fun newInstance(context: Context, invitation: Uri?) {
            val intent = Intent(context, LoaderActivity::class.java)
            intent.data = invitation
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }


    val exceptionListener : ExceptionListener = object : ExceptionListener {
        override fun handleException(e: Throwable) {
            if(e is TimeoutException){
                //onShowAlertDialogLiveData.postValue(Pair("An error has occurred" , "Timed out waiting for a response. Please try another time."))
            }else if(e  is IndyException){

            }else {

            }
            // Log.d("mylog2090","handleException = ${e}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ExceptionHandler.removeOneTimeListener(exceptionListener)
    }
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        ExceptionHandler.addOneTimeListener(exceptionListener)
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


    fun handleIntent(): Boolean {
        if (intent?.data?.queryParameterNames?.contains("c_i") == true) {
            HandleWebInviteActivity.newInstance(this, intent?.data?.toString())
            return true
        }
        return false
    }


    override fun subscribe() {
        super.subscribe()
        model.initEndLiveData.observe(this, Observer {
            finishAffinity()
            if (!handleIntent()) {
                MainActivity.newInstance(this)
            }
        })
    }

    override fun setupViews() {
        super.setupViews()

    }

    fun initSdk() {
        model.initSdk(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        // MessageActivity.newInstance(this,Chat())
        if (AppPref.getInstance().getPin().isNullOrEmpty() && !AppPref.getInstance()
                .getUseBiometric()
        ) {
            initSdk()
        } else {
            showPage(EnterPinFragment())
        }
    }


}