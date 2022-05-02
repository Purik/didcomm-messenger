package com.socialsirius.messenger.sirius_sdk_impl

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.socialsirius.messenger.service.WebSocketService
import com.socialsirius.messenger.utils.DateUtils.PATTERN_ROSTER_STATUS_RESPONSE2
import com.sirius.library.agent.BaseSender
import com.sirius.library.agent.aries_rfc.concept_0017_attachments.Attach
import com.sirius.library.agent.aries_rfc.feature_0095_basic_message.Message

import com.sirius.library.agent.aries_rfc.feature_0113_question_answer.messages.QuestionMessage
import com.sirius.library.agent.aries_rfc.feature_0113_question_answer.messages.Recipes

import com.sirius.library.mobile.SiriusSDK
import com.sirius.library.mobile.helpers.ChanelHelper
import com.sirius.library.mobile.helpers.PairwiseHelper
import com.sirius.library.mobile.helpers.ScenarioHelper
import com.sirius.library.mobile.scenario.impl.InviterScenario
import com.sirius.library.mobile.utils.HashUtils


import com.socialsirius.messenger.repository.EventRepository
import com.socialsirius.messenger.repository.MessageRepository
import com.socialsirius.messenger.repository.UserRepository
import com.socialsirius.messenger.repository.models.LocalMessage
import com.socialsirius.messenger.sirius_sdk_impl.scenario.*
import com.socialsirius.messenger.utils.FileUtils
import com.sirius.library.agent.aries_rfc.feature_0036_issue_credential.messages.OfferCredentialMessage
import com.sirius.library.agent.aries_rfc.feature_0036_issue_credential.messages.ProposeCredentialMessage
import com.sirius.library.mobile.models.CredentialsRecord
import com.socialsirius.messenger.models.FileAttach
import com.sodium.LibSodium
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject

import java.io.File
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SDKUseCase @Inject constructor(
    private val eventRepository: EventRepository,
    private val messageRepository: MessageRepository, val userRepository: UserRepository
) {


    public fun startSocketService(context: Context) {
        val intent = Intent(context, WebSocketService::class.java)
        context.startService(intent)
    }

    private fun connectToSocket(context: Context, url: String) {
        val intent = Intent(context, WebSocketService::class.java)
        intent.setAction(WebSocketService.EXTRA_CONNECT)
        intent.putExtra("url", url)
        context.startService(intent)

    }


    private fun closeSocket(context: Context) {
        val intent = Intent(context, WebSocketService::class.java)
        intent.setAction(WebSocketService.EXTRA_CLOSE)
        context.startService(intent)
    }


    private fun sendMessToSocket(context: Context, endpoint: String, data: ByteArray) {
        val intent = Intent(context, WebSocketService::class.java)
        intent.setAction(WebSocketService.EXTRA_SEND)
        intent.putExtra("data", data)
        intent.putExtra("url", endpoint)
        context.startService(intent)
    }


    interface OnInitListener {
        fun initStart()
        fun initEnd()
    }

    fun initSdk(
        context: Context,
        userJid: String,
        pass: String,
        label: String,
        onInitListener: OnInitListener?
    ) {
        onInitListener?.initStart()
        val mainDirPath = context.filesDir.absolutePath
        val walletDirPath = mainDirPath + File.separator + "wallet"
        val alias = userJid
        val passForWallet = pass
        val projDir = File(walletDirPath)
        if (!projDir.exists()) {
            projDir.mkdirs()
        }
        val walletId = alias

        val sender = object : BaseSender() {
            override fun sendTo(endpoint: String?, data: ByteArray?): Boolean {
                if (endpoint?.startsWith("http") == true) {
                    Thread(Runnable {
                        //content-type
                        val ssiAgentWire: MediaType = "application/ssi-agent-wire".toMediaType()
                        var client: OkHttpClient = OkHttpClient()
                        Log.d("mylog200", "requset=" + String(data ?: ByteArray(0)))
                        val body: RequestBody =
                            RequestBody.create(ssiAgentWire, data ?: ByteArray(0))
                        val request: Request = Request.Builder()
                            .url(endpoint ?: "")
                            .post(body)
                            .build()
                        client.newCall(request).execute().use { response ->
                            Log.d("mylog200", "response=" + response.body?.string())
                            response.isSuccessful
                        }
                    }).start()
                } else if (endpoint?.startsWith("ws") == true) {
                    sendMessToSocket(context, endpoint, data ?: ByteArray(0))
                }

                return false
            }

            override fun open(endpoint: String?) {
                println("SOCKET open endpoint$endpoint")
                connectToSocket(context, endpoint ?: "")
            }


            override fun close() {
                println("SOCKET closeSocket ")
                closeSocket(context)
            }


        }
        val mediatorAddress = "wss://mediator.socialsirius.com/ws"
        val recipientKeys = "DjgWN49cXQ6M6JayBkRCwFsywNhomn8gdAXHJ4bb98im"



        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                GlobalScope.launch(Dispatchers.Default) {

                    SiriusSDK.getInstance().initializeCorouitine(
                        alias = walletId, pass = passForWallet,
                        mainDirPath = mainDirPath,
                        mediatorAddress = mediatorAddress, recipientKeys = listOf(recipientKeys),
                        label = label, "default_mobile", baseSender = sender
                    )
                    ChanelHelper.getInstance().initListener()
                    SiriusSDK.getInstance().connectToMediator()
                    initScenario()
                    onInitListener?.initEnd()
                }
                return@OnCompleteListener
            }
            val token = task.result
            GlobalScope.launch(Dispatchers.Default) {

                SiriusSDK.getInstance().initializeCorouitine(
                    alias = walletId, pass = passForWallet,
                    mainDirPath = mainDirPath,
                    mediatorAddress = mediatorAddress, recipientKeys = listOf(recipientKeys),
                    label = label, "default_mobile", baseSender = sender
                )
                ChanelHelper.getInstance().initListener()
                SiriusSDK.getInstance().connectToMediator(token)
                initScenario()
                onInitListener?.initEnd()
            }
        })


    }

    fun deleteWallet(context: Context) {
        userRepository.logout()
        val mainDirPath = context.filesDir.absolutePath
        val walletDirPath = mainDirPath + File.separator + "wallet"
        FileUtils.cleanDirectory(File(walletDirPath))
        FileUtils.deleteDirectory(File(walletDirPath))
    }

    private fun initScenario() {
        ScenarioHelper.getInstance().addScenario("Inviter", InviterScenarioImpl(messageRepository))
        ScenarioHelper.getInstance()
            .addScenario("Invitee", InviteeScenarioImp(messageRepository, eventRepository))
        ScenarioHelper.getInstance()
            .addScenario("Holder", HolderScenarioImp(messageRepository, eventRepository))
        ScenarioHelper.getInstance()
            .addScenario("Text", TextScenarioImpl(messageRepository, eventRepository))
        ScenarioHelper.getInstance()
            .addScenario("Prover", ProverScenarioImpl(messageRepository, eventRepository))
        ScenarioHelper.getInstance()
            .addScenario("Question", QuestionAnswerScenarioImp(messageRepository, eventRepository))
        ScenarioHelper.getInstance()
            .addScenario("Notification", NotificationScenarioImpl(messageRepository))

    }


    fun sendMessageWithAttachForPairwise(pairwiseDid: String, attach : FileAttach): LocalMessage {
        val pairwise = PairwiseHelper.getInstance().getPairwise(theirDid = pairwiseDid)
        val message = Message.builder().setContent(attach.messageText).build()
        val att: Attach = Attach().setId(attach.id).setMimeType("image/png").setFileName(attach.fileName)
            .setData(attach.fileBase64Bytes ?: ByteArray(0))
        message.addAttach(att)
        val localMessage = LocalMessage(pairwiseDid = pairwiseDid)

        localMessage.isMine = true
        localMessage.type = "doc"

        localMessage.message = message.serialize()
        localMessage.sentTime = Date()
        pairwise?.let {
            SiriusSDK.getInstance().context.sendTo(message, pairwise)
        }
        return localMessage
    }

    fun sendTextMessageForPairwise(pairwiseDid: String, messageText: String?): LocalMessage {
        val pairwise = PairwiseHelper.getInstance().getPairwise(theirDid = pairwiseDid)
        val message = Message.builder().setContent(messageText).build()
        val localMessage = LocalMessage(pairwiseDid = pairwiseDid)
        localMessage.isMine = true
        localMessage.type = "text"
        localMessage.message = message.serialize()
        localMessage.sentTime = Date()
        pairwise?.let {
            SiriusSDK.getInstance().context.sendTo(message, pairwise)
        }
        return localMessage
    }

    fun sendRequestToPairwise(pairwiseDid: String): LocalMessage {
        val pairwise = PairwiseHelper.getInstance().getPairwise(theirDid = pairwiseDid)
        val proposMessage =
            ProposeCredentialMessage.builder().setCredDefId("4565").setSchemaId("465").build()
        //  val message = Message.builder().setContent(messageText).build()
        val localMessage = LocalMessage(pairwiseDid = pairwiseDid)
        localMessage.isMine = true
        localMessage.type = "propose"
        localMessage.message = proposMessage.serialize()
        localMessage.sentTime = Date()
        pairwise?.let {
            SiriusSDK.getInstance().context.sendTo(proposMessage, pairwise)
        }
        return localMessage
    }

    fun sendRequestToPairwise(
        pairwiseDid: String,
        credentialsRecord: CredentialsRecord
    ): LocalMessage {
        val pairwise = PairwiseHelper.getInstance().getPairwise(theirDid = pairwiseDid)
        val proposMessage =
            ProposeCredentialMessage.builder().setCredDefId(credentialsRecord.cred_def_id)
                .setCredentialProposal(credentialsRecord.getAttributes())
                .setSchemaId(credentialsRecord.schema_id).build()
        //  val message = Message.builder().setContent(messageText).build()
        val localMessage = LocalMessage(pairwiseDid = pairwiseDid)
        localMessage.isMine = true
        localMessage.type = "text"
        localMessage.message = proposMessage.serialize()
        localMessage.sentTime = Date()
        pairwise?.let {
            SiriusSDK.getInstance().context.sendTo(proposMessage, pairwise)
        }
        return localMessage
    }

    fun generateInvitation(): String? {
        val inviter = ScenarioHelper.getInstance().getScenarioBy("Inviter") as? InviterScenario
        return inviter?.generateInvitation()
    }

    fun sendTestQuestion(pairwiseDid: String): LocalMessage {
        val pairwise = PairwiseHelper.getInstance().getPairwise(theirDid = pairwiseDid)
        val message = QuestionMessage.builder()
            .setQuestionText("Alice, are you on the phone with Bob from Faber Bank right now?")
            .setValidResponses(listOf("Yes, it's me", "No, that's not me!"))
            .setQuestionDetail("This is optional fine-print giving context to the question and its various answers.")
            .build()
        val localMessage = LocalMessage(pairwiseDid = pairwiseDid)
        localMessage.isMine = true
        localMessage.sentTime = Date()
        localMessage.type = "question"
        localMessage.message = message.serialize()
        Thread(Runnable {
            pairwise?.let {
                Recipes.askAndWaitAnswer(SiriusSDK.getInstance().context, message, pairwise)
            }
        }).start()
        return localMessage
    }
}