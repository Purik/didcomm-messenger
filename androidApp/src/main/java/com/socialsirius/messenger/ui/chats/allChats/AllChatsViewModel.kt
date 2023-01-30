package  com.socialsirius.messenger.ui.chats.allChats

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.sirius.library.mobile.helpers.PairwiseHelper
import com.socialsirius.messenger.base.providers.ResourcesProvider
import com.socialsirius.messenger.base.ui.BaseViewModel
import com.socialsirius.messenger.models.ChatMessageStatus
import com.socialsirius.messenger.models.Chats
import com.socialsirius.messenger.models.ui.ItemContacts
import com.socialsirius.messenger.repository.MessageRepository
import com.socialsirius.messenger.repository.UserRepository
import com.socialsirius.messenger.transform.LocalMessageTransform
import com.socialsirius.messenger.transform.PairwiseTransform
import com.socialsirius.messenger.utils.extensions.observeOnce
import com.socialsirius.messenger.utils.extensions.observeUntilDestroy

import javax.inject.Inject

class AllChatsViewModel @Inject constructor(
    resourcesProvider: ResourcesProvider,
   // val chatsRepository: ChatsRepository,
    val userRepository: UserRepository,
    val messageRepository: MessageRepository
 //   val uiUseCase: UIUseCase
) : BaseViewModel() {
    val eventStoreLiveData = messageRepository.eventStoreLiveData
    val emptyStateLiveData = MutableLiveData<Boolean>()
   // val chatsLiveData = chatsRepository.result
    val chatsListLiveData = MutableLiveData<List<Chats>>(listOf())
    var originalList : List<Chats> = listOf()
    val chatsSelectLiveData = MutableLiveData<Chats>()
    //val messagesLiveData = messagesRepository.messagesInDBLiveData
    val updateOneChatsLiveData = MutableLiveData<Chats>()
   // val lastActivityAllLiveData = userRepository.statusListLiveData
 //   val activityStatusLiveData = MutableLiveData<List<RosterStatusResponse>>()
 //   val messagesInDBForceRefreshLiveData = messagesRepository.messagesInDBForceRefreshLiveData
 //   val repositoryCreatedLiveData = chatsRepository.oneChatCreatedUpdateLiveData
  //  val updateOneChatLiveData = chatsRepository.oneChatUpdateLiveData
 //   val filterLiveData = uiUseCase.searchFilterLiveData

    val inviteUserLiveData = MutableLiveData<Boolean>()
    val scanQrLiveData = MutableLiveData<Boolean>()
    val updateMessageLiveData = messageRepository.updateMessageLiveData


    fun updateStatusOfChat(idMess : String?){
        if(idMess != null){
            updateMessageLiveData.value = null
            val list =  chatsListLiveData.value.orEmpty().toMutableList()
            var message  =list.firstOrNull {
                it.lastMessage?.id == idMess
            }

            message?.let {
                val mess = messageRepository.getItemBy(idMess?:"")
                message.lastMessage = mess
                chatsListLiveData.postValue(list)
            }
        }
    }
    override fun onViewCreated() {
        super.onViewCreated()

   /*     chatsRepository.getAllChats()
        repositoryCreatedLiveData.observeUntilDestroy(this) {
            it?.let {
                chatsRepository.getAllChats(forceRefresh = true)
                repositoryCreatedLiveData.postValue(null)
            }
        }
        updateOneChatLiveData.observeUntilDestroy(this) {
            it?.let {
                updateChatWithJid(it.id)
            }
        }*/
    //    getChatList()
    }

    override fun setupViews() {
        super.setupViews()
        subscribe()
    }

    fun subscribe(){


    }
    override fun onResume() {
        super.onResume()
        getChatList()
    }

    fun getChatList() {
        createList()

    }

    fun onSelectChat(chat: Chats) {
        chatsSelectLiveData.value = chat
    }

    fun updateChatWithJid(jid: String) {
     /*   if (jid == AppPref.getUserJid()) {
            return
        }
        chatsRepository.getChatWithJid(jid).observeOnce(this, action = { chats ->
            if (chats != null) {
                updateOnechat(chats)
                Log.d("mylog20709", "updateChatWithJid chats=" + chats)
                updateOneChatsLiveData.value = chats
            }
        })*/
    }

    fun updateLastActivity() {
      /*  val items = lastActivityAllLiveData.value
        activityStatusLiveData.postValue(items)*/
    }

    fun refreshAllChats() {
      //  chatsRepository.getAllChats(false)
    }

    fun updateList(list : List<Chats>){
        emptyStateLiveData.postValue(list.isEmpty())
        chatsListLiveData.postValue(list)
    }
  //  fun filterWith(filter: FilterChipsModel?) {
       /* val list = originalList?.filter { chats ->
            filter?.let {
                if (filter.id == -2) {
                    return@filter true
                }
                if (filter.id == -1) {
                    if (chats.isRoom) {
                        return@filter chats.workspacePk == 0
                    }

                    if (chats is SecretChats) {
                        return@filter true
                    }

                    Log.d("mylog2099","chats.creatorUser?="+chats.creatorUser?.name +" chats.creatorUser?="+chats.creatorUser?.userGroups)
                    return@filter chats.creatorUser?.isUserAddedByMe ?: false
                  //  val keySet = StringUtils.makeSetFromString(chats.creatorUser?.workspaceName)
                 //   val isDo = keySet.contains(filter.id.toString())
                  //  return@filter false
                }
                if (chats.isRoom) {
                    return@filter chats.workspacePk == filter.id
                } else {
                    val keySet = StringUtils.makeSetFromString(chats.creatorUser?.workspaceName)
                    val isDo = keySet.contains(filter.id.toString())
                    return@filter isDo
                }
            }
            return@filter true*/
     //   }
        //chatsListLiveData.postValue(list)
   // }

    fun updateOnechat(chats: Chats?) {


    }

    fun onInviteUserClick(v: View){
        inviteUserLiveData.postValue(true)
    }

    fun onScanQrClick(v: View){
        scanQrLiveData.postValue(true)
    }

    private fun createList() {
        val pairwises = PairwiseHelper.getAllPairwise()

        val list = pairwises.map {
           val lastMess =  messageRepository.getLastMessagesForPairwiseDid2(it.their.did?:"")
           val unreadCount =  messageRepository.getUnreadcountForDid(it.their.did?:"")
            val chats = PairwiseTransform.pairwiseToItemContacts(it,lastMess)
            chats.unreadMessageNotInDB = unreadCount
            return@map chats
        }.filter { it.title != "Mediator" }.sortedByDescending {  it.lastMessage?.sentTime }.toMutableList()

         messageRepository.getUnacceptedInvitationMessages().observeOnce(this){listMessage->
             if(listMessage.isNotEmpty()){
                 val invitationChat = Chats()
                 invitationChat.title = "Invitations"
                 invitationChat.id = "invitation"
                 invitationChat.allMessageCount = listMessage.count()
                 invitationChat.unreadMessageNotInDB = listMessage.filter { it.status != ChatMessageStatus.acknowlege }.count()
                 list.add(0,invitationChat)
                 updateList(list)
             }
        }
        updateList(list)

    }

}