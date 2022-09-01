package com.socialsirius.messenger.sirius_sdk_impl

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.socialsirius.messenger.service.WebSocketService
import com.socialsirius.messenger.utils.DateUtils.PATTERN_ROSTER_STATUS_RESPONSE2
import com.sirius.library.agent.BaseSender
import com.sirius.library.agent.aries_rfc.AriesProtocolMessage
import com.sirius.library.agent.aries_rfc.concept_0017_attachments.Attach
import com.sirius.library.agent.aries_rfc.feature_0015_ack.Ack
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
import com.sirius.library.agent.aries_rfc.feature_0048_trust_ping.Ping
import com.sirius.library.agent.aries_rfc.feature_0048_trust_ping.Pong
import com.sirius.library.mobile.models.CredentialsRecord
import com.socialsirius.messenger.base.data.api.HttpLoggingInterceptorMy
import com.socialsirius.messenger.models.ChatMessageStatus
import com.socialsirius.messenger.models.FileAttach
import com.socialsirius.messenger.use_cases.EventUseCase
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
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Singleton
class SDKUseCase @Inject constructor(
    private val eventRepository: EventRepository,
    private val messageRepository: MessageRepository,
    val userRepository: UserRepository
) {


    var isInitiated: Boolean = false

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

    //  private static OkHttpClient ApiOkHttpClient;
    private fun provideOkHttpClient(
        timeOut: Int = 30,
    ): OkHttpClient {
        /*if (ApiOkHttpClient != null) {
            return ApiOkHttpClient;
        }*/
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.connectTimeout(timeOut.toLong(), TimeUnit.SECONDS)
        okHttpClient.readTimeout(timeOut.toLong(), TimeUnit.SECONDS)
        //okHttpClient.addInterceptor(RequestInterceptor(useAuthorize, addHostHeader))
        //  okHttpClient.addInterceptor(new ResponseInterceptor());
        val logInterceptor = HttpLoggingInterceptorMy()
        logInterceptor.setLevel(HttpLoggingInterceptorMy.Level.BODY)
      //  okHttpClient.addInterceptor(LogoutInterceptor(useAuthorize))
        okHttpClient.addInterceptor(logInterceptor)
        okHttpClient.followRedirects(true)
        okHttpClient.followSslRedirects(true)

        // Create a trust manager that does not validate certificate chains
        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
        )

        // Install the all-trusting trust manager
        val sslContext: SSLContext
        try {
            sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            okHttpClient.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            okHttpClient.hostnameVerifier(HostnameVerifier { hostname, session -> true })
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }
        //ApiOkHttpClient = okHttpClient.build();
        return okHttpClient.build()
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
        val passForWallet = pass
        val projDir = File(walletDirPath)
        if (!projDir.exists()) {
            projDir.mkdirs()
        }
        val walletId = userJid

        val sender = object : BaseSender() {
            override fun sendTo(endpoint: String?, data: ByteArray?): Boolean {
                if (endpoint?.startsWith("http") == true) {
                    Thread(Runnable {
                        //content-type
                        val ssiAgentWire: MediaType = "application/ssi-agent-wire".toMediaType()
                        var client: OkHttpClient = provideOkHttpClient()
                        Log.d("mylog200", "requset=" + String(data ?: ByteArray(0)))
                        val body: RequestBody =
                            RequestBody.create(ssiAgentWire, data ?: ByteArray(0))
                        val request: Request = Request.Builder()
                            .url(endpoint ?: "")
                            .post(body)
                            .build()
                        try {
                            client.newCall(request).execute().use { response ->
                                Log.d("mylog200", "response=" + response.body?.string())
                                response.isSuccessful
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
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
                startSdk(walletId,passForWallet,mainDirPath,mediatorAddress,recipientKeys,label,sender,null,onInitListener )
                return@OnCompleteListener
            }
            val token = task.result
            startSdk(walletId,passForWallet,mainDirPath,mediatorAddress,recipientKeys,label,sender,token,onInitListener )
        })


    }

    fun startSdk(walletId : String,passForWallet : String,mainDirPath:String,mediatorAddress :  String,
                 recipientKeys : String,label : String,sender : BaseSender,token : String?,  onInitListener: OnInitListener?){
        GlobalScope.launch(Dispatchers.Default) {

            SiriusSDK.getInstance().initializeCorouitine(
                alias = walletId,
                pass = passForWallet,
                mainDirPath = mainDirPath,
                mediatorAddress = mediatorAddress,
                recipientKeys = listOf(recipientKeys),
                label = label,
                "default_mobile",
                baseSender = sender
            )
            ChanelHelper.getInstance().initListener()
            SiriusSDK.getInstance().connectToMediator(token)
            initScenario()
            isInitiated = true
            onInitListener?.initEnd()
        }
    }

    fun deleteWallet(context: Context) {
        userRepository.logout()
        logoutFromSDK()
        val mainDirPath = context.filesDir.absolutePath
        val walletDirPath = mainDirPath + File.separator + "wallet"
        FileUtils.cleanDirectory(File(walletDirPath))
        FileUtils.deleteDirectory(File(walletDirPath))
    }

    private fun initScenario() {
        ScenarioHelper.getInstance().addScenario("Inviter", InviterScenarioImpl(messageRepository, eventRepository))
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
        ScenarioHelper.getInstance()
            .addScenario("Ping", PingScenarioImpl(this))
        ScenarioHelper.getInstance()
            .addScenario("Pong", PongScenarioImpl(eventRepository, messageRepository ))
        ScenarioHelper.getInstance()
            .addScenario("Ack", AckScenarioImpl(eventRepository, messageRepository ))
        ScenarioHelper.getInstance()
            .addScenario("Status", MessageStatusScenarioImpl(this))

    }


    fun sendMessageWithAttachForPairwise(pairwiseDid: String, attach: FileAttach): LocalMessage {
        val pairwise = PairwiseHelper.getInstance().getPairwise(theirDid = pairwiseDid)
        val message = Message.builder().setContent(attach.messageText).setOutTime(com.sirius.library.utils.Date()).build()
        val att: Attach =
            Attach().setId(attach.id).setMimeType("image/png").setFileName(attach.fileName)
                .setData(attach.fileBase64Bytes ?: ByteArray(0))
        message.addAttach(att)
        val localMessage = LocalMessage(pairwiseDid = pairwiseDid)

        localMessage.isMine = true
        localMessage.type = "doc"
        localMessage.status = ChatMessageStatus.sent
        localMessage.message = message.serialize()
        localMessage.sentTime = Date()
        pairwise?.let {
            SiriusSDK.getInstance().context.sendTo(message, pairwise)
        }
        return localMessage
    }

    fun sendTextMessageForPairwise(pairwiseDid: String, messageText: String?): LocalMessage {
        val pairwise = PairwiseHelper.getInstance().getPairwise(theirDid = pairwiseDid)
        val message = Message.builder().setContent(messageText).setOutTime(com.sirius.library.utils.Date()).build()
        val localMessage = LocalMessage(id = message.getId(), pairwiseDid = pairwiseDid)
        localMessage.isMine = true
        localMessage.type = "text"
        localMessage.status = ChatMessageStatus.sent
        localMessage.message = message.serialize()
        localMessage.sentTime = Date()
        pairwise?.let {
            SiriusSDK.getInstance().context.sendTo(message, pairwise)
        }
        return localMessage
    }


    fun sendTrustPingMessageForPairwise(pairwiseDid: String, pingId : String?=null){
        val pairwise = PairwiseHelper.getInstance().getPairwise(theirDid = pairwiseDid)
        var message : AriesProtocolMessage = Ping.builder().setResponseRequested(true).build()
        if(pingId!=null){
             message = Pong.builder().setPingId(pingId).build()
        }
        pairwise?.let {
            SiriusSDK.getInstance().context.sendTo(message, pairwise)
        }
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
        val localLang = "en"
        val serverUri = "https://messenger.socialsirius.com/$localLang/invitation"
        return inviter?.generateInvitation(serverUri)
    }


    fun sendStatusFoMessage(id : String, pairwiseDid: String, status : Ack.Status) {
        val pairwise = PairwiseHelper.getInstance().getPairwise(theirDid = pairwiseDid)
        val ack: Ack = Ack.builder().setStatus(status).build()
        ack.setThreadId(id)
        SiriusSDK.getInstance().context.currentHub.getAgenti()?.sendMessage(ack,pairwise?.their?.endpointAddress)
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
        localMessage.status = ChatMessageStatus.sent
        localMessage.message = message.serialize()
        Thread(Runnable {
            pairwise?.let {
                Recipes.askAndWaitAnswer(SiriusSDK.getInstance().context, message, pairwise)
            }
        }).start()
        return localMessage
    }

    fun logoutFromSDK() {
        SiriusSDK.cleanInstance()
        //TODO close websoket, delete firebase Token and some other
      //  WebSocketService.
    }

    fun changeLabel(){
        SiriusSDK.getInstance().label = userRepository.myUser.name
    }
}