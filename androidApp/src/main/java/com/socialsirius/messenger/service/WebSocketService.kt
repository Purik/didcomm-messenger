package com.socialsirius.messenger.service

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.*
import android.util.Log
import androidx.lifecycle.LifecycleService
import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketException
import com.neovisionaries.ws.client.WebSocketExtension
import com.neovisionaries.ws.client.WebSocketFactory
import com.sirius.library.mobile.helpers.ChanelHelper
import com.socialsirius.messenger.base.App
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocketListener
import okio.ByteString
import java.io.IOException
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * TODO посмотреть что то новое в инете
 */
@Singleton
class WebSocketService : LifecycleService() {
    var networkStateReceiver: NetworkStateReceiver? = null
    private var mServiceLooper: Looper? = null
    private var mServiceHandler: ServiceHandler? = null

    private inner class ServiceHandler(looper: Looper?) : Handler(looper!!) {
        override fun handleMessage(msg: Message) {
            if (msg.obj != null) {
                onHandleIntent(msg.obj as Intent)
            }
        }
    }

    var webSockets: MutableMap<String, WebSocket> = HashMap()
    fun getWebSocket(endpoint: String): WebSocket? {
        return if (webSockets.containsKey(endpoint)) {
            webSockets[endpoint]
        } else {
            val socket = connectToWebSocket(endpoint)
            if (socket != null) {
                webSockets[endpoint] = socket
            }
            socket
        }
    }

    override fun onDestroy() {
        if (networkStateReceiver != null) {
            unregisterReceiver(networkStateReceiver)
        }
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("SOCKET", "onStartCommand=$intent")
        super.onStartCommand(intent, flags, startId)
        sendMessageToHandler(intent, flags, startId)
        return START_STICKY
    }

    private fun sendMessageToHandler(intent: Intent?, startId: Int, flags: Int) {
        Log.d("SOCKET", "sendMessageToHandler=$intent")
        val message = mServiceHandler!!.obtainMessage()
        message.arg1 = startId
        message.arg2 = flags
        message.obj = intent
        mServiceHandler!!.sendMessage(message)
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("SOCKET", "onCreate=")
        networkStateReceiver = NetworkStateReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkStateReceiver, intentFilter)

        // To avoid cpu-blocking, we create a background handler to run our service
        val thread = HandlerThread(
            "SmackService",
            Process.THREAD_PRIORITY_BACKGROUND
        )
        // start the new handler thread
        thread.start()
        mServiceLooper = thread.looper
        // start the service using the background handler
        mServiceHandler = ServiceHandler(mServiceLooper)
        /*       Intent intent = new Intent(EXTRA_INITIALIZE);
        sendMessageToHandler(intent, 0, 0);*/
    }

    protected fun onHandleIntent(intent: Intent) {
        Log.d("SOCKET", "onHandleIntent=$intent")
        if (EXTRA_INITIALIZE == intent.action) {
         //   connect()
        } else if (EXTRA_CONNECT == intent.action) {
            Log.d("mylog200", "EXTRA_CONNECT dataString=" + intent.action)
            val url = intent.getStringExtra("url")
            url?.let {  getWebSocket(url) }

        } else if (EXTRA_SEND == intent.action) {
            val data = intent.getByteArrayExtra("data")
            val url = intent.getStringExtra("url")
            url?.let {
                val ws = getWebSocket(url)
                val dataString = String(data!!)
                Log.d("mylog200", "EXTRA_SEND dataString=$dataString")
                Log.d("mylog200", "EXTRA_SEND ws=$ws")
                if (ws != null) {
                    Log.d("mylog200", "EXTRA_SEND ws.isOpen()=" + ws.isOpen)
                    if (ws.isOpen) {
                    //    ws.sendBinary(data)
                    }
                }
            }

        }
    }

    //TODO
    private fun buildUrl() {
        //   "wss://socialsirius.com/ws/notifications/?Token=bbf6cfb334c9a397d22477d0250d9329351517fb2653f31aee2f7e7f1ef75bbf5710b79a644a97b2e11e1cde928d7019da741180ce932c1b&session_id=48fa9281-d6b1-4b17-901d-7db9e64b70b1&extended=on";
        val token =
            "5f41ae8b439627ce7d809e0c593a33e271246454ddfe54a6a0551b872ecbd6d31b483bdbaa9e10b969b3c656f914e89a9c9f4ccd2d64b8ed" // AppPref.getInstance().getServerInfoSession();
        val session = "48fa9281-d6b1-4b17-901d-7db9e64b70b1"
        val url =
            ("wss://" + "socialsirius.com" + "/ws/notifications/?Token=" + token + "&session_id=" + session
                    + "&extended=off")
        val intent = Intent(EXTRA_CONNECT)
        intent.putExtra("url", url)
        sendMessageToHandler(intent, 0, 0)
    }

      private fun connectToWebSocket(url: String?): WebSocket? {
          Log.d("mylog500", "socket url=$url")
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
          var sslContext: SSLContext? = null
          try {
              sslContext = SSLContext.getInstance("SSL")
              sslContext.init(null, trustAllCerts, SecureRandom())
              // Create an ssl socket factory with our all-trusting manager
              val sslSocketFactory = sslContext.socketFactory
              /*okHttpClient.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            okHttpClient.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });*/
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }
        try {
            return WebSocketFactory()
                .setVerifyHostname(false)
                .setSSLContext(sslContext)
                .setConnectionTimeout(15000)
                .createSocket(url)
                .addListener(SiriusWebSocketListener())
                .addExtension(WebSocketExtension.PERMESSAGE_DEFLATE)
                .setPingInterval((60 * 3 * 1000).toLong())
                .connectAsynchronously()
        } catch (e: WebSocketException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

/*    var mWebSocket: okhttp3.WebSocket? = null
    fun connectToWebSocket(endpoint: String): okhttp3.WebSocket {
        val httpClient = OkHttpClient.Builder()
            .pingInterval(40, TimeUnit.SECONDS) // Устанавливаем интервал отправки PING кадра
            .build()
        val webSocketUrl = endpoint
        val request = Request.Builder()
            .url(webSocketUrl)
            .build()
        return httpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: okhttp3.WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.d(
                    TAG,
                    "onOpen websocket=$webSocket frame=$response"
                )
                // Соединение с WebSocket установлено
                mWebSocket = webSocket
            }

            override fun onMessage(webSocket: okhttp3.WebSocket, text: String) {
                super.onMessage(webSocket, text)
               // binary?.let {
                    val payloadText =text // String(binary)
                    Log.d(
                        TAG,
                        "onBinaryMessage websocket=$webSocket frame=$payloadText"
                    )
                    val messageWrapper = SiriusWebSocketListener.parseSocketMessage(payloadText)
                    Log.d("mylog2090", "messageWrapper?.contentType=" + messageWrapper?.contentType);
                    if (messageWrapper?.topic == "indy.transport") {
                        // WebSocketService.agent.receiveMsg(binary)
                        Log.d(
                            "mylog2090",
                            "messageWrapper?.messageString=" + messageWrapper?.messageString
                        );
                        ChanelHelper.getInstance()
                            .parseMessage(messageWrapper?.messageFromMessageString ?: "")
                    }
             //   }
                // Получение сообщения типа String от сервера
            }

            override fun onClosing(webSocket: okhttp3.WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                Log.d(
                    TAG,
                    "onClosing websocket=$webSocket code=$code  reason=$reason"
                )
                // Получено сообщение CLOSE frame от сервера, готовность закрыть соединение
            }

            override fun onClosed(webSocket: okhttp3.WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                Log.d(
                    TAG,
                    "onClosed websocket=$webSocket code=$code  reason=$reason"
                )
                // Соединение с WebSocket закрыто
            }

            override fun onFailure(
                webSocket: okhttp3.WebSocket,
                t: Throwable,
                response: Response?
            ) {

                super.onFailure(webSocket, t, response)
                Log.d(
                    TAG,
                    "onClosed websocket=$webSocket Throwable=${t.message}  response=$response"
                )
                // ошибка
            }
        })
    }*/

    private fun connect() {
        buildUrl()
    }

    companion object {
        const val EXTRA_RECONNECT = "reconnect"
        const val EXTRA_PERFORM_CONNECTION = "perform_connection"
        const val EXTRA_DISCONNECT = "disconnect"
        const val EXTRA_INITIALIZE = "initialize"
        const val EXTRA_CONNECT = "connect"
        const val EXTRA_CLOSE = "close"
        const val EXTRA_SEND = "send"
        private val TAG = WebSocketService::class.java.name
        private const val TIMEOUT = 30 * 1000
    }

    init {
        if (App.getInstance() == null) {
            App.initialize()
        }
    }
}