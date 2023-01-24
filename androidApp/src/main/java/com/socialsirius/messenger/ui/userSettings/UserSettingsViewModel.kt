package  com.socialsirius.messenger.ui.userSettings

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.sirius.library.mobile.SiriusSDK
import com.sirius.library.mobile.helpers.WalletHelper
import com.sirius.library.utils.SDK
import com.socialsirius.messenger.BuildConfig
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.AppExecutors
import com.socialsirius.messenger.base.AppPref
import com.socialsirius.messenger.base.providers.ResourcesProvider
import com.socialsirius.messenger.base.ui.BaseViewModel
import com.socialsirius.messenger.repository.UserRepository
import com.socialsirius.messenger.sirius_sdk_impl.SDKUseCase

import java.util.*
import javax.inject.Inject

class UserSettingsViewModel @Inject constructor(
    val resourceProvider: ResourcesProvider,
    val userRepository: UserRepository,
   // val indyUseCase: IndyUseCase,
  //  val walletUseCase: WalletUseCase,
    val appExecutors: AppExecutors,
  // val loginUseCase: LoginUseCase,
   // val contactUseCase: ContactUseCase,
    val sdkUseCase: SDKUseCase
) : BaseViewModel() {

    val logoutLiveData = MutableLiveData<Boolean>()
    val nameLiveData = MutableLiveData<String?>()
    val surnameLiveData = MutableLiveData<String?>()
    val phoneLiveData = MutableLiveData<String?>()
    val nicknameLiveData = MutableLiveData<String?>()
    val aboutLiveData = MutableLiveData<String?>()
    val enableEditViewsLiveData = MutableLiveData<Boolean>()
    val publicAccountLiveData = MutableLiveData<Boolean>()
    val newConversationsLiveData = MutableLiveData<Boolean>()
    val taskRemainderLiveData = MutableLiveData<Boolean>()
    val taskCompleteLiveData = MutableLiveData<Boolean>()
    val appVersionLiveData = MutableLiveData<String>()
    val appNotificationsLiveData = MutableLiveData<String>()
    val askPinTimeLiveData = MutableLiveData<String>()
    val syncContactLiveData = MutableLiveData<Boolean>()
    val backupPeriodicLiveData = MutableLiveData<String>()

    val updateNotificationsClickLiveData = MutableLiveData<List<String>>()
    val changePinClickLiveData = MutableLiveData<Boolean>()
    val pinCodeTimeClickLiveData = MutableLiveData<List<String>?>()
    val backupPeriodicClickLiveData = MutableLiveData<List<String>>()
    val showRestoreWalletAlert = MutableLiveData<Boolean>(false)
    val showDeleteWalletAlert = MutableLiveData<Boolean>(false)
    val lastBackupTimeLiveData = MutableLiveData<String>("")
    val showImportFragmentLiveData = MutableLiveData<Boolean>(false)
    val showCreateWalletFragmentLiveData = MutableLiveData<Boolean>(false)
  //  var user: RosterUser? = null

    override fun onViewCreated() {
        super.onViewCreated()
        val myUser = userRepository.myUser
        nameLiveData.value = myUser.name
        val versionText = "Ver. ${BuildConfig.VERSION_NAME}"
        appVersionLiveData.value = versionText.toString()
        backupPeriodicLiveData.value = getPeriodicString()
        syncContactLiveData.value = AppPref.getInstance().isSyncContacts()
       /*
        surnameLiveData.value = myUser.fullname
        phoneLiveData.value = myUser.telephony_mob
        enableEditViewsLiveData.value = user?.jid == AppPref.getMyselfUser().jid
        aboutLiveData.value = myUser.descr
        nicknameLiveData.value = myUser.username
        publicAccountLiveData.value = myUser.isIs_public
        taskCompleteLiveData.value = AppPref.getInstance().isEndedTask && AppPref.getInstance().isEndedTaskVibro
        newConversationsLiveData.value =  AppPref.getInstance().isSoundNewMessage && AppPref.getInstance().isSoundNewMessageVibro
        taskRemainderLiveData.value =   AppPref.getInstance().isReminderTask && AppPref.getInstance().isReminderTaskVibro

        setLastBackup()*/
        askPinTimeLiveData.value = getPinString()

    }

    fun setLastBackup(){
      /*  if (AppPref.getInstance().backupLastTime==0L){
            lastBackupTimeLiveData.postValue(resourceProvider.getString(R.string.never))
        }else{
            lastBackupTimeLiveData.postValue(DateUtils.getStringFromDate(Date( AppPref.getInstance().backupLastTime),DateUtils.PATTERN_ddMMyyyyHHmmss,false))
        }*/
    }

    private fun getPinString(): String {
        val type = AppPref.getInstance().getBlockType()
        var string: String = resourceProvider.getString(R.string.settings_safety_safety_30)
        when (type) {
            0 -> string = resourceProvider.getString(R.string.settings_safety_safety_30)
            1 -> string = resourceProvider.getString(R.string.settings_safety_safety_15)
            2 -> string = resourceProvider.getString(R.string.settings_safety_safety_3)
            3 -> string = resourceProvider.getString(R.string.settings_safety_safety_5)
            4 -> string = resourceProvider.getString(R.string.settings_safety_safety_always)
            else -> string = resourceProvider.getString(R.string.settings_safety_safety_30)
        }
        return string
        return ""
    }

    private fun getPeriodicString(): String {
      /*  val type = AppPref.getInstance().backupPeriod
        var string: String = resourceProvider.getString(R.string.settings_safety_safety_30)
        when (type) {
            0 -> string = resourceProvider.getString(R.string.never)
            1 -> string = resourceProvider.getString(R.string.backup_everyday)
            2 -> string = resourceProvider.getString(R.string.backup_everyweek)
            3 -> string = resourceProvider.getString(R.string.backup_everymonth)
            else -> string = resourceProvider.getString(R.string.backup_everyday)
        }
        return string*/
        return ""
    }

    fun logout(forceLogout : Boolean){
        userRepository.logout()
        sdkUseCase.logoutFromSDK()
    //    showProgressDialog()
        //loginUseCase.logout(forceLogout)
    }
    fun onLogoutButtonClick(v: View?) {
        logoutLiveData.value = true
    }

    fun onNameChanged(name: String) {
        if(name.count()<=20){
            userRepository.myUser.name = name
            userRepository.saveUserToPref()
        }else{
            onShowToastLiveData.postValue(resourceProvider.getString(R.string.user_name_count_error))
        }

    }

    fun onLastNameChanged(lastName: String) {
       /* userRepository.updateUser(jid = user?.jid ?: "", lastName = lastName).observeOnce(this) {
            if (it == true) {
                AppPref.getMyselfUser().fullname = lastName
                DaoUtilsRoster.writeRosterUser(AppPref.getMyselfUser())
            }
        }*/
    }

    fun onChangePinClick(v :View) {
        changePinClickLiveData.postValue(true)
        /* userRepository.updateUser(jid = user?.jid ?: "", nickname = nickname).observeOnce(this) {
             if (it == true) {
                 AppPref.getMyselfUser().username = nickname
                 DaoUtilsRoster.writeRosterUser(AppPref.getMyselfUser())
             }
         }*/
    }

    fun onNickNameChanged(nickname: String) {
       /* userRepository.updateUser(jid = user?.jid ?: "", nickname = nickname).observeOnce(this) {
            if (it == true) {
                AppPref.getMyselfUser().username = nickname
                DaoUtilsRoster.writeRosterUser(AppPref.getMyselfUser())
            }
        }*/
    }

    fun onAboutChanged(about: String) {
    /*    userRepository.updateUser(jid = user?.jid ?: "", about = about).observeOnce(this) {
            if (it == true) {
                AppPref.getMyselfUser().descr = about
                DaoUtilsRoster.writeRosterUser(AppPref.getMyselfUser())
            }
        }*/
    }

    //TODO каждый раз при заходе на экран обновляется на сервере - УБРАТЬ!!!
    fun onIsPublicChanged(isPublic: Boolean) {
       /* userRepository.updateUser(jid = user?.jid ?: "", isPublic = isPublic).observeOnce(this) {
            if (it == true) {
                AppPref.getMyselfUser().isIs_public = isPublic
                DaoUtilsRoster.writeRosterUser(AppPref.getMyselfUser())
            }
        }*/
    }


     fun sendPhoneBookToServer() {
      /*  Thread(Runnable {
            val phoneBook: PhoneBook = contactUseCase.phones
            val instance = PhoneNumberUtil.createInstance(App.getContext())
            val countryDefCode = AppPref.getInstance().defaultCountry
            val countryCode = instance.getCountryCodeForRegion(countryDefCode)
            phoneBook.setCountryPrefix("+$countryCode")
            rosteRepository.sendPhoneBookToServer(phoneBook)
        }).start()*/
    }

    fun onPinCodeTimerClick() {
        pinCodeTimeClickLiveData.value = listOf(
              //  resourceProvider.getString(R.string.settings_safety_safety_never),
                resourceProvider.getString(R.string.settings_safety_safety_30),
                resourceProvider.getString(R.string.settings_safety_safety_15),
                resourceProvider.getString(R.string.settings_safety_safety_3),
                resourceProvider.getString(R.string.settings_safety_safety_5),
                resourceProvider.getString(R.string.settings_safety_safety_always)
        )
    }

    fun onBackupPeriodicClick() {
      /*  backupPeriodicClickLiveData.value = listOf(
                resourceProvider.getString(R.string.never),
                resourceProvider.getString(R.string.backup_everyday),
                resourceProvider.getString(R.string.backup_everyweek),
                resourceProvider.getString(R.string.backup_everymonth),

        )*/
    }

    fun onDeleteWalletClick(){
      /*  IndyWallet.deleteWallet()
        logout(true)*/
    }

    fun onBackupWallet(){
     /*   showProgressDialog(resourceProvider.getString(R.string.backup_wallet_in_progress))
        Thread(Runnable {
            indyUseCase.backupWallet()
            walletUseCase.deleteExportedWallet()
            walletUseCase.exportWallet()
            hideProgressDialog()
            setLastBackup()
        }).start()*/
    }



    fun onRestoreWallet(){
       /* showProgressDialog()
        Thread(Runnable {
           // indyUseCase.restoreWallet()
            IndyWallet.deleteWallet()
            showImportFragmentLiveData.postValue(true)
            hideProgressDialog()
        }).start()*/
    }


    fun onUpdateNotificationsClick() {
       /* updateNotificationsClickLiveData.value = listOf(
                resourceProvider.getString(R.string.updates_all),
                resourceProvider.getString(R.string.updates_important),
                resourceProvider.getString(R.string.updates_no)
        )*/
    }

    fun onSetPinCodeTimer(item: Pair<Int, String>) {
        AppPref.getInstance().setBlockType(item.first)
        askPinTimeLiveData.value = item.second
    }

    fun onSetBackupPeriod(item: Pair<Int, String>) {
       /* AppPref.getInstance().backupPeriod = item.first
        backupPeriodicLiveData.value = item.second*/
    }

    fun onSetUpdateNotificationStrategy(item: Pair<Int, String>) {
     //   appNotificationsLiveData.value = item.second
    }

    fun onNotificationTaskCompleteChanged() {
     /*   AppPref.getInstance().isEndedTask = taskCompleteLiveData.value ?: false
        AppPref.getInstance().isEndedTaskVibro = taskCompleteLiveData.value ?: false*/

    }

    fun onNotificationTaskReminderChanged() {
      /*  AppPref.getInstance().isReminderTask = taskRemainderLiveData.value ?: false
        AppPref.getInstance().isReminderTaskVibro = taskRemainderLiveData.value ?: false*/
    }

    fun onNotificationConversionChanged() {
        /*AppPref.getInstance().isSoundNewMessage = newConversationsLiveData.value ?: false
        AppPref.getInstance().isSoundNewMessageVibro = newConversationsLiveData.value ?: false*/
    }
}