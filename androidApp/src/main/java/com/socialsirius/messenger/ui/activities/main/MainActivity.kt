package com.socialsirius.messenger.ui.activities.main

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import com.google.android.material.navigation.NavigationView
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.AppPref
import com.socialsirius.messenger.base.ui.BaseActivity
import com.socialsirius.messenger.databinding.ActivityMainBinding
import com.socialsirius.messenger.ui.chats.allChats.AllChatsFragment
import com.socialsirius.messenger.ui.inviteUser.InviteUserFragment
import com.socialsirius.messenger.ui.scan.MenuScanQrFragment
import com.socialsirius.messenger.ui.userSettings.UserSettingsFragment


class MainActivity : BaseActivity<ActivityMainBinding, MainActivityModel>() {

    companion object {
        @JvmStatic
        fun newInstance(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_main
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }

    override fun getRootFragmentId(): Int {
        return R.id.mainFrame
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     //   val drawerLayout: DrawerLayout = dataBinding.drawerLayout
        val navView: NavigationView = dataBinding.navView
      //  val toolbar: Toolbar = findViewById(R.id.toolbar)
        showPage(AllChatsFragment())
       // setSupportActionBar(toolbar)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        val drawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val mDrawerToggle: ActionBarDrawerToggle

        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            mDrawerToggle = object : ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            ) {
                override fun onDrawerClosed(drawerView: View) {
                    supportInvalidateOptionsMenu()
                    //drawerOpened = false;
                }

              override  fun onDrawerOpened(drawerView: View) {
                    supportInvalidateOptionsMenu()
                    //drawerOpened = true;
                }
            }
            mDrawerToggle.setDrawerIndicatorEnabled(true)
            drawerLayout.setDrawerListener(mDrawerToggle)
            mDrawerToggle.syncState()
        }

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_create_group -> {

                }
                R.id.nav_contacts -> {

                }
                R.id.nav_calls -> {

                }
                R.id.nav_favorites -> {

                }
                R.id.nav_settings -> {
                    showPage(UserSettingsFragment())
                }
                R.id.nav_invite -> {
                    showPage(InviteUserFragment())
                }
                R.id.nav_about -> {

                }
            }
            drawerLayout.closeDrawers()
            return@setNavigationItemSelectedListener true
        }

        val nickName = dataBinding.navView.getHeaderView(0).findViewById<TextView>(R.id.nickname)
        nickName.text = AppPref.getInstance().getUser()?.name
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_scan_qr){
            showPage(MenuScanQrFragment())
        }
        return super.onOptionsItemSelected(item)
    }
    var dialog: AlertDialog? = null
    override fun subscribe() {
        super.subscribe()



        model.invitationStartLiveData.observe(this, Observer {
            if (it != null) {
                model.invitationStartLiveData.value = null
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Connecting...")
                builder.setMessage("Please wait,secure connection is being established")
                builder.setCancelable(false)
                dialog = builder.show()
            }
        })

        model.invitationErrorLiveData.observe(this, Observer {
            if (it != null) {
                model.invitationErrorLiveData.value = null
                dialog?.cancel()
                model.onShowToastLiveData.postValue(it.second)
            }

        })

        model.invitationSuccessLiveData.observe(this, Observer {
            if (it != null) {
                model.invitationSuccessLiveData.value = null
                val item = model.getMessage(it)
                dialog?.cancel()
                //     popPage(ChatsFragment.newInstance(item))
            }
        })

        model.invitationPolicemanSuccessLiveData.observe(this, Observer {
            if (it != null) {
                model.invitationPolicemanSuccessLiveData.value = null
                val item = model.getMessage(it)
                dialog?.cancel()
                //  popPage(DocumentShareFragment.newInstance(item))
            }
        })
    }


}