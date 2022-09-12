package com.socialsirius.messenger.ui.activities.loader



import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketException
import com.neovisionaries.ws.client.WebSocketExtension
import com.neovisionaries.ws.client.WebSocketFactory
import com.socialsirius.messenger.base.providers.ResourcesProvider
import com.socialsirius.messenger.base.ui.BaseActivityModel
import com.socialsirius.messenger.repository.UserRepository
import com.socialsirius.messenger.service.SiriusWebSocketListener
import com.socialsirius.messenger.sirius_sdk_impl.SDKUseCase
import io.crossbar.autobahn.websocket.WebSocketConnection
import io.crossbar.autobahn.websocket.WebSocketConnectionHandler
import io.crossbar.autobahn.websocket.types.ConnectionResponse
import io.crossbar.autobahn.websocket.types.WebSocketOptions
import kotlinx.coroutines.GlobalScope
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocketListener
import java.io.IOException
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class LoaderActivityModel @Inject constructor(
    resourceProvider: ResourcesProvider,
    private val sdkUseCase: SDKUseCase,
    private val userRepository: UserRepository
) :
    BaseActivityModel() {
    val initStartLiveData = MutableLiveData<Boolean>()
    val initEndLiveData = MutableLiveData<Boolean>()

        fun initSdk(context: Context){
            userRepository.setupUserFromPref()
            var  login = "defaultWalletLogin" // userRepository.myUser.uid ?: ""
            var  pass = userRepository.myUser.pass ?:""
            var  label = userRepository.myUser.name ?:""
            sdkUseCase.initSdk(context,login,pass, label,object : SDKUseCase.OnInitListener{
                override fun initStart() {
                    initStartLiveData.postValue(true)
                }

                override fun initEnd() {
                    sdkUseCase.isInitiated = true
                    initEndLiveData.postValue(true)
                }

            })
        }


}