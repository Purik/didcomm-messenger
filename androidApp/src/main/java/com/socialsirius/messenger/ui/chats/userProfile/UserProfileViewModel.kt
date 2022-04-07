package  com.socialsirius.messenger.ui.chats.userProfile

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.socialsirius.messenger.base.providers.ResourcesProvider
import com.socialsirius.messenger.base.ui.BaseViewModel

import javax.inject.Inject

class UserProfileViewModel @Inject constructor(
    resourcesProvider: ResourcesProvider
) : BaseViewModel() {

    val nameLiveData = MutableLiveData<String>()
    val phoneLiveData = MutableLiveData<String>()
    val nicknameLiveData = MutableLiveData<String>()
  //  val avatarLiveData = MutableLiveData<RosterUser>()
  //  private lateinit var myUser: RosterUser

    val chatClickLiveData = MutableLiveData<Boolean>()

  /*  fun setUser(user: RosterUser) {
        myUser = user
    }*/

    override fun onViewCreated() {
        super.onViewCreated()

     /*   avatarLiveData.value = myUser
        nameLiveData.value = myUser.contactNameFull
        phoneLiveData.value = myUser.telephony_mob ?: ""
        nicknameLiveData.value = myUser.contactName*/
    }

    fun onStartChatClick(v: View?) {
        chatClickLiveData.value = true
    }
}