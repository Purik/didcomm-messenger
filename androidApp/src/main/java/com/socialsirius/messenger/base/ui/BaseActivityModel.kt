package com.socialsirius.messenger.base.ui

import android.os.Handler
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.sirius.library.agent.aries_rfc.feature_0160_connection_protocol.messages.Invitation
import com.sirius.library.mobile.SiriusSDK
import com.sirius.library.mobile.helpers.ChanelHelper
import com.sirius.library.mobile.helpers.ScenarioHelper
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App
import com.socialsirius.messenger.design.BottomNavView
import com.socialsirius.messenger.models.Chats
import com.socialsirius.messenger.repository.MessageRepository
import com.socialsirius.messenger.repository.models.LocalMessage
import com.socialsirius.messenger.service.SiriusWebSocketListener
import com.socialsirius.messenger.sirius_sdk_impl.SDKUseCase
import com.socialsirius.messenger.transform.LocalMessageTransform
import com.socialsirius.messenger.ui.chats.chat.message.BaseItemMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


abstract class BaseActivityModel(val messageRepository: MessageRepository) : BaseViewModel() {

    val invitationStartLiveData = messageRepository.invitationStartLiveData
    val invitationStartInviteeLiveData = messageRepository.invitationStarInviteeLiveData
    val invitationSuccessLiveData = messageRepository.invitationSuccessLiveData
    val invitationSheetDismissLiveData = MutableLiveData<Boolean>(false)
    val bottomNavClick: MutableLiveData<BottomNavView.BottomTab> =
        MutableLiveData(BottomNavView.BottomTab.Menu)
    var selectedTab = MutableLiveData(BottomNavView.BottomTab.Menu)
    val isVisibleUnauthBottomBar: MutableLiveData<Pair<Boolean, Boolean>> =
        MutableLiveData<Pair<Boolean, Boolean>>(Pair(false, false))


    val invitationErrorLiveData = messageRepository.invitationErrorLiveData
    val showInvitationBottomSheetLiveData = MutableLiveData<Invitation?>()
    val showErrorBottomSheetLiveData = MutableLiveData<String>()

    override fun onResume() {
        super.onResume()
        connect()
        SiriusWebSocketListener.isForeground = true
    }

    fun getChats(id: String): Chats {
        val localMessage = messageRepository.getItemBy(id)
        return LocalMessageTransform.toItemContacts(localMessage)
    }

    fun getMessage(id: String): LocalMessage? {
        val localMessage = messageRepository.getItemBy(id)
        return localMessage
    }


    fun connect(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            GlobalScope.launch {
                if (!task.isSuccessful) {
                    Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                    SiriusSDK.connectToMediator()
                }else{
                    val token = task.result
                    SiriusSDK.connectToMediator()
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()

        SiriusWebSocketListener.isForeground = false
    }

    fun getOnBottomNavClickListner(): BottomNavView.OnbottomNavClickListener {
        return object : BottomNavView.OnbottomNavClickListener {
            override fun onBottomClick(tab: BottomNavView.BottomTab) {
                bottomNavClick.postValue(tab)
            }
        }
    }

    fun getMyEndpoint(): String {
        return SiriusSDK.context?.endpointAddressWithEmptyRoutingKeys ?:""
    }




    fun startInvitationTimeout(){

        Handler().postDelayed(Runnable {
            if(isConnecting){
                isConnecting =false
                showErrorBottomSheetLiveData.postValue(App.getContext().getString(R.string.invitation_later_error))
               // invitationErrorLiveData.postValue(Pair(true, App.getContext().getString(R.string.invitation_later_error)))
            }
        },30*1000)
    }

    var isConnecting = false
    fun acceptInvitation(id: String) {

        ScenarioHelper.acceptScenario(SDKUseCase.Scenario.PersistentInvitation.name,id,null)
        startInvitationTimeout()
        // ChanelHelper.parseMessage(message.serialize())
    }

    fun connectToInvitation(message: Invitation) {
        isConnecting = true
        startInvitationTimeout()
      //  isConnecting = true
        //  ScenarioHelper.acceptScenario(SDKUseCase.Scenario.PersistentInvitation.name,message.getId()?:"",null)
        ChanelHelper.parseMessage(message.serialize())
    }

}