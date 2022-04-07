package  com.socialsirius.messenger.ui.more

import android.text.TextUtils
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.socialsirius.messenger.base.providers.ResourcesProvider
import com.socialsirius.messenger.base.ui.BaseViewModel
import com.socialsirius.messenger.repository.UserRepository

import javax.inject.Inject

class MenuMoreViewModel @Inject constructor(
    val resourcesProvider: ResourcesProvider,
   // val indyRepository: IndyRepository,
  //  val uploadRepository: UploadRepository,
    val userRepository: UserRepository
) : BaseViewModel( ) {

/*
    val connectionsLiveData = MutableLiveData<List<ConnectionItem>>()
    val settingsLiveData = MutableLiveData<Boolean>()
    val nameLiveData = MutableLiveData<String>()
    val phoneLiveData = MutableLiveData<String>()
    val nicknameLiveData = MutableLiveData<String>()
    val avatarLiveData = MutableLiveData<RosterUser>()
    val avatarOnClickLiveData = MutableLiveData<Boolean>()
    val connectionClickLiveData = MutableLiveData<ConnectionItem>()
*/

    override fun onViewCreated() {
        super.onViewCreated()
     /*   val myUser = AppPref.getMyselfUser()
        avatarLiveData.value = myUser
        nameLiveData.value = myUser.contactNameFull
        phoneLiveData.value = myUser.telephony_mob ?: ""
        nicknameLiveData.value = myUser.contactName

        if (IndyWallet.getMyWallet() != null) {
            updateLastConnections()
        }*/
    }

  //  override fun onWalletOpen() {
  //      super.onWalletOpen()
  //      updateLastConnections()
  //  }


    private fun updateLastConnections() {

     /*   val messagesList  = ArrayList<MessagesNew>();
        try {
            val s = Anoncreds.proverGetCredentials(IndyWallet.getMyWallet(), "{}").get()
            val gson = GsonBuilder().create()
            Utils.logLongText("mylo99","s="+s)
            val credentialsModels: List<CredentialsModel> = gson.fromJson<List<CredentialsModel>>(s, object : TypeToken<List<CredentialsModel?>?>() {}.type)
            for (i in credentialsModels.indices) {
                val credentialsModel: CredentialsModel = credentialsModels[i]
                val message = IndyWallet.getCredDefMessage(credentialsModel.getCredDefId())
                Utils.logLongText("mylo99","message="+message)
                var messages: MessagesNew? = null
                if (message != null) {
                    messages = repareFromMessageGson(message)
                } else {
                    messages = repareFromWallet(credentialsModel)
                }
                if (messages != null) {
                    messagesList.add(messages)
                }
            }


        } catch (e: Exception) {
            e.printStackTrace();
        }

        indyRepository.getConnectionsFromMessages(messagesList).observeOnce(this) {
            val connectionItemsList: MutableList<ConnectionItem> = ArrayList()
            it.forEach { wrapper ->
                val attributesCount = resourceProvider.getPluralsString(R.plurals.attrbute_plurals, wrapper.attrCount
                        ?: 0, wrapper.attrCount ?: 0)

                var icon = R.drawable.ic_group_work
                var color = R.color.cardViolet
                when {
                    wrapper.type == ConnectionsWrapper.ConnectionType.proofrequest -> {
                        color = R.color.cardConnectionCredentials
                        icon = R.drawable.ic_connection_verification
                    }
                    wrapper.type == ConnectionsWrapper.ConnectionType.credentilas -> {
                        color = R.color.cardOrange
                        icon = R.drawable.ic_connection_credentials2
                    }
                    wrapper.type == ConnectionsWrapper.ConnectionType.workspaces-> {
                        color = R.color.cardConnectionGreen
                        icon = R.drawable.ic_connection_chat
                    }
                    wrapper.status == ConnectionsWrapper.ConnectionStatus.canceled -> {
                        color = R.color.errorColor
                    }
                }
                val item = ConnectionItem(wrapper.id, wrapper.userName, wrapper.credName, attributesCount, icon, color, wrapper)
                connectionItemsList.add(item)
            }
            connectionsLiveData.value = (connectionItemsList)

        }*/
    }

 /*   fun repareFromMessageGson(message: String): MessagesNew? {
        try {
            val gson = Gson()
            val messages: MessagesNew = gson.fromJson(message, MessagesNew::class.java)
            if (messages != null) {
                if (!TextUtils.isEmpty(messages.getId())) {
                    return messages
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }*/

   // fun repareFromWallet(credentialsModel: CredentialsModel): MessagesNew? {
  //      try {
            //   String schema = IndyWallet.getIssuerSchema(credentialsModel.getSchemaId());
            //   String schema1 = IndyWallet.getCredDef(credentialsModel.getCredDefId());

            //   String schema3 = IndyWallet.getCredDefMessage2(credentialsModel.getCredDefId());
            //    Log.d("mylog2080", "schema=" + schema);
            //     Log.d("mylog2080", "schema1=" + schema1);
            //Log.d("mylog2080","message="+message);
            // Log.d("mylog2080","schema3="+schema3);
    //    } catch (e: Exception) {
   //         e.printStackTrace()
   //     }
   //     return null
   // }

    fun onSettingsButtonClick(v: View?) {
      //  settingsLiveData.value = true
    }

  //  fun onConnectionClick(item: ConnectionItem) {
      //  connectionClickLiveData.value = (item)
  //  }

    fun onAvatarClick(v: View?) {
       // avatarOnClickLiveData.value = true
    }

    fun uploadIconAndSend(filename: String) {
      /*  uploadRepository.uploadFile(filename, true).observeOnceForDone(this) {
            if (it.status == Status.SUCCESS) {
                val avatarUrl = it.data?.url
                userRepository.updateUser(jid = AppPref.getMyselfUser().jid, avatar = avatarUrl).observeOnce(this) {
                    if (it != null) {
                        AppPref.getMyselfUser().avatar = avatarUrl
                        DaoUtilsRoster.writeRosterUser(AppPref.getMyselfUser())
                    }
                }
            } else if (it.status == Status.ERROR) {
                onShowToastLiveData.postValue(resourceProvider.getString(R.string.error))
            }
        }*/
    }
}