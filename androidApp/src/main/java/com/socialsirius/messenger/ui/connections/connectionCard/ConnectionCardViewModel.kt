package com.socialsirius.messenger.ui.connections.connectionCard

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.Log

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.sirius.library.utils.ExceptionHandler
import com.sirius.library.utils.ExceptionListener
import com.socialsirius.messenger.R
import com.socialsirius.messenger.base.App

import com.socialsirius.messenger.base.ui.BaseViewModel
import com.socialsirius.messenger.models.ui.ItemCredentials
import com.socialsirius.messenger.ui.chats.chat.message.BaseItemMessage
import com.socialsirius.messenger.ui.chats.chat.message.OfferItemMessage
import com.socialsirius.messenger.ui.chats.chat.message.ProverItemMessage
import com.socialsirius.messenger.ui.connections.connectionCard.items.DetailsBaseItem
import com.socialsirius.messenger.utils.DateUtils
import kotlinx.android.synthetic.main.item_chat_connection.view.*
import java.util.concurrent.TimeoutException
import javax.inject.Inject


class ConnectionCardViewModel @Inject constructor() : BaseViewModel() {


    val connectionDetailsLiveData = MutableLiveData<List<DetailsBaseItem>>()
    val connectionUserLiveData = MutableLiveData<String>()
    val connectionDateLiveData = MutableLiveData<String>()
    val connectionTypeLiveData = MutableLiveData<String>()
    val connectionStatusLiveData = MutableLiveData<String>()
    val connectionDescriptionLiveData = MutableLiveData<String>()
    val errorLiveData = MutableLiveData<String?>()
    val commentLiveData = MutableLiveData<String?>()
    val showDetailsLiveData = MutableLiveData<Boolean>()
    val detailsTitleLiveData = MutableLiveData<String>()
    val topViewColorLiveData = MutableLiveData<ColorStateList>()
    val topIconLiveData = MutableLiveData<Drawable>()
    val topIconPaddingLiveData = MutableLiveData<Int>()

    val showAction1LiveData = MutableLiveData<String?>()
    val showAction2LiveData = MutableLiveData<String?>()
    val showAction3LiveData = MutableLiveData<String?>()

    val action2LiveData = MutableLiveData<Boolean>()
    val messageSuccessLiveData = MutableLiveData<Boolean?>()
    val messageErrorLiveData = MutableLiveData<Boolean?>()
    val messageStartObservLiveData = MutableLiveData<Boolean?>()

    var item: BaseItemMessage? = null
    var titleUser : String? = null
    val exceptionListener : ExceptionListener = object : ExceptionListener {
        override fun handleException(e: Throwable) {
            if(e is TimeoutException){
                onShowAlertDialogLiveData.postValue(Pair("An error has occurred" , "Timed out waiting for a response. Please try another time."))
            }
           // Log.d("mylog2090","handleException = ${e}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ExceptionHandler.removeOneTimeListener(exceptionListener)
    }
    fun onAction1ButtonClick(v: View?) {
        ExceptionHandler.addOneTimeListener(exceptionListener)
        item?.accept()
    }

    fun onAction2ButtonClick(v: View?) {
        action2LiveData.postValue(true)
    }

    fun onAction3ButtonClick(v: View?) {
        item?.cancel()
    }

    override fun setupViews() {
        super.setupViews()
        fillAll()
        item?.notifyDataListener = object : BaseItemMessage.NotifyDataListener {
            override fun notifyData() {

            }
            override fun notifyItem(item1: BaseItemMessage) {
                if (item1.isLoading) {
                    showProgressDialog()
                } else {
                    hideProgressDialog()
                    item = item1
                    fillAll()
                }
            }

        }


    }

    fun fillAll() {
        item?.let {
            fillBase(it)
            if (it is OfferItemMessage) {
                fillOffer(it)
            } else if (it is ProverItemMessage) {
                fillProver(it)
            }else if (it is ItemCredentials) {
                fillCredentials(it)
            }
        }
    }



    fun fillBase(item: BaseItemMessage) {
        connectionUserLiveData.postValue(titleUser)
        connectionStatusLiveData.postValue(item.getTitle())
        connectionDescriptionLiveData.postValue(item.getText())
        connectionDateLiveData.postValue(DateUtils.getStringFromDate(item.date,DateUtils.PATTERN_DATETIME_DOT,false))
        if (item.isError){
            errorLiveData.postValue(item.errorString)
        }
        if (item.commentString!=null){
            commentLiveData.postValue(item.commentString)
        }
        var action1Text: String? = null
        var action2Text: String? = "Details"
        var action3Text: String? = null
        val dateTime = DateUtils.getStringFromDate(item.date,DateUtils.PATTERN_DATETIME_DOT,false)
        val status = item.getStatusString()
        if (item.isAccepted) {
            connectionDateLiveData.postValue("$status at $dateTime")
        } else if (item.isError) {
            connectionDateLiveData.postValue("$status at $dateTime")
        } else {
            connectionDateLiveData.postValue(status)
            action1Text = "Accept"
            action3Text = "Cancel"
        }
        showAction1LiveData.postValue(action1Text)
        showAction2LiveData.postValue(action2Text)
        showAction3LiveData.postValue(action3Text)

    }

    fun fillProver(item: ProverItemMessage) {
        connectionTypeLiveData.postValue("Verification")
        connectionDetailsLiveData.postValue(item.detailList)


        topViewColorLiveData.postValue(App.getContext().resources.getColorStateList(R.color.cardConnectionCredentials))
        topIconLiveData.postValue(App.getContext().resources.getDrawable(R.drawable.ic_connection_verification))
        //  itemView.connectionMessage.backgroundTintList = App.getContext().resources.getColorStateList(R.color.cardConnectionCredentials)
        //     itemView.connectionIcon.setImageDrawable(App.getContext().resources.getDrawable(R.drawable.ic_connection_verification))
        //   itemView.connectionIcon.backgroundTintList = App.getContext().resources.getColorStateList(R.color.cardConnectionCredentials)

    }

    fun fillOffer(item: OfferItemMessage) {
        connectionTypeLiveData.postValue("Credentials")
        connectionDetailsLiveData.postValue(item.detailList)

        topViewColorLiveData.postValue(
            App.getContext().resources.getColorStateList(
                R.color.cardOrange
            )
        )
        topIconLiveData.postValue(App.getContext().resources.getDrawable(R.drawable.ic_connection_credentials2))
    }

    fun fillCredentials(item: ItemCredentials){
        showAction2LiveData.postValue(null)
        connectionTypeLiveData.postValue("Credentials")
        connectionDetailsLiveData.postValue(item.detailList)
        topViewColorLiveData.postValue(
            App.getContext().resources.getColorStateList(
                R.color.cardOrange
            )
        )
        topIconLiveData.postValue(App.getContext().resources.getDrawable(R.drawable.ic_connection_credentials2))


    }

}

/*

class ConnectionCardViewModel @Inject constructor(
        resourcesProvider: ResourcesProvider,
        val messagesRepository: MessagesRepository,
        val appExecutors: AppExecutors,
        val utilsRepository: UtilsRepository,
        val chatsRepository: ChatsRepository
) : BaseViewModel(resourcesProvider) {

    private var connection: ConnectionsWrapper? = null
    val connectionDetailsLiveData = MutableLiveData<List<Any>>()
    val connectionUserLiveData = MutableLiveData<String>()
    val connectionDateLiveData = MutableLiveData<String>()
    val connectionTypeLiveData = MutableLiveData<String>()
    val connectionStatusLiveData = MutableLiveData<String>()
    val connectionDescriptionLiveData = MutableLiveData<String>()
    val errorLiveData = MutableLiveData<String>()
    val showDetailsLiveData = MutableLiveData<Boolean>()
    val detailsTitleLiveData = MutableLiveData<String>()
    val topViewColorLiveData = MutableLiveData<ColorStateList>()
    val topIconLiveData = MutableLiveData<Drawable>()
    val topIconPaddingLiveData = MutableLiveData<Int>()

    val showAction1LiveData = MutableLiveData<String>()
    val showAction2LiveData = MutableLiveData<String>()
    val showAction3LiveData = MutableLiveData<String>()
    val action3LiveData = MutableLiveData<ConnectionDetailsWrapper>()
    val messageSuccessLiveData = MutableLiveData<Boolean?>()
    val messageErrorLiveData = MutableLiveData<Boolean?>()
    val messageStartObservLiveData = MutableLiveData<Boolean?>()
    val messagesInDBLiveData = messagesRepository.messagesInDBLiveData
    override fun onViewCreated() {
        super.onViewCreated()

        connection?.let {
            fillCard(it)
        }
    }


    private fun mapWorkspacesToInfo(attributes: List<AttributesAttach>): MutableList<DetailsInfoItem> {
        val itemsToAdd: MutableList<DetailsInfoItem> = ArrayList()
   */
/*     val infoItemContacts = DetailsInfoItem("Количество контактов","10")
        itemsToAdd.add(infoItemContacts)

        val infoItemGroups = DetailsInfoItem("Количество групповых чатов","10")
        itemsToAdd.add(infoItemGroups)*//*

      */
/*  attributes.forEachIndexed { index, attr ->
            val infoItem = DetailsInfoItem.map(attr)
            itemsToAdd.add(infoItem)
        }*//*

        return itemsToAdd
    }

    private fun mapAttributesToInfo(attributes: List<AttributesAttach>): MutableList<DetailsInfoItem> {
        val itemsToAdd: MutableList<DetailsInfoItem> = ArrayList()
        attributes.forEachIndexed { index, attr ->
            val infoItem = DetailsInfoItem.map(attr)
            itemsToAdd.add(infoItem)
        }
        return itemsToAdd
    }

    private fun mapAttributesToProof(attributes: List<FirebaseIndyFields>): MutableList<DetailsBaseItem> {
        val itemsToAdd: MutableList<DetailsBaseItem> = ArrayList()
        attributes.forEachIndexed { index, attr ->
            if (attr.value.isNullOrEmpty()) {
                val infoItem = DetailsErrorItem.map(attr)
                itemsToAdd.add(infoItem)
            } else {
                val infoItem = DetailsSuccessItem.map(attr)
                itemsToAdd.add(infoItem)
            }

        }
        return itemsToAdd
    }

    fun fillCard(connection: ConnectionsWrapper) {

        connectionUserLiveData.value = connection.userName.orEmpty()
        connectionStatusLiveData.value = connection.credName.orEmpty()
        connectionDescriptionLiveData.value = connection.comment
        val creentialtitle = App.getContext().getString(R.string.credential_message).replace("[", "").replace("]", "");
        val verification = App.getContext().getString(R.string.proof_request_message).replace("[", "").replace("]", "");
        val workspaces = App.getContext().getString(R.string.workspaces_message).replace("[", "").replace("]", "");
        val orders = App.getContext().getString(R.string.orders_message).replace("[", "").replace("]", "");

        if (connection.type == ConnectionsWrapper.ConnectionType.proofrequest) {
            val items = mapAttributesToProof(connection.indyFields)
            connectionDetailsLiveData.value = items
            topViewColorLiveData.value = App.getContext().resources.getColorStateList(R.color.cardConnectionCredentials)
            topIconLiveData.value = App.getContext().resources.getDrawable(R.drawable.ic_connection_verification)

            connectionTypeLiveData.value = verification
            detailsTitleLiveData.value = resourceProvider.getString(R.string.proof_request_details).toUpperCase()
        } else if (connection.type == ConnectionsWrapper.ConnectionType.credentilas) {
            val items = mapAttributesToInfo(connection.fileds)
            connectionDetailsLiveData.value = items
            topViewColorLiveData.value = App.getContext().resources.getColorStateList(R.color.cardOrange)
            topIconLiveData.value = App.getContext().resources.getDrawable(R.drawable.ic_connection_credentials2)
            connectionTypeLiveData.value = creentialtitle
            detailsTitleLiveData.value = resourceProvider.getString(R.string.proof_request_details).toUpperCase()
        }else if(connection.type == ConnectionsWrapper.ConnectionType.workspaces){
            val items = mapWorkspacesToInfo(connection.fileds)
            connectionDetailsLiveData.value = items
            topViewColorLiveData.value = App.getContext().resources.getColorStateList(R.color.cardConnectionGreen)
            topIconLiveData.value = App.getContext().resources.getDrawable(R.drawable.ic_connection_chat)
            connectionTypeLiveData.value = workspaces
            detailsTitleLiveData.value = resourceProvider.getString(R.string.proof_request_details).toUpperCase()
        }else if(connection.type == ConnectionsWrapper.ConnectionType.orders){
            val items = mapAttributesToInfo(connection.fileds)
            connectionDetailsLiveData.value = items
            topViewColorLiveData.value = App.getContext().resources.getColorStateList(R.color.cardBlue)
            topIconLiveData.value = App.getContext().resources.getDrawable(R.drawable.ic_connection_credentials2)
            connectionTypeLiveData.value = orders
            detailsTitleLiveData.value = resourceProvider.getString(R.string.proof_request_details).toUpperCase()
        }

        //Для proofrequest комментарий при отказе
        */
/*credStatus.setText(R.string.proof_request_error);
        String commnet = "";
        if (!TextUtils.isEmpty(messages.getCommentCode())) {
            commnet = messages.getCommentCode();
        }
        if (!TextUtils.isEmpty(commnet) && !TextUtils.isEmpty(messages.getComment())) {
            commnet = commnet + " : " + messages.getComment();
        }
        credName.setText(commnet);
        *//*

        var status = ""
        if (connection.status == ConnectionsWrapper.ConnectionStatus.not_accepted) {
            if (connection.type == ConnectionsWrapper.ConnectionType.proofrequest) {
                status = App.getContext().getString(R.string.proof_request_error)
                showAction1LiveData.value = resourceProvider.getString(R.string.proof_request_send_proof_btn)
            } else {
                showAction1LiveData.value = resourceProvider.getString(R.string.accept)
                status = App.getContext().getString(R.string.accepted_not)
            }
            showAction2LiveData.value = resourceProvider.getString(R.string.cancel)
            if (connection.type == ConnectionsWrapper.ConnectionType.workspaces) {
                showAction2LiveData.value = null
                showAction3LiveData.value = resourceProvider.getString(R.string.proof_request_details)
            }
        } else if (connection.status == ConnectionsWrapper.ConnectionStatus.accepted) {
            if (connection.type == ConnectionsWrapper.ConnectionType.proofrequest) {
                status = App.getContext().getString(R.string.proof_request_success)
            } else {
                status = App.getContext().getString(R.string.accepted)
            }
            showAction1LiveData.value = null
            showAction2LiveData.value = null
            showAction3LiveData.value = resourceProvider.getString(R.string.proof_request_details)
         */
/*  if (connection.type == ConnectionsWrapper.ConnectionType.workspaces) {
                showAction2LiveData.value =  resourceProvider.getString(R.string.workspaces_leave)
            }*//*

        } else if (connection.status == ConnectionsWrapper.ConnectionStatus.canceled) {
            if (connection.type == ConnectionsWrapper.ConnectionType.proofrequest) {
                status = App.getContext().getString(R.string.proof_request_error)
            } else {
                status = App.getContext().getString(R.string.canceled)
            }
            showAction1LiveData.value = null
            showAction2LiveData.value = null
            showAction3LiveData.value = resourceProvider.getString(R.string.proof_request_details)
            topViewColorLiveData.value = App.getContext().resources.getColorStateList(R.color.errorColor)
            errorLiveData.value = connection.errorComment ?: ""

        }
        connectionDateLiveData.value = status + ":" + connection.parseToDateTime()

        if (!connection.isAllCredInside) {
            errorLiveData.value = resourceProvider.getString(R.string.proof_request_cred_not_good)
            showAction3LiveData.value = resourceProvider.getString(R.string.proof_request_details)
            showAction1LiveData.value = null
        }

    }


    fun setConnection(connection: ConnectionsWrapper?) {
        this.connection = connection
    }

    fun onRadioClick(item: DetailsRadioItem) {

    }

    fun onCheckboxCLick(item: DetailsCheckboxItem) {

    }

    fun onAction1ButtonClick(v: View?) {
        if(connection?.isTimeOut() == true){
            onShowToastLiveData.postValue(resourceProvider.getString(R.string.proof_request_time_expire))
            return
        }
        if (connection?.type == ConnectionsWrapper.ConnectionType.proofrequest) {
            messagesInDBLiveData.value = MessageActionResourse.wait()
            sendProof()
        } else if (connection?.type == ConnectionsWrapper.ConnectionType.credentilas) {
            createCredIdConnection(connection?.messageRef?.indy_transport
                    ?: "", connection?.credDefId ?: "")
        }else if(connection?.type == ConnectionsWrapper.ConnectionType.workspaces){
            showProgressDialog()
            utilsRepository.getDynamicUrl(connection?.invitation).observeOnce(this){
                hideProgressDialog()
                if(it==false){
                    onShowToastLiveData.postValue(resourceProvider.getString(R.string.error))
                }else{
                    Log.d("mylog2000","getDynamicUrl response it="+it)
                    val messages1: MessagesNew? = messagesRepository.readMessageWith(connection?.id ?: "");
                    messages1?.isAccepted = true
                    messagesRepository.changeCredProofStatusMessageInDB(messages1)
                    onBackClickLiveData.postValue(true)
                }
            }
        }
        else if(connection?.type == ConnectionsWrapper.ConnectionType.orders){
            val messages1: MessagesNew? = messagesRepository.readMessageWith(connection?.id ?: "");
            messages1?.isAccepted = true
            messagesRepository.changeCredProofStatusMessageInDB(messages1)
            onBackClickLiveData.postValue(true)
        }
    }

    fun onAction2ButtonClick(v: View?) {
        if(connection?.isTimeOut() == true){
            onShowToastLiveData.postValue(resourceProvider.getString(R.string.proof_request_time_expire))
            return
        } else if(connection?.type == ConnectionsWrapper.ConnectionType.workspaces){
          */
/*  chatsRepository.leaveWorkspaces(connection?.credDefId ).observeOnce(this){
                if(it == false){
                    onShowToastLiveData.postValue(resourceProvider.getString(R.string.error))
                }else{
                    val messages1: MessagesNew? = messagesRepository.readMessageWith(connection?.id ?: "");
                    messages1?.isAccepted = false
                    messagesRepository.changeCredProofStatusMessageInDB(messages1)
                    onBackClickLiveData.postValue(true)
                }
            }*//*

        }else{
            showProgressDialog()
            sendProblem(connection?.did1
                    ?: "", connection?.messageRef, Feature0035.REQUEST_NOT_ACCEPTED, "User manually cancel operation");
        }
    }

    fun onAction3ButtonClick(v: View?) {
        val gson = Gson()
        var text = ""
        val indyCipherResponse: IndyCipherResponse? = gson.fromJson(connection?.messageRef?.indy_transport
                ?: "", IndyCipherResponse::class.java)
        if (indyCipherResponse != null) {
            val message: String? = indyCipherResponse.getMessage()
            if (connection?.type == ConnectionsWrapper.ConnectionType.proofrequest) {
                val offerCredentialMessage = gson.fromJson(message, MessageRequestPresentation::class.java)
                text = offerCredentialMessage.deSerializePretty();
            } else if (connection?.type == ConnectionsWrapper.ConnectionType.credentilas) {
                val offerCredentialMessage = gson.fromJson(message, MessageOfferCredential::class.java);
                text = offerCredentialMessage.deSerializePretty();
            }else if (connection?.type == ConnectionsWrapper.ConnectionType.orders) {
                val offerCredentialMessage = gson.fromJson(message, MessageOfferCredential::class.java);
                text = offerCredentialMessage.deSerializePretty();
            }else if (connection?.type == ConnectionsWrapper.ConnectionType.workspaces) {
                val offerCredentialMessage = gson.fromJson(message, WorkspacesMessage::class.java);
                text = offerCredentialMessage?.deSerializePretty() ?:""
            }
        }

        var icon = R.drawable.ic_group_work
        var color = R.color.cardViolet
        if (connection?.type == ConnectionsWrapper.ConnectionType.proofrequest) {
            color = R.color.cardConnectionCredentials
            icon = R.drawable.ic_connection_verification
        }
        if (connection?.type == ConnectionsWrapper.ConnectionType.credentilas) {
            color = R.color.cardOrange
            icon = R.drawable.ic_connection_credentials2
        }
        if (connection?.type == ConnectionsWrapper.ConnectionType.workspaces) {
            color = R.color.cardConnectionGreen
            icon = R.drawable.ic_connection_chat
        }
        if (connection?.type == ConnectionsWrapper.ConnectionType.orders) {
            color = R.color.cardBlue
            icon = R.drawable.ic_connection_credentials2
        }
        if (connection?.status == ConnectionsWrapper.ConnectionStatus.canceled) {
            color = R.color.errorColor

        }
        val name = connection?.credName

        val details = ConnectionDetailsWrapper(text, name, color, icon)
        action3LiveData.postValue(details)
    }

    private fun createCredIdConnection(finalData: String, credDefId: String) {
        showProgressDialog()
        try {
            Feature0036(true).handleOfferCredential(connection?.did1, connection?.messageRef?.id, finalData, credDefId, object : ConnectionProtocol.OnSendIndyMessage {
                override fun onSucces() {
                    val messages: MessagesNew? = messagesRepository.readMessageWith(connection?.messageRef?.id ?: "");
                    if (messages != null) {
                        messages.setAccepted(true);
                        val gson = GsonBuilder().disableHtmlEscaping().create();
                        val messageData = gson.toJson(messages, MessagesNew::class.java);
                        IndyWallet.storeCredDefMessage(credDefId, messageData);

                        messagesRepository.changeCredProofStatusMessageInDB(messages)
                    }
                    hideProgressDialog()
                    onBackClickLiveData.postValue(true)

                }


                override fun onFail() {
                    onShowToastLiveData.postValue(resourceProvider.getString(R.string.error))
                    hideProgressDialog()
                }


                override fun onProblemReport(code: String, comment: String) {
                    val messages: MessagesNew? = messagesRepository.readMessageWith(connection?.messageRef?.id ?: "");
                    sendProblem(connection?.did1 ?: "", messages, code, comment);
                }
            });
        } catch (e: IndyException) {
            e.printStackTrace();
        } catch (e: ExecutionException) {
            e.printStackTrace();
        } catch (e: InterruptedException) {
            e.printStackTrace();
        }
    }

    public fun sendProof() {
        // progressBar2.setVisibility(View.VISIBLE);
        showProgressDialog()
        var isSended = true;
        try {
            //   String masterSecret = "test";
            val masterSecret = HashUtils.generateHash(AppPref.getInstance().getUserResourses());
            var proof = "";

            // Utils.logLongText("mylog2080", "schemaJson=" + connection?.schemaJson);
            //  Utils.logLongText("mylog2080", "credDefJson=" + connection?.credDefJson);
            //  Utils.logLongText("mylog2080", "proofRequest=" + connection?.proofRequest);
            //  Utils.logLongText("mylog2080", "prover_requested_creds=" + connection?.prover_requested_creds);

            proof = Anoncreds.proverCreateProof(IndyWallet.getMyWallet(), connection?.proofRequest, connection?.prover_requested_creds.toString(),
                    masterSecret, connection?.schemaJson.toString(), connection?.credDefJson.toString(), "{}").get();


            val feature0037 = Feature0037(true);


            val baseIndyMessage = MessagePresentation(feature0037.presentation_type);
            baseIndyMessage.setId(connection?.id);
            val presentatioAttachList = ArrayList<OffersAttach>();
            val offersAttach = OffersAttach();
            //    JSONObject jsonObjectProof = new JSONObject(proof);
            //     jsonObjectProof.put("requested_proof", new JSONObject());
            //    JSONArray identifiersArray = jsonObjectProof.getJSONArray("identifiers");
            //     identifiersArray.getJSONObject(0).put("schema_id","9090");
            //     jsonObjectProof.put("identifiers",identifiersArray);
            offersAttach.setId("libindy-presentation-" + baseIndyMessage.getId());
            val base64Data = Base64Data(proof);
            offersAttach.setData(base64Data);
            presentatioAttachList.add(offersAttach);
            baseIndyMessage.setPresentationsAttach(presentatioAttachList);


            feature0037.send_message_to_agent(connection?.did1, baseIndyMessage.deSerialize(), null,
                    true, false, object : ConnectionProtocol.OnSendIndyMessage {
                override fun onSucces() {
                    //   hideProgressDialog()
                    Log.d("mylog2090","OnSuccess startTimer");
                    messageStartObservLiveData.postValue(true)
                    val timer = Timer();
                    timer.schedule(object : TimerTask() {
                        override fun run() {
                            try {
                                Log.d("mylog2090","timer ALREADY");
                                hideProgressDialog()
                                val messages1: MessagesNew? = messagesRepository.readMessageWith(connection?.id ?: "");
                                if (messages1 != null) {
                                    if (!messages1.isAccepted() && !messages1.isCanceled()) {
                                        onShowToastLiveData.postValue(resourceProvider.getString(R.string.proof_request_time_expire))
                                        onBackClickLiveData.postValue(true)
                                    }
                                }
                                Log.d("mylog2090","timer hideProgressDialog");
                              */
/*  appExecutors.mainThread().execute {
                                    Runnable() {
                                        fun run() {



                                        }
                                    }
                                }*//*


                            } catch (e: Exception) {
                                e.printStackTrace();
                                hideProgressDialog()
                                Log.d("mylog2090","timer e="+e.localizedMessage);
                            }


                        }
                    }, 60 * 1000);
                }


                override fun onFail() {
                    Log.d("mylog2090","onFail");
                    hideProgressDialog()
                    onShowToastLiveData.postValue(resourceProvider.getString(R.string.error))
                }


                override fun onProblemReport(code: String, comment: String) {
                    Log.d("mylog2090","onProblemReport");
                    */
/* val messages:MessagesNew? = DaoUtils.readMessage(connection?.messageRef?.id);
                     sendProblem(connection?.did1 ?: "", messages, code, comment);*//*

                    onShowToastLiveData.postValue(resourceProvider.getString(R.string.proof_request_error) + ": " + comment)
                    hideProgressDialog()
                }
            });

        } catch (ex: InterruptedException) {
            ex.printStackTrace();
            isSended = false;
        } catch (ex: ExecutionException) {
            ex.printStackTrace();
            isSended = false;
        } catch (ex: IndyException) {
            ex.printStackTrace();
            isSended = false;
        } catch (e: Exception) {
            e.printStackTrace();
            isSended = false;
        }
        if (!isSended) {
            hideProgressDialog()
            onShowToastLiveData.postValue(resourceProvider.getString(R.string.error))
        }
    }


    private fun sendProblem(did1: String, messages: MessagesNew?, code: String, comment: String) {
        val feature0036 = Feature0036(true);
        val feature0037 = Feature0037(true);
        var messID = "1";
        if (messages != null) {
            messID = messages.getId();
        }
        var problemMessage = ProblemMessage(feature0036.getProblemProtocol(feature0036.getProtocol()), code,
                comment, messID);
        if (connection?.type == ConnectionsWrapper.ConnectionType.proofrequest) {
            problemMessage = ProblemMessage(feature0037.getProblemProtocol(feature0037.getProtocol()), code,
                    comment, messID);
        }
        feature0036.send_message_to_agent(did1, problemMessage.deSerialize(), null, true, false, object : ConnectionProtocol.OnSendIndyMessage {

            override fun onSucces() {
                try {
                    if (messages != null) {
                        messages.setCanceled(true);
                        messages.setComment(comment);
                        messages.setCommentCode(code);
                        messagesRepository.changeCredProofStatusMessageInDB(messages)
                    }

                } catch (e: Exception) {
                    e.printStackTrace();
                }
                hideProgressDialog()
                onBackClickLiveData.postValue(true)
            }


            override fun onFail() {
                try {
                    hideProgressDialog()
                    onShowToastLiveData.postValue(resourceProvider.getString(R.string.error))
                } catch (e: Exception) {
                    e.printStackTrace();
                }

            }

            override fun onProblemReport(code: String, comment: String) {
                hideProgressDialog()
            }
        });
    }

    fun messageCompare(data: MessagesNew?): Boolean {
        if (data?.id == connection?.id) {
            Log.d("mylo890","messageCompare="+data)
            if (data?.isAccepted == true) {
                messageSuccessLiveData.postValue(true)
            } else {
                messageErrorLiveData.postValue(true)
            }
            return true
        }
        return false
    }

}*/
