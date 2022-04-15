package  com.socialsirius.messenger.ui.chats.chat


import android.media.MediaPlayer
import android.media.MediaRecorder
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.socialsirius.messenger.base.providers.ResourcesProvider
import com.socialsirius.messenger.base.ui.BaseViewModel
import com.socialsirius.messenger.models.Chats
import com.socialsirius.messenger.models.ui.ItemContacts
import com.socialsirius.messenger.repository.MessageRepository
import com.socialsirius.messenger.sirius_sdk_impl.SDKUseCase
import com.socialsirius.messenger.transform.LocalMessageTransform
import com.socialsirius.messenger.ui.chats.chats.message.BaseItemMessage
import com.socialsirius.messenger.utils.extensions.observeOnce


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.math.BigInteger
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

const val MESSAGES_LIMIT = 20

class ChatViewModel @Inject constructor(
        val resourcesProvider: ResourcesProvider,
        private val messageRepository: MessageRepository,
        val sdkUseCase: SDKUseCase
     //   private val userRepository: UserRepository,
     //   private val messageListenerUseCase: MessageListenerUseCase,
      //  private val favoritesRepository: FavoritesRepository,
       // private val chatsRepository: ChatsRepository,
      //  private val uploadRepository: UploadRepository,
      //  val messageUseCase: MessagesUseCase,
     //   val downloadRepository: DownloadRepository
) : BaseViewModel() {
        private var currentChat: Chats? = null
        val chatLiveData = MutableLiveData<Chats>()
        val adapterListLiveData: MutableLiveData<List<BaseItemMessage>> = MutableLiveData(listOf())
        val clearTextLiveData: MutableLiveData<Boolean> = MutableLiveData()
        val eventStoreLiveData = messageRepository.eventStoreLiveData
        fun setChat(chats: Chats?) {
                currentChat = chats
                currentChat?.let {
                        chatLiveData.value = it
                }
                createList()
        }


        fun readUnread(id : String){
                messageRepository.updateErrorAccepted(id,true,false,null,null)
        }
        private fun createList() {
                messageRepository.getMessagesForPairwiseDid(currentChat?.id ?: "").observeOnce(this) {
                        var list = it.map {
                                LocalMessageTransform.toBaseItemMessage(it)
                        }.toMutableList()

                      /*  if (list.isEmpty()) {
                                visibilityChatLiveData.postValue(View.GONE)
                        } else {
                                visibilityChatLiveData.postValue(View.VISIBLE)
                        }*/

                        if (list.isEmpty()) {
                                LocalMessageTransform.toLocalMessage(currentChat, messageRepository)
                                        .observeOnce(this) {
                                                val message = LocalMessageTransform.toBaseItemMessage(it)
                                                list.add(message)
                                                Collections.sort(
                                                        list,
                                                        kotlin.Comparator { o1, o2 ->
                                                                o1.date?.compareTo(o2.date ?: Date(0)) ?: -1
                                                        })
                                                adapterListLiveData.postValue(list)
                                        }
                        }else{
                                Collections.sort(
                                        list,
                                        kotlin.Comparator { o1, o2 ->
                                                o1.date?.compareTo(o2.date ?: Date(0)) ?: -1
                                        })
                                adapterListLiveData.postValue(list)
                        }
                }
        }

        fun sendMessageText(messageText: String) {
                val message = sdkUseCase.sendTextMessageForPairwise(currentChat?.id ?: "", messageText)
                message?.let {
                        messageRepository.createOrUpdateItem(it)
                        // eventRepository.storeEvent(message.message()?.id ?: "", message, "text")
                        clearTextLiveData.postValue(true)
                }
        }

        fun updateList() {
                createList()
        }

        /*
          private var isRangeLoading = false

          val emptyStateLiveData = MutableLiveData<Boolean>()

          val moreActionLiveData = MutableLiveData<List<String>>()
          val messageActionLiveData = MutableLiveData<List<MessageActionItem>>()
          val lastActivityLiveData = MutableLiveData<String>()
          val activityStatusLiveData = MutableLiveData<Pair<String, Boolean>>()
          val lastActivityAllLiveData = userRepository.statusListLiveData
          val updateOneChatLiveData = chatsRepository.oneChatUpdateLiveData


          val messagesSetLiveData = MutableLiveData<Int>()
          val messagesAddLiveData = MutableLiveData<Triple<Int, Int, Boolean>>()
          val messagesAddRangeLiveData = MutableLiveData<Pair<Int, Int>>()
          val messagesUpdateLiveData = MutableLiveData<List<Int>>()
          val showAcceptInviteButtonLiveData = MutableLiveData<Pair<Boolean, Boolean>>()
          val goToNewSecretChatLiveData = MutableLiveData<SecretChats>()
          val goToNewSecretFromChatLiveData = chatsRepository.oneChatCreatedUpdateLiveData
          val goToConnectionLiveData = MutableLiveData<ConnectionsWrapper>()
          val messageTextLiveData = MutableLiveData<String>()
          val bottomBotButtonList = MutableLiveData<List<BotButton>>()
          val scrollToPositionLiveData = MutableLiveData<Int>()
          private val originalMessages = mutableListOf<IChatItem>()


          val addToContactListLayoutVisibilityLiveData = MutableLiveData<Int>(View.GONE)
          val addToContactListTextLiveData = MutableLiveData<String>()

          fun getOriginalMessages() = originalMessages
          fun getCurrentChat() = chatLiveData.value
          fun getRoomsResponseFromChats() = RoomsResponse(getCurrentChat())

          override fun onDestroy() {
              super.onDestroy()
              cancelTimer()
              cancelAllCredProofTimer()
              chatsRepository.currentOpenChatLiveData.value = null

          }

          fun getDownloadedPath(url: String): String {
              return messageUseCase.getDownloadedFilePathFromUrl(url)
          }

          fun getFilename(url: String, text: String?): String {
              return messageUseCase.getFilename(url, text)
          }

          fun updateUncompleteMessage(message: String?) {
              chatsRepository.updateUncompleteMessage(getCurrentChat(), message)
          }

          override fun onViewCreated() {
              super.onViewCreated()
              currentChat?.let { chat ->
                  chatLiveData.value = chat
                  chatsRepository.currentOpenChatLiveData.value = chat
                  if (!chat.isRoom) {
                      userRepository.checkLastActivityStatus(chat.id ?: "")
                  }

                  val uncompleteMessage = chat.uncompleteMessage

                  messageTextLiveData.value = uncompleteMessage


                  showHideMenu()

                  messagesRepository.messagesInDBLiveData.observeUntilDestroy(this) {
                      if (isMessageCurrentChat(it.data)) {

                          val items = mapMessages(listOfNotNull(it.data))
                          when (it.action) {
                              CHANGE -> updateMessages(items)
                              UPDATE -> updateMessages(items)
                              ADD -> {
                                  addMessage(items)
                                  downloadRepository.startDownloadAfterRefresh(listOfNotNull(it.data), false)
                              }
                              ADD_RANGE -> {
                                  addMessages(items)
                                  downloadRepository.startDownloadAfterRefresh(listOfNotNull(it.data), false)
                              }
                              DELETE -> deleteMessages(items)
                              SET -> {
                                  setMessages(items)
                                  downloadRepository.startDownloadAfterRefresh(listOfNotNull(it.data), false)
                              }
                          }
                          showHideQuestionMenu(it.data)
                          startTimerForCredentialProofMessage1(items)
                      }
                  }

                  initialLoadMessages()

                  *//*  goToNewSecretFromChatLiveData.observeUntilDestroy(this) {
                  it.let {
                      if (it?.id != chat.id && it is SecretChats) {
                          goToNewSecretChatLiveData.postValue(it)
                      }
                  }
              }*//*
            val user: RosterUser? = DaoUtilsRoster.readRosterUser(chat.id)
            val string = java.lang.String.format(App.getContext().getString(R.string.request_add_to_contact), chat.user?.contactNameFull,
                    user?.contactNameFull);
            addToContactListTextLiveData.postValue(string)
        }


    }

    private fun isMessageCurrentChat(messagesNew: MessagesNew?): Boolean {
        if (messagesNew == null) {
            return false
        }
        val chat = getCurrentChat()
        if (chat != null) {
            if (chat is SecretChats) {

                if (messagesNew.msg_from == chat.indyDidFrom || messagesNew.msg_to == chat.indyDidFrom) {
                    return true
                }
            } else {
                if (chat.isRoom) {
                    if (messagesNew.msg_to == chat.id) {
                        return true
                    }
                } else {
                    val member = chat.getNotMeMember()
                    if (messagesNew.msg_from == chat.getNotMeMember() || messagesNew.msg_to == member) {
                        return true
                    }
                }
            }

        }
        return false
    }
    //TODO after timing hideQuestion Menu, open bot menu if bot


    private fun initialLoadMessages() {
        getCurrentChat()?.let { chat ->
            messagesRepository.getLastMessages(chat, 0, MESSAGES_LIMIT).observeOnceForDone(this) {
                if (it.status != Status.LOADING) {
                    val messagesList = it.data ?: listOf()
                    val items = mapMessages(messagesList)
                    setMessages(items)
                    showHideAcceptedBtn()
                    downloadRepository.startDownloadAfterRefresh(messagesList, false)
                    val messages = messagesList.lastOrNull()
                    showHideQuestionMenu(messages)
                    startTimerForCredentialProofMessage1(items)
                }
            }
        }
    }

    fun loadMessagesIfEMpty() {
        getCurrentChat()?.let { chat ->
            if (chat is SecretChats && chat.indyDidTo != null) {
                messagesRepository.getLastMessages(chat, 0, MESSAGES_LIMIT).observeOnceForDone(this) {
                    if (it.status != Status.LOADING) {
                        val items = mapMessages(it.data ?: listOf())
                        setMessages(items)
                        showHideAcceptedBtn()
                    }
                }
            }
        }
    }

    fun showHideAcceptedBtn() {
        if (getCurrentChat() is SecretChats) {
            val secretChat = (getCurrentChat() as SecretChats)
            val gson = Gson()
            val inviteMess = gson.fromJson(secretChat.inviteMessage, MessagesNew::class.java)
            val isVisible = (secretChat.inviteMessage != null && inviteMess.messageUserType == Incoming && secretChat.indyDidTo == null) || secretChat.tempInviteMessage != null
            val isEnabled = secretChat.isAccepted && !secretChat.isTempNotAccepted
            val pair = Pair(isVisible, isEnabled)
            showAcceptInviteButtonLiveData.postValue(pair)
        }
    }

    fun rangeLoadMessages() {
        if (!isRangeLoading) {
            isRangeLoading = true
            getCurrentChat()?.let { chat ->
                val currentOffset = originalMessages.filter { it !is ChatDateItem }.size
                messagesRepository.getLastMessages(chat, currentOffset, MESSAGES_LIMIT).observeOnceForDone(this) {
                    if (it.status != Status.LOADING) {
                        val items = mapMessages(it.data ?: listOf())
                        addMessages(items)
                        isRangeLoading = false
                    }
                }
            }
        }
    }

    fun onMoreButtonClick(v: View?) {
        val favorites: Favorites? = DaoUtilsFavorites.readFavoritesWithKey(Favorites.Category.user, getCurrentChat()?.id
                ?: "")
        if (getCurrentChat()?.isRoom == false) {
            if (getCurrentChat() is SecretChats) {
                moreActionLiveData.value = listOf(
                        resourceProvider.getString(R.string.menu_fragment_messages_chat_clear),
                        resourceProvider.getString(R.string.menu_fragment_messages_send_propose),
                )
            } else {
                val list = mutableListOf(
                        if (favorites == null) resourceProvider.getString(R.string.menu_fragment_messages_chat_add_to_favorite) else resourceProvider.getString(R.string.menu_fragment_messages_chat_del_from_favorite),
                        resourceProvider.getString(R.string.menu_fragment_messages_chat_clear),
                )
                val user: RosterUser? = DaoUtilsRoster.readRosterUser(getCurrentChat()?.id)
                if (user?.isUserOnlyInIncoming(true) == true) {
                    list.add(resourceProvider.getString(R.string.add_user))
                }
                moreActionLiveData.value = list
            }

        } else {
            if (getCurrentChat()?.creator == AppPref.getUserJid()) {
                moreActionLiveData.value = listOf(
                        resourceProvider.getString(R.string.title_settings),
                        resourceProvider.getString(R.string.menu_fragment_messages_chat_add_users)
                )
            } else {
                moreActionLiveData.value = listOf(
                        resourceProvider.getString(R.string.title_settings),
                        resourceProvider.getString(R.string.menu_fragment_messages_chat_add_users),
                        resourceProvider.getString(R.string.leave_chat)
                )
            }
        }
    }




    fun sendAnswerMessageText(message: String) {
        val mesForDb = messageUseCase.createJsonMessage(message, getCurrentChat(), OutComing, ContentType.answer)
        messagesRepository.handleSendIndyMessages(mesForDb)
    }

    fun sendSound(time: Long, type: ChatPanelView.SendType) {
        //TODO from type // uploadFileForRecognize(fileName)
        if (fileName == null) {
            onShowToastLiveData.postValue(resourceProvider.getString(R.string.error))
            return
        }
        val isEncoded = (getCurrentChat() is SecretChats) ?: false
        val messages = messageUseCase.createAudioMessage(time.toString(), fileName!!, getCurrentChat())
        uploadRepository.uploadFileAndSend(messages, isEncoded, isEncoded)
    }

    fun sendImageVideo() {
        if (fileName == null) {
            onShowToastLiveData.postValue(resourceProvider.getString(R.string.error))
            return
        }
        val isEncoded = (getCurrentChat() is SecretChats) ?: false
        val messages = messageUseCase.createMessageByExtension(fileName!!, fileName, getCurrentChat())
        uploadRepository.uploadFileAndSend(messages, isEncoded, isEncoded)
    }

    fun startSoundRecord() {
        try {
            releasePlayer()
            var dir = "audio"
            *//* FOR INDY CRYPTO
            if (Jid.isJidPeerJid(user.getJid())) {
                 dir = "crypto"
             }*//*
            val outFile: File = FileUtils.getOutputMediaFile(App.getContext(), dir, "record_" + System.currentTimeMillis() + ".wav")
            //val outFile: File = FileUtils.getOutputMediaFile(App.getContext(), dir, "record_test" + ".wav")
            fileName = outFile.absolutePath
            mediaRecorder = MediaRecorder()
            mediaRecorder?.let {
                it.setAudioSource(MediaRecorder.AudioSource.MIC)
                it.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                it.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                it.setOutputFile(fileName)
                it.prepare()
                it.start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun deleteCurrentFileFromFilename() {
        FileUtils.forceDeleteWithoutException(File(fileName ?: ""))
    }

    fun stopSoundRecord() {
        try {
            mediaRecorder?.stop()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun updateLastActivity() {
        val items = lastActivityAllLiveData.value
        items?.forEach {
            activityStatusLiveData.postValue(Pair(it.uid, it.isOnline))
        }

        var countOnline = 0
        val string = if (getCurrentChat()?.isRoom == true) {
            if (items != null && getCurrentChat()?.members != null) {
                items.indices.forEach { i ->
                    for (z in getCurrentChat()?.members!!.indices) {
                        if (items[i].uid == currentChat?.members!![z].jid) {
                            if (items[i].isOnline) {
                                countOnline++
                            }
                        }
                    }
                }
            }
            resourceProvider.getPluralsString(R.plurals.group_members_plurals, getCurrentChat()?.members?.size
                    ?: 0, getCurrentChat()?.members?.size
                    ?: 0) + ", " + countOnline + " " + resourceProvider.getString(R.string.online)
        } else {
            var status: RosterStatusResponse? = null
            if (items != null) {
                for (i in items.indices) {
                    if (items[i].uid == getCurrentChat()?.id) {
                        activityStatusLiveData.postValue(Pair(items[i].uid, items[i].isOnline))
                        status = items[i]
                        break
                    }
                }
            }
            if (status != null) {
                if (status.isOnline) {
                    ""
                } else {
                    if (!status.last_online.isNullOrEmpty()) {
                        val dateString: String = DateUtils.getStringFromDate(status.dateFromLastOnline, "dd.MM.yyyy", false)
                        val timeString: String = DateUtils.getStringFromDate(status.dateFromLastOnline, "HH:mm", false)
                        java.lang.String.format(App.getContext().getString(R.string.last_activity_was),
                                dateString, timeString)
                    } else {
                        ""
                    }
                }
            } else {
                ""
            }
        }
        lastActivityLiveData.postValue(string)
    }

    fun addToFavorite() {
        favoritesRepository.addToFavorites(Favorites.Category.user, getCurrentChat()?.id
                ?: "").observeOnceForDone(this) {
            if (it.status == Status.SUCCESS) {

            } else if (it.status == Status.ERROR) {
                onShowToastLiveData.postValue(resourceProvider.getString(R.string.error_1890))
            }
        }
    }

    fun deleteFromFavorite() {
        favoritesRepository.delFromFavorites(Favorites.Category.user, getCurrentChat()?.id
                ?: "").observeOnceForDone(this) {
            if (it.status == Status.SUCCESS) {

            } else if (it.status == Status.ERROR) {
                onShowToastLiveData.postValue(resourceProvider.getString(R.string.error_1890))
            }
        }
    }

    fun leaveGroupChat() {
        showProgressDialog()
        chatsRepository.deleteRoom(getCurrentChat()?.id ?: "").observeOnce(this) {
            hideProgressDialog()
            if (it == true) {
                finishActivityLiveData.postValue(true)
            } else {
                onShowToastLiveData.postValue(resourceProvider.getString(R.string.error))
            }
        }
    }

    fun onMessageRead(messageId: String, contentType: ConnectionsWrapper.ConnectionType) {
        Log.d("mylog2080", "messageId=" + messageId + " contentType=" + contentType)
        messagesRepository.sendAcknowledgeChatMarker(messageId, getCurrentChat()
                ?: Chats(), contentType)
    }

    fun deleteChatRequest() {
        chatsRepository.deleteAllMessagesForChat(getCurrentChat() ?: Chats()).observeOnce(this) {
            if (it == true) {
                chatsRepository.getAllChats(false)
                onBackClickLiveData.postValue(true)
            }
        }
    }


    fun inviteToSecret() {
        val inviteWrapper: InviteWrapper = Feature0160(true).generateMyInvite()
        val messageInvite = inviteWrapper.indyMessageString
        val messageDid = inviteWrapper.indyMessage.firstRecipient
        val indyTransportMessage = messageUseCase.createIndyTransportMessage(messageInvite, getCurrentChat())
        val gson = Gson()
        val mesString = gson.toJson(indyTransportMessage, MessagesNew::class.java)
        val secretChats = SecretChats(getCurrentChat()?.creatorUser, messageDid, mesString)

        messagesRepository.sendMessageWithOutSave(indyTransportMessage).observeOnceForDone(this) {
            if (it.status == Status.SUCCESS) {
                chatsRepository.createSecretChatWith(secretChats).observeOnce(this) {
                    chatsRepository.getAllChats(true)
                    goToNewSecretChatLiveData.postValue(secretChats)
                }
            } else if (it.status == Status.ERROR) {
                onShowToastLiveData.postValue(resourceProvider.getString(R.string.error))
            }
        }
    }

    private fun mapMessages(messages: List<MessagesNew>): MutableList<IChatItem> {
        val itemsToAdd: MutableList<IChatItem> = ArrayList()

        messages.forEachIndexed { index, mes ->
            if (mes.messageUserType == null) {
                mes.messageUserType = messageUseCase.messageUserTypeByJid(mes.msg_from, mes.contentType)
            }
            val messMaps = when {
                mes.contentType == ContentType.indytransport -> {
                    val wrapper = ConnectionsWrapper.map(mes, getCurrentChat()?.title ?: "")
                    if (wrapper.type == ConnectionsWrapper.ConnectionType.invite) {
                        SecretInviteItem.map(mes, getCurrentChat())
                    } else {
                        ChatConnectionItem.map(wrapper)
                    }
                }
                mes.messageUserType == Incoming
                        || mes.messageUserType == OutComing -> ChatMessageItem.map(mes, getCurrentChat())
                mes.messageUserType == Common -> TechMessageItem.map(mes)
                else -> ChatMessageItem.map(mes, getCurrentChat())
            }

            itemsToAdd.lastOrNull()?.let { prevMessageItem ->
                if (needToAddDateDelimiter(prevMessageItem, messMaps)) {
                    itemsToAdd.add(ChatDateItem(messMaps.timeInMillis))
                }
            }

            itemsToAdd.add(messMaps)
        }
        return itemsToAdd
    }

    private fun updateMessages(items: MutableList<IChatItem>) {
        // removeInviteMessageAfterAdd()
        items.forEach { newItem ->
            val oldIndex = originalMessages.indexOfFirst { it.getMessageId() == newItem.getMessageId() }
            if (oldIndex >= 0) {
                originalMessages[oldIndex] = newItem
                if (oldIndex > 0) messagesUpdateLiveData.postValue(listOf(oldIndex, oldIndex - 1))
                else messagesUpdateLiveData.postValue(listOf(oldIndex))
            }
        }
    }

    private fun deleteMessages(items: MutableList<IChatItem>) {
        //Add to removed itemsList
        val removedItems = originalMessages.filter { item ->
            var isDeleted = false
            items.forEachIndexed { index, iChatItem ->
                if (iChatItem.getMessageId() == item.getMessageId()) {
                    isDeleted = true
                }
            }
            return@filter isDeleted
        }
        //Add to removed dateDelimetr
        val removedItems2: MutableList<IChatItem> = mutableListOf()
        originalMessages.forEachIndexed { index, iChatItem ->
            removedItems.forEach { item ->
                if (iChatItem.getMessageId() == item.getMessageId()) {
                    if ((index - 1) >= 0) {
                        val prevRemovedMess1 = originalMessages.get(index - 1)
                        val isPreDate = prevRemovedMess1 is ChatDateItem
                        val isDelimeterNotNeeded = !needToAddDateDelimiter(prevRemovedMess1, iChatItem) && isPreDate
                        if (isDelimeterNotNeeded) {
                            removedItems2.add(prevRemovedMess1)
                        }
                    }
                }
            }
        }
        originalMessages.removeAll(removedItems)
        originalMessages.removeAll(removedItems2)
        messagesSetLiveData.postValue(0)
        emptyStateLiveData.value = originalMessages.isEmpty()
    }

    private fun setMessages(items: MutableList<IChatItem>) {
        originalMessages.clear()
        originalMessages.addAll(items)
        //FOR TEST
        //   removeInviteMessageAfterAdd()
        messagesSetLiveData.postValue(0)
        emptyStateLiveData.value = originalMessages.isEmpty()
    }

    private fun addMessages(itemsToAdd: MutableList<IChatItem>) {
        if (itemsToAdd.isEmpty()) return

        //берем первое у originalMessages (не дату)
        //берем последнее у itemsToAdd (не дату)
        val originMessage = originalMessages.firstOrNull { it !is ChatDateItem }
        val addedMessage = itemsToAdd.last()
        //в этом странном случае добавляем все
        if (originMessage == null) {
            originalMessages.removeAll { it is ChatDateItem }
            val listToAdd: MutableList<IChatItem> = ArrayList<IChatItem>()
            itemsToAdd.forEach { newItem ->
                val oldIndex = originalMessages.indexOfFirst { it.getMessageId() == newItem.getMessageId() }
                if (oldIndex >= 0) {
                    originalMessages[oldIndex] = newItem
                    //if (oldIndex > 0) messagesUpdateLiveData.postValue(listOf(oldIndex, oldIndex - 1))
                    //else messagesUpdateLiveData.postValue(listOf(oldIndex))
                } else {
                    listToAdd.add(newItem)
                }
            }
            originalMessages.addAll(0, listToAdd)
        } else {
            //если нужно добавить разделитель - добавляем
            if (needToAddDateDelimiter(addedMessage, originMessage)) {
                itemsToAdd.add(ChatDateItem(originMessage.timeInMillis))
            }
            //на всякий случай проверяем - если первый элемент - это дата, то убираем ее
            if (originalMessages.firstOrNull() is ChatDateItem) {
                originalMessages.removeAt(0)
            }
            //добавляем элементы
            val listToAdd: MutableList<IChatItem> = ArrayList<IChatItem>()
            itemsToAdd.forEach { newItem ->
                val oldIndex = originalMessages.indexOfFirst { it.getMessageId() == newItem.getMessageId() }
                if (oldIndex >= 0) {
                    originalMessages[oldIndex] = newItem
                    //if (oldIndex > 0) messagesUpdateLiveData.postValue(listOf(oldIndex, oldIndex - 1))
                    //else messagesUpdateLiveData.postValue(listOf(oldIndex))
                } else {
                    listToAdd.add(newItem)
                }
            }
            originalMessages.addAll(0, listToAdd)
            removeInviteMessageAfterAdd()
            //  originalMessages.addAll(0, itemsToAdd)
        }
        messagesAddRangeLiveData.postValue(Pair(0, itemsToAdd.size))
    }

    private fun addMessage(itemsToAdd: MutableList<IChatItem>) {
        val originMessage = originalMessages.lastOrNull()
        val addedMessage = itemsToAdd.first()
        //если нужно добавить разделитель - добавляем
        if (needToAddDateDelimiter(originMessage, addedMessage)) {
            itemsToAdd.add(0, ChatDateItem(addedMessage.timeInMillis))
        }
        //добавляем элементы если такие есть уже то обновлеям их
        itemsToAdd.forEach { newItem ->
            val oldIndex = originalMessages.indexOfFirst { it.getMessageId() == newItem.getMessageId() }
            if (oldIndex >= 0) {
                originalMessages[oldIndex] = newItem
                if (oldIndex > 0) messagesUpdateLiveData.postValue(listOf(oldIndex, oldIndex - 1))
                else messagesUpdateLiveData.postValue(listOf(oldIndex))
            } else {
                originalMessages.add(newItem);
            }
        }
        //   originalMessages.addAll(itemsToAdd)
        val index = originalMessages.indexOf(itemsToAdd.first())
        removeInviteMessageAfterAdd()

        messagesAddLiveData.postValue(Triple(index, itemsToAdd.size,
                addedMessage is ChatMessageItem && addedMessage.isMine))
    }

    private fun removeInviteMessageAfterAdd() {
        if (getCurrentChat() is SecretChats) {
            val secretChat = getCurrentChat() as SecretChats
            if (originalMessages.size > 1) {
                val gson = Gson()
                val inviteMess: MessagesNew? = gson.fromJson(secretChat.inviteMessage, MessagesNew::class.java)
                if (originalMessages[0].getMessageId() == inviteMess?.id) {
                    originalMessages.removeAt(0)
                }
            }
        }
    }

    private fun needToAddDateDelimiter(prevMessageItem: IChatItem?, currentMessageItem: IChatItem): Boolean {
        val prevSentDate = Calendar.getInstance().apply {
            timeInMillis = prevMessageItem?.timeInMillis?.toLong() ?: 0
        }
        val nextSentDate = Calendar.getInstance().apply { timeInMillis = currentMessageItem.timeInMillis.toLong() }

        return (nextSentDate.get(Calendar.DAY_OF_MONTH) != prevSentDate.get(Calendar.DAY_OF_MONTH) ||
                nextSentDate.get(Calendar.MONTH) != prevSentDate.get(Calendar.MONTH) ||
                nextSentDate.get(Calendar.YEAR) != prevSentDate.get(Calendar.YEAR))
    }

    fun onMessageShortClick(message: ChatMessageItem) {
        if (message.status == ChatMessageStatus.error && message.isMine) {
            messageActionLiveData.value = listOf(
                    MessageActionItem(RESEND, message, this::onResendMessage),
                    MessageActionItem(MessageActionItemType.DELETE, message, this::onDeleteMessage)
            )
        }
    }

    fun onMessageLongClick(message: ChatMessageItem) {
        if (message.status != ChatMessageStatus.error) {
            messageActionLiveData.value = listOf(
                    MessageActionItem(DELETE_LOCALLY, message, this::onDeleteMessage)
                    //,MessageActionItem(QUOTE, message, this::onDeleteMessage)
            )
        }
    }

    fun onMessageCancelDownloadUploadClick(message: ChatMessageItem) {
        if (!message.isCanceled) {
            cancelDownloadUploadMessage(message)
        } else {
            retryUploadDownload(message)
        }
    }

    fun cancelDownloadUploadMessage(message: ChatMessageItem) {
        val messFromDb: MessagesNew? = messagesRepository.readMessageWith(message.id)
        messFromDb?.let {
            if (message.isMine) {
                messagesRepository.cancelUploadMessage(messFromDb)
            } else {
                messagesRepository.cancelDownloadMessage(messFromDb)
            }
        }
    }

    fun retryUploadDownload(message: ChatMessageItem) {
        val messFromDb: MessagesNew? = messagesRepository.readMessageWith(message.id)
        messFromDb?.let {
            if (message.isMine) {
                messFromDb.isCanceled = false
                val isEncoded = (getCurrentChat() is SecretChats) ?: false
                uploadRepository.uploadFileAndSend(it,
                        isEncoded, isEncoded)
            } else {
                downloadRepository.startDownloadAfterRefresh(listOf(it), true)
            }
        }
    }

    fun onConnectionClick(message: ChatConnectionItem) {
        goToConnectionLiveData.value = message.wrapper
    }

    fun onAcceptInviteClick(v: View?) {
        if (getCurrentChat() is SecretChats) {
            val secretChat = (getCurrentChat() as SecretChats)
            secretChat.isAccepted = true
            if (secretChat.getTempInviteMessage() != null) {
                secretChat.setInviteMessage(secretChat.getTempInviteMessage())
            }

            secretChat.isTempNotAccepted = false
            chatsRepository.updateChatsInDb(secretChat)
            showHideAcceptedBtn()
            secretChat.setTempInviteMessage(null)
            val gson = Gson()
            val inviteMess = gson.fromJson(secretChat.inviteMessage, MessagesNew::class.java)
            messageListenerUseCase.parseBaseMessages(inviteMess, ContentType.indytransport.seriliazideName, ConnectionProtocol.MESSAGE_CONTENT_TYPE)
        }
    }

    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    public var fileName: String? = null

    private fun releasePlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun onResendMessage(message: ChatMessageItem) {
        messagesRepository.resendMessage(message.id, getCurrentChat()?.id ?: "")
    }

    private fun onDeleteMessage(message: ChatMessageItem) {
        messagesRepository.deleteMessageWithId(message.id, getCurrentChat()?.id ?: "")
    }


    fun botButtonOnclick(botButton: BotButton) {
        if (botButton.message != null) {
            if (botButton.message.timing != null) {
                if (botButton.message.timing.isDateExpiries) {
                    onShowToastLiveData.postValue(resourceProvider.getString(R.string.question_answer_time_expired))
                    return
                }
            }
            val answerMessage = AnswerMessage()
            answerMessage.response = botButton.command
            val responseThread = ResponseThread()
            responseThread.thid = botButton.message.id
            answerMessage.responseThread = responseThread
            answerMessage.timing = TimingAnswer(Date(System.currentTimeMillis() + 5 * 60 * 1000))
            try {
                if (botButton.message.isSignature_required) {
                    //TODO заменить на didiTo
                    val pairwise_info = Pairwise.getPairwise(IndyWallet.getMyWallet(), (getCurrentChat() as? SecretChats)?.indyDidTo).get()
                    val gson = Gson()
                    val pairwiseInfoGson: PairwiseMetadata = gson.fromJson(pairwise_info, PairwiseMetadata::class.java)
                    val pairwiseMeta: DidTheirMetdata = pairwiseInfoGson.didMetada
                    val myDid: String = pairwiseInfoGson.my_did
                    val connectionResponse = AnswerResponseSig()
                    val messageString: String = botButton.message.question_text + botButton.command + botButton.message.nonce
                    //connectionResponse.set
                    // "sig_data": "<base64(HASH("Alice, are you on the phone with Bob?"+"Yes, it's me"+"<nonce>"))>"
                    val timestamp = System.currentTimeMillis() / 1000
                    val timestampBytes = Utils.longToBytes(timestamp)
                    val messageBytes = messageString.toByteArray()
                    val baos = ByteArrayOutputStream()
                    baos.write(timestampBytes)
                    baos.write(messageBytes)
                    val sigDataBytes = baos.toByteArray()
                    val sigData = android.util.Base64.encode(sigDataBytes, android.util.Base64.NO_WRAP or android.util.Base64.URL_SAFE)
                    val signatureBytes = Crypto.cryptoSign(IndyWallet.getMyWallet(), pairwiseMeta.connection_key, sigDataBytes).get()
                    val signature = android.util.Base64.encode(signatureBytes, android.util.Base64.NO_WRAP or android.util.Base64.URL_SAFE)
                    connectionResponse.sig_data = String(sigData)
                    connectionResponse.type = ConnectionProtocol.BASE_PROTOCOL + "signature/1.0/ed25519Sha512_single"
                    connectionResponse.signature = String(signature)
                    connectionResponse.setSigners(pairwiseMeta.connection_key)
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            sendAnswerMessageText(answerMessage.deSerialize())
        } else {
            sendMessageText(botButton.command)
        }
    }


    private fun checkIndyExist() {
        createDID()
    }

    private fun createDID() {
        var did = "did:sov:"
        val uuid = UUID.randomUUID()
        val uuidRepresentation = uuid.toString().replace("-", "")
        val b2 = BigInteger(uuidRepresentation, 16).toByteArray()
        val encodedDid = Base58.encode(b2)
        did += encodedDid
        val rosterDid: RosterDid? = DaoUtilsRoster.readRosterDid(getCurrentChat()?.id
                ?: "")
        if (rosterDid != null) {
            if (!TextUtils.isEmpty(rosterDid.getRosterDid())) {
                did = rosterDid.getRosterDid()
            }
        }
        val finalDid = did
        if (TextUtils.isEmpty(finalDid)) {
            return
        }
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful || task.result == null) {
                return@OnCompleteListener
            }
            // Get new Instance ID token
            val token = task.result
            if (!TextUtils.isEmpty(token)) {
                Api.getIndyApi().createIndy(IndyRequest(finalDid,token)).enqueue(object : Callback<IndyRequest?> {
                    override fun onResponse(call: Call<IndyRequest?>, response: Response<IndyRequest?>) {
                        if (response.code() == 201) {

                            // user.setStatus(finalDid);
                            //   DaoUtils.updateRosterIndy(user);
                            val rosterDid1 = RosterDid()
                            rosterDid1.setRosterJid(getCurrentChat()?.id ?: "")
                            rosterDid1.setRosterDid(finalDid)
                            DaoUtilsRoster.writeRosterDid(rosterDid1)
                            //    Log.d("mylog900", "createDID did=" + user.getStatus());
                            //     RosterUser rosterUser = DaoUtils.readRosterUser(user);
                            //     BroadcastUtils.sendBroadcast(context, BroadcastUtils.BroadcastType.NotifyChatsChanged);
                        }
                    }

                    override fun onFailure(call: Call<IndyRequest?>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
            }
        })

    }


*//*   fun hideShowBotView(url: String?, user: RosterUser?) {
       val intent = Intent(App.getContext(), WebActivity::class.java)
       intent.putExtra("user", user)
       intent.putExtra("url", url)
       context.startActivity(intent)
   }*//*

*//*chatMessageText.setMovementMethod(LocalLinkMovementMethod.getInstance { url -> hideShowBotView(url, messageItem.getUser()) })*//*

    open fun generateButtonViewsFromQuestion(messagesNew: MessagesNew) {
        val gson = Gson()
        val botButtonList: MutableList<BotButton> = ArrayList<BotButton>()
        val questionMessage = gson.fromJson(messagesNew.json, QuestionMessage::class.java)

        if (questionMessage != null) {
            if (questionMessage.valid_responses != null) {
                if (questionMessage.valid_responses.size > 0) {
                    for (i in 0 until questionMessage.valid_responses.size) {
                        val command: String = questionMessage.valid_responses[i].text
                        botButtonList.add(BotButton(command, questionMessage))
                    }
                }
            }
            if (questionMessage.timing?.isDateExpiries == true || messagesNew.isAccepted) {
                botButtonList.clear()
                showHideMenu()
            }
            if (botButtonList.size > 0) {
                startTimerForQuestionMenu(messagesNew)
            }
        }
        bottomBotButtonList.postValue(botButtonList)

    }


    fun generateButtonViewsFromJson(jsonbot: String?) {
        var botButtonList: List<BotButton> = ArrayList<BotButton>()
        try {
            if (!jsonbot.isNullOrEmpty()) {
                botButtonList = Utils.GSON.fromJson<List<BotButton>>(jsonbot, object : TypeToken<List<BotButton?>?>() {}.type)
            }
        } catch (e: Exception) {

        }
        bottomBotButtonList.postValue(botButtonList)
    }

    fun showHideMenu() {
        val user: RosterUser? = DaoUtilsRoster.readRosterUser(getCurrentChat()?.id)
        if (user?.isRobot == true) {
            checkIndyExist()
        }
        generateButtonViewsFromJson(user?.menu ?: "")
    }

    fun showHideQuestionMenu(messagesNew: MessagesNew?) {
        if (messagesNew == null) {
            showHideMenu()
            return
        }
        if (messagesNew.contentType == ContentType.services) {
            if (!messagesNew.json.isNullOrEmpty()) {
                generateButtonViewsFromQuestion(messagesNew)
            } else {
                showHideMenu()
            }
        } else {
            showHideMenu()
        }
    }

    var timer = Timer()
    val credProofTimerList = HashMap<String, Timer>()

    private fun cancelTimer() {
        timer.cancel()
    }

    private fun cancelAllCredProofTimer() {
        credProofTimerList.forEach {
            it.value.cancel()
        }
        credProofTimerList.clear()
    }


    private fun startTimerForCredentialProofMessage1(messagesList: List<IChatItem>) {
        messagesList.forEach {
            if (it is ChatConnectionItem) {
                val timing = it.wrapper.expireTime
                if (timing != null) {
                    if (!timing.isDateExpiries) {
                        val messId = it.getMessageId()
                        var timer = credProofTimerList[messId]
                        timer?.cancel()
                        timer = Timer()
                        credProofTimerList[messId] = timer
                        timer.schedule(object : TimerTask() {
                            override fun run() {
                                messagesUpdateLiveData.postValue(listOf())
                            }
                        }, timing.dateFromTimingString)
                    }
                }
            }
        }
    }


    private fun startTimerForQuestionMenu(messagesNew: MessagesNew) {
        cancelTimer()
        val gson = Gson()
        val questionMessage = gson.fromJson(messagesNew.json, QuestionMessage::class.java)
        val date: Date? = questionMessage.timing?.dateFromTimingString
        if (date != null) {
            timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    messagesRepository.updateMessageInLiveData(messagesNew)
                    showHideQuestionMenu(messagesNew)
                }
            }, date)
        }
    }

    fun removeUser() {
        userRepository.removeUserFromRoster()
    }

    fun addUserToContacts() {
        userRepository.addUserToManuallyGroup(getCurrentChat()?.id)
    }

    fun sendTestProposeCredential() {
        val mesForDb = messageUseCase.createTextMessage("/propose_credential_test", getCurrentChat())
        if (getCurrentChat() is SecretChats) {
            messagesRepository.handleSendIndyMessages(mesForDb)
            return
        }*/
   // }
}