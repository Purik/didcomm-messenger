package  com.socialsirius.messenger.ui.userSettings

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog.Builder
import androidx.lifecycle.Observer
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.base.AppPref
import com.socialsirius.messenger.base.ui.BaseFragment
import com.socialsirius.messenger.databinding.FragmentUserSettingsBinding
import com.socialsirius.messenger.design.dialogs.SelectorDialogFragment
import com.socialsirius.messenger.ui.activities.auth.AuthActivity
import com.socialsirius.messenger.ui.pinCreate.CreatePinFragment
import com.socialsirius.messenger.ui.pinEnter.EnterPinFragment
import com.socialsirius.messenger.ui.pinEnter.GoToAfterSuccess
import com.socialsirius.messenger.utils.PermissionHelper


class UserSettingsFragment : BaseFragment<FragmentUserSettingsBinding, UserSettingsViewModel>() {

    companion object {
        /*   @JvmStatic
           fun newInstance(user: RosterUser) = UserSettingsFragment().apply {
               arguments = Bundle().apply {
                   putSerializable(StaticFields.BUNDLE_USER, user)
               }
           }*/
    }

    override fun getLayoutRes(): Int = R.layout.fragment_user_settings

    override fun setModel() {
        dataBinding!!.viewModel = model
    }

    override fun initDagger() {
        App.getInstance().appComponent.inject(this)
    }

    private fun geTextWatcher(onTextFinishChanged: ((String) -> Unit)): TextWatcher {
        return object : TextWatcher {
            var timer: CountDownTimer? = null
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                timer?.cancel()
                timer = object : CountDownTimer(1000, 1500) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        onTextFinishChanged(s.toString())
                    }
                }.start()
            }
        }

    }

    val nameEditTextWatcher = geTextWatcher() { model.onNameChanged(it) }
    val lastNameEditTextWatcher = geTextWatcher() { model.onLastNameChanged(it) }
    val nicknameEditTextWatcher = geTextWatcher() { model.onNickNameChanged(it) }
    val aboutEditTextWatcher = geTextWatcher() { model.onAboutChanged(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        dataBinding?.askPinLayout?.setOnClickListener { model.onPinCodeTimerClick() }
        dataBinding?.backupPeriodicLayout?.setOnClickListener { model.onBackupPeriodicClick() }
        dataBinding.syncContactToggle.setOnClickListener {
            AppPref.getInstance().setSyncContacts(dataBinding.syncContactToggle.isChecked)
            if (AppPref.getInstance().isSyncContacts()) {
                checkPermission()
            }
        }
        /*  model.user = arguments?.getSerializable(StaticFields.BUNDLE_USER) as RosterUser

          newConversationsToggle.setOnCheckedChangeListener { _, b ->
              model.newConversationsLiveData.value = newConversationsToggle.isChecked
          }


          updateNotificationsValue.setOnClickListener { model.onUpdateNotificationsClick() }

          deleteAskPinLayout.setOnClickListener { model.showDeleteWalletAlert.postValue(true) }
          backupWalletLayout.setOnClickListener { model.onBackupWallet() }
          restoreWalletLayout.setOnClickListener { model.showRestoreWalletAlert.postValue(true) }

          */

    }

    override fun subscribe() {
        model.onBackClickLiveData.observe(this, Observer { onBackPressed() })
        model.logoutLiveData.observe(this, Observer { testLogout() })

        model.appVersionLiveData.observe(this, Observer {
            dataBinding?.versionText?.text = it
        })
        model.askPinTimeLiveData.observe(this, Observer {
            dataBinding?.pinCodeValue?.text = it
        })
        model.backupPeriodicLiveData.observe(this, Observer {
            dataBinding?.backupPeriodicValue?.text = it
        })

        model.nameLiveData.observe(this, Observer {
            dataBinding?.nameEditText?.removeTextChangedListener(nameEditTextWatcher)
            dataBinding?.nameEditText?.setText(it ?: "")
            dataBinding?.nameEditText?.addTextChangedListener(nameEditTextWatcher)
        })
        model.pinCodeTimeClickLiveData.observe(this, Observer {
            if (it !=null){
                model.pinCodeTimeClickLiveData.value = null
                SelectorDialogFragment(it, model::onSetPinCodeTimer).show(
                    parentFragmentManager,
                    SelectorDialogFragment::class.java.simpleName
                )
            }

        })




        model.backupPeriodicClickLiveData.observe(this, Observer {
            SelectorDialogFragment(it, model::onSetBackupPeriod).show(
                parentFragmentManager,
                SelectorDialogFragment::class.java.simpleName
            )
        })

        model.changePinClickLiveData.observe(this, Observer {
            if (it) {
                model.changePinClickLiveData.value = false
                var enterPin = EnterPinFragment()
                enterPin.goToAfterSuccess = GoToAfterSuccess.CreatePin
                baseActivity.pushPageAdd(enterPin)
            }
        })
        model.lastBackupTimeLiveData.observe(this, Observer {
            dataBinding.lastbackupRestoreTimeText.text = it
        })

        model.syncContactLiveData.observe(this, Observer {
            dataBinding.syncContactToggle.isChecked = it
        })
        /*


        model.nicknameLiveData.observe(this, Observer {
            nicknameEditText.removeTextChangedListener(nicknameEditTextWatcher)
            nicknameEditText.text = it.orEmpty()
            nicknameEditText.addTextChangedListener(nicknameEditTextWatcher)
        })
        model.aboutLiveData.observe(this, Observer {
            aboutEditText.removeTextChangedListener(aboutEditTextWatcher)
            aboutEditText.text = it.orEmpty()
            aboutEditText.addTextChangedListener(aboutEditTextWatcher)
        })
        model.phoneLiveData.observe(this, Observer {
            phoneEditText.setText(it ?: "")
        })
        model.enableEditViewsLiveData.observe(this, Observer {
            phoneEditText.isEnabled = false
            nameEditText.isEnabled = it
            nicknameEditText.isEnabled = it
            lastNameEditText.isEnabled = it
            aboutEditText.isEnabled = it
            publicAccountToggle.isEnabled = it
            taskRemainderToggle.isEnabled = it
            taskCompleteToggle.isEnabled = it
            newConversationsToggle.isEnabled = it
        })

        model.publicAccountLiveData.observe(this, Observer {
            publicAccountToggle.isChecked = it
        })

        model.newConversationsLiveData.observe(this, Observer {
            newConversationsToggle.isChecked = it
            model.onNotificationConversionChanged()

        })
        model.taskRemainderLiveData.observe(this, Observer {
            taskRemainderToggle.isChecked = it
            model.onNotificationTaskReminderChanged()

        })
        model.taskCompleteLiveData.observe(this, Observer {
            taskCompleteToggle.isChecked = it
            model.onNotificationTaskCompleteChanged()

        })

        model.appNotificationsLiveData.observe(this, Observer {
            updateNotificationsValue.text = it
        })

        model.updateNotificationsClickLiveData.observe(this, Observer {
            SelectorDialogFragment(it, model::onSetUpdateNotificationStrategy).show(parentFragmentManager, SelectorDialogFragment::class.java.simpleName)
        })
        model.showRestoreWalletAlert.observe(this, Observer {
            if (it) {
                restoreWalletAlert()
                model.showRestoreWalletAlert.value = false
            }
        })

        model.showDeleteWalletAlert.observe(this, Observer {
            if (it) {
                deleteWalletAlert()
                model.showDeleteWalletAlert.value = false
            }

        })
        model.showImportFragmentLiveData.observe(this, Observer {
            if (it) {
                baseWalletActivity.intent.putExtra("from_login", true)
                baseWalletActivity.intent.putExtra("type", 2)
                baseWalletActivity.openCodeActivity()
                model.showImportFragmentLiveData.value = false
            }
        })

       */
    }

    private fun checkPermission() {
        if (!PermissionHelper.checkReadContactPermissionGranted(baseActivity)) {
            PermissionHelper.requestReadContactPermission(baseActivity, 100)
        } else {
            model.sendPhoneBookToServer()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionHelper.onRequestPermissionsResult(
            requestCode,
            100,
            permissions,
            grantResults,
            object : PermissionHelper.OnRequestPermissionListener {
                override fun onRequestFail() {
                    Toast.makeText(
                        baseActivity,
                        R.string.check_sync_contacts_error,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onRequestSuccess() {
                    model.sendPhoneBookToServer()
                    //   model.loginViaPhone(dataBinding.syncContactsCheckbox.isChecked)
                }
            })
    }

    private fun restoreWalletAlert() {
        val builder = Builder(requireContext())
        builder.setTitle(App.getContext().getString(R.string.warning_with_point))
        builder.setMessage(App.getContext().getString(R.string.settings_restore_wallet_hint))
        builder.setPositiveButton(
            App.getContext().getString(R.string.yes)
        ) { dialog, which -> model.onRestoreWallet() }
        builder.setNegativeButton(App.getContext().getString(R.string.no), null)
        builder.show()
    }

    private fun deleteWalletAlert() {
        val builder = Builder(requireContext())
        builder.setTitle(App.getContext().getString(R.string.warning_with_point))
        builder.setMessage(App.getContext().getString(R.string.settings_delete_wallet))
        builder.setPositiveButton(
            App.getContext().getString(R.string.yes)
        ) { dialog, which -> model.onDeleteWalletClick() }
        builder.setNegativeButton(App.getContext().getString(R.string.no), null)
        builder.show()
    }


    private fun testLogout() {
        try {
            val builder = Builder(requireContext())
            builder.setTitle(App.getContext().getString(R.string.user_logout_title))
            builder.setMessage(App.getContext().getString(R.string.user_logout_are_you_sure))
            builder.setPositiveButton(App.getContext().getString(R.string.yes)) { dialog, which ->
                model.logout(false)
                baseActivity.finishAffinity()
                AuthActivity.newInstance(requireContext())
            }
            builder.setNegativeButton(App.getContext().getString(R.string.no), null)
            builder.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}