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
import com.socialsirius.messenger.base.ui.BaseFragment
import com.socialsirius.messenger.databinding.FragmentUserSettingsBinding
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
      /*  model.user = arguments?.getSerializable(StaticFields.BUNDLE_USER) as RosterUser
        super.onViewCreated(view, savedInstanceState)

        publicAccountToggle.setOnCheckedChangeListener { _, b ->
            model.publicAccountLiveData.value = publicAccountToggle.isChecked
            model.onIsPublicChanged(model.publicAccountLiveData.value ?: false)
        }
        taskRemainderToggle.setOnCheckedChangeListener { _, b ->
            model.taskRemainderLiveData.value = taskRemainderToggle.isChecked
        }
        taskCompleteToggle.setOnCheckedChangeListener { _, b ->
            model.taskCompleteLiveData.value = taskCompleteToggle.isChecked
        }
        newConversationsToggle.setOnCheckedChangeListener { _, b ->
            model.newConversationsLiveData.value = newConversationsToggle.isChecked
        }

        changePinButton.setOnClickListener { model.changePinClickLiveData.value = true }
        updateNotificationsValue.setOnClickListener { model.onUpdateNotificationsClick() }
        askPinLayout.setOnClickListener { model.onPinCodeTimerClick() }
        askPinLayout.setOnClickListener { model.onPinCodeTimerClick() }
        deleteAskPinLayout.setOnClickListener { model.showDeleteWalletAlert.postValue(true) }
        backupWalletLayout.setOnClickListener { model.onBackupWallet() }
        restoreWalletLayout.setOnClickListener { model.showRestoreWalletAlert.postValue(true) }
        backupPeriodicLayout.setOnClickListener { model.onBackupPeriodicClick() }
        dataBinding.syncContactToggle.setOnClickListener {
            AppPref.getInstance().isSyncContacts = dataBinding.syncContactToggle.isChecked
            if (AppPref.getInstance().isSyncContacts) {
                checkPermission()
            }
        }*/

    }

    override fun subscribe() {
        /*model.onBackClickLiveData.observe(this, Observer { onBackPressed() })
        model.logoutLiveData.observe(this, Observer { testLogout() })
        model.nameLiveData.observe(this, Observer {
            nameEditText.removeTextChangedListener(nameEditTextWatcher)
            nameEditText.text = it ?: ""
            nameEditText.addTextChangedListener(nameEditTextWatcher)
        })
        model.surnameLiveData.observe(this, Observer {
            lastNameEditText.removeTextChangedListener(lastNameEditTextWatcher)
            lastNameEditText.text = it ?: ""
            lastNameEditText.addTextChangedListener(lastNameEditTextWatcher)

        })
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

        model.appVersionLiveData.observe(this, Observer {
            versionText.text = it
        })
        model.appNotificationsLiveData.observe(this, Observer {
            updateNotificationsValue.text = it
        })

        model.askPinTimeLiveData.observe(this, Observer {
            pinCodeValue.text = it
        })
        model.backupPeriodicLiveData.observe(this, Observer {
            backupPeriodicValue.text = it
        })


        model.pinCodeTimeClickLiveData.observe(this, Observer {
            SelectorDialogFragment(it, model::onSetPinCodeTimer).show(parentFragmentManager, SelectorDialogFragment::class.java.simpleName)
        })

        model.backupPeriodicClickLiveData.observe(this, Observer {
            SelectorDialogFragment(it, model::onSetBackupPeriod).show(parentFragmentManager, SelectorDialogFragment::class.java.simpleName)
        })

        model.changePinClickLiveData.observe(this, Observer {
            baseActivity.pushPageAdd(CreatePinFragment())//todo ??
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

        model.lastBackupTimeLiveData.observe(this, Observer {
            dataBinding.lastbackupRestoreTimeText.text = it
        })

        model.syncContactLiveData.observe(this, Observer {
            dataBinding.syncContactToggle.isChecked = it
        })*/
    }

    private fun checkPermission() {
        if (!PermissionHelper.checkReadContactPermissionGranted(baseActivity)) {
            PermissionHelper.requestReadContactPermission(baseActivity, 100)
        } else {
            model.sendPhoneBookToServer()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionHelper.onRequestPermissionsResult(requestCode, 100, permissions, grantResults, object : PermissionHelper.OnRequestPermissionListener {
            override fun onRequestFail() {
                Toast.makeText(baseActivity, R.string.check_sync_contacts_error, Toast.LENGTH_SHORT).show()
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
        builder.setPositiveButton(App.getContext().getString(R.string.yes)) { dialog, which -> model.onRestoreWallet() }
        builder.setNegativeButton(App.getContext().getString(R.string.no), null)
        builder.show()
    }

    private fun deleteWalletAlert() {
        val builder = Builder(requireContext())
        builder.setTitle(App.getContext().getString(R.string.warning_with_point))
        builder.setMessage(App.getContext().getString(R.string.settings_delete_wallet))
        builder.setPositiveButton(App.getContext().getString(R.string.yes)) { dialog, which -> model.onDeleteWalletClick() }
        builder.setNegativeButton(App.getContext().getString(R.string.no), null)
        builder.show()
    }


    private fun testLogout() {
        try {
            val builder = Builder(requireContext())
            builder.setTitle(App.getContext().getString(R.string.user_logout_title))
            builder.setMessage(App.getContext().getString(R.string.user_logout_are_you_sure))
            builder.setPositiveButton(App.getContext().getString(R.string.yes)) { dialog, which -> model.logout(false) }
            builder.setNegativeButton(App.getContext().getString(R.string.no), null)
            builder.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}