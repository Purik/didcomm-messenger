/*
package com.socialsirius.messenger.base.ui

import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.socialsirius.messenger.design.BottomNavView.BottomTab
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

abstract class BaseActivity<VB : ViewDataBinding, M : BaseActivityModel> :
    AppCompatActivity() {
    @Inject
    public var viewModelFactory: ViewModelProvider.Factory? = null
    var model: M? = null
    var isBack = false
    var dataBinding: VB? = null
    val isUseDataBinding: Boolean
        get() = true
    val isUseDefault: Boolean
        get() = true

    @get:LayoutRes
    abstract val layoutRes: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isUseDataBinding) {
            dataBinding = DataBindingUtil.setContentView(this, layoutRes)
            dataBinding!!.lifecycleOwner = this
            //  model = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()).create(M);
        } else if (isUseDefault) {
            setContentView(layoutRes)
        }
        initDagger()
        initViewModel()
        setupViews()
        subscribe()
    }

    //FRAGMENTS
    @get:IdRes
    val rootFragmentId: Int
        get() = 0

    @JvmOverloads
    fun pushPage(page: BaseFragment<*, *>, withAnimation: Boolean = false) {
        require(rootFragmentId != 0) { "Declare geRootFragmentId() to use this method" }
        page.isBack = true
        isBack = true
        val ft = supportFragmentManager.beginTransaction()
        if (withAnimation) {
            //  ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        }
        ft.addToBackStack(null).replace(rootFragmentId, page).commitAllowingStateLoss()
    }

    fun pushPage(page: BaseFragment<*, *>, withAnimation: Boolean, isBack: Boolean) {
        require(rootFragmentId != 0) { "Declare geRootFragmentId() to use this method" }
        page.isBack = isBack
        this.isBack = isBack
        val ft = supportFragmentManager.beginTransaction()
        if (withAnimation) {
            //  ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        }
        ft.addToBackStack(null).replace(rootFragmentId, page).commitAllowingStateLoss()
    }

    @JvmOverloads
    fun pushPageAdd(page: BaseFragment<*, *>, withAnimation: Boolean = false) {
        require(rootFragmentId != 0) { "Declare geRootFragmentId() to use this method" }
        page.isBack = true
        isBack = true
        val ft = supportFragmentManager.beginTransaction()
        if (withAnimation) {
            // ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        }
        ft.addToBackStack(null).add(rootFragmentId, page).commit()
    }

    @JvmOverloads
    fun showPage(
        page: BaseFragment<*, *>,
        bundle: Bundle? = null,
        enterAnimations: Int = 0,
        exitAnimations: Int = 0
    ) {
        require(rootFragmentId != 0) { "Declare geRootFragmentId() to use this method" }
        try {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        page.isBack = false
        isBack = false
        val transaction = supportFragmentManager.beginTransaction()
        if (enterAnimations != 0 && exitAnimations != 0) {
            transaction.setCustomAnimations(enterAnimations, exitAnimations)
        }
        transaction.replace(rootFragmentId, page).commitAllowingStateLoss()
    }

    fun showPage(page: BaseFragment<*, *>, isBack: Boolean) {
        require(rootFragmentId != 0) { "Declare geRootFragmentId() to use this method" }
        try {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        page.isBack = isBack
        this.isBack = isBack
        supportFragmentManager.beginTransaction().replace(rootFragmentId, page)
            .commitAllowingStateLoss()
    }

    fun popPage(page: BaseFragment<*, *>) {
        try {
            supportFragmentManager.popBackStackImmediate()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        pushPage(page)
        // page.setBack(false);
        // page.setPageManager(getPageManager());
        //getSupportFragmentManager().beginTransaction().replace(getRootFragmentId(), page).commit();
    }

    fun isBackFragments(): Boolean {
        val listFragment = supportFragmentManager.fragments
        return if (listFragment != null) {
            if (listFragment.size > 0) {
                true
            } else {
                false
            }
        } else {
            false
        }
    }

    fun popPage() {
        onBackPressed()
    }

    //Send  permisions, result  inside Fragments(NEED or NO?)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        val listFragment = supportFragmentManager.fragments
        if (listFragment != null) {
            for (fragmnet in listFragment) {
                fragmnet.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val listFragment = supportFragmentManager.fragments
        if (listFragment != null) {
            for (fragmnet in listFragment) {
                fragmnet.onActivityResult(requestCode, resultCode, data)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun onFragmentResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val listFragment = supportFragmentManager.fragments
        if (listFragment != null) {
            for (fragmnet in listFragment) {
                if (fragmnet is BaseFragment<*, *>) {
                    fragmnet.onFragmentResult(requestCode, resultCode, data)
                }
            }
        }
    }

    var isCustomBackEnabled = false
    var customBackListener: CustomBackListener? = null

    interface CustomBackListener {
        fun onBackPressed()
    }

    fun onBack() {
        if (isCustomBackEnabled) {
            if (customBackListener != null) {
                customBackListener!!.onBackPressed()
                return
            }
        }
        try {
            supportFragmentManager.fragments[supportFragmentManager.backStackEntryCount - 1].onResume()
        } catch (e: Exception) {
        }
        super.onBackPressed()
    }

    override fun onBackPressed() {
        onBack()
    }

    private fun initViewModel() {
        val vmClass =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<M>
        model = ViewModelProvider(this, viewModelFactory!!).get(vmClass)
        model!!.onViewCreated()
    }

    var isBottomNavigationEnabled = true

    fun subscribe() {
        if (!isBottomNavigationEnabled) {
            return
        }
        model!!.invitationStartLiveData.observe(
            this
        ) { }
        model!!.bottomNavClick.observe(
            this
        ) { bottomTab ->
            when (bottomTab) {
                BottomTab.Contacts -> {}
                BottomTab.Menu -> {}
                BottomTab.Credentials -> {}
                else -> {}
            }
        }
    }

    fun openTab(tab: BottomTab?) {
        model!!.selectedTab.postValue(tab)
    }

    abstract fun initDagger()
    public override fun onDestroy() {
        model!!.onDestroy()
        super.onDestroy()
    }

    public override fun onResume() {
        super.onResume()
        model!!.onResume()
    }

    fun setupViews() {
        if (model != null) {
            model!!.setupViews()
        }
    }
}*/
