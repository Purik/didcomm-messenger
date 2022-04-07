package com.socialsirius.messenger.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketExtension;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.socialsirius.messenger.base.App;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * TODO посмотреть что то новое в инете
 */

public class WebSocketService extends Service {

    public static final String EXTRA_RECONNECT = "reconnect";
    public static final String EXTRA_PERFORM_CONNECTION = "perform_connection";
    public static final String EXTRA_DISCONNECT = "disconnect";
    public static final String EXTRA_INITIALIZE = "initialize";
    public static final String EXTRA_CONNECT = "connect";
    public static final String EXTRA_CLOSE = "close";
    public static final String EXTRA_SEND = "send";
    private final static String TAG = WebSocketService.class.getName();
    NetworkStateReceiver networkStateReceiver;

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    private final class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.obj != null) {
                onHandleIntent((Intent) msg.obj);
            }
        }
    }

    Map<String, WebSocket> webSockets = new HashMap<>();

    WebSocket getWebSocket(String endpoint) {
        if (webSockets.containsKey(endpoint)) {
            return webSockets.get(endpoint);
        } else {
            WebSocket socket = connectToWebSocket(endpoint);
            if(socket!=null){
                webSockets.put(endpoint, socket);
            }
            return socket;
        }
    }


    public WebSocketService() {
        super();
        if (App.getInstance() == null) {
            App.initialize();
        }

    }


    @Override
    public void onDestroy() {
        if (networkStateReceiver != null) {
            unregisterReceiver(networkStateReceiver);
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        sendMessageToHandler(intent, flags, startId);
        return START_STICKY;
    }

    private void sendMessageToHandler(Intent intent, int startId, int flags) {
        android.os.Message message = mServiceHandler.obtainMessage();
        message.arg1 = startId;
        message.arg2 = flags;
        message.obj = intent;
        mServiceHandler.sendMessage(message);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("mylog200","WEBSOCKET CREATE");
        networkStateReceiver = new NetworkStateReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkStateReceiver, intentFilter);

        // To avoid cpu-blocking, we create a background handler to run our service
        HandlerThread thread = new HandlerThread("SmackService",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        // start the new handler thread
        thread.start();

        mServiceLooper = thread.getLooper();
        // start the service using the background handler
        mServiceHandler = new ServiceHandler(mServiceLooper);
 /*       Intent intent = new Intent(EXTRA_INITIALIZE);
        sendMessageToHandler(intent, 0, 0);*/

    }

    protected void onHandleIntent(Intent intent) {
        if (EXTRA_INITIALIZE.equals(intent.getAction())) {
            connect();
        } else if (EXTRA_CONNECT.equals(intent.getAction())) {
            Log.d("mylog200","EXTRA_CONNECT dataString="+intent.getAction());
            getWebSocket(intent.getStringExtra("url"));
        } else if (EXTRA_SEND.equals(intent.getAction())) {
            byte[] data = intent.getByteArrayExtra("data");
           WebSocket ws = getWebSocket(intent.getStringExtra("url"));
          String dataString =   new String(data);
          Log.d("mylog200","EXTRA_SEND dataString="+dataString);
            Log.d("mylog200","EXTRA_SEND ws="+ws);
            if(ws!=null){
                Log.d("mylog200","EXTRA_SEND ws.isOpen()="+ws.isOpen());
                if(ws.isOpen()){
                    ws.sendBinary(data);
                }
            }
        }
    }


    //TODO

    private void buildUrl() {
        //   "wss://socialsirius.com/ws/notifications/?Token=bbf6cfb334c9a397d22477d0250d9329351517fb2653f31aee2f7e7f1ef75bbf5710b79a644a97b2e11e1cde928d7019da741180ce932c1b&session_id=48fa9281-d6b1-4b17-901d-7db9e64b70b1&extended=on";
        String token = "5f41ae8b439627ce7d809e0c593a33e271246454ddfe54a6a0551b872ecbd6d31b483bdbaa9e10b969b3c656f914e89a9c9f4ccd2d64b8ed";// AppPref.getInstance().getServerInfoSession();
        String session = "48fa9281-d6b1-4b17-901d-7db9e64b70b1";
        String url = "wss://" + "socialsirius.com" + "/ws/notifications/?Token=" + token + "&session_id=" + session
                + "&extended=off";
        Intent intent = new Intent(EXTRA_CONNECT);
        intent.putExtra("url", url);
        sendMessageToHandler(intent, 0, 0);
    }

    private static final int TIMEOUT = 30*1000;

    private WebSocket  connectToWebSocket(String url) {
        Log.d("mylog500", "socket url=" + url);
        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };
        // Install the all-trusting trust manager
         SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            /*okHttpClient.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            okHttpClient.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });*/

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        try {
            WebSocket ws = new WebSocketFactory()
                    .setVerifyHostname(false)
                    .setSSLContext(sslContext)
                    .setConnectionTimeout(TIMEOUT)
                    .createSocket(url, TIMEOUT)
                    .addListener(new SiriusWebSocketListener())
                    .addExtension(WebSocketExtension.PERMESSAGE_DEFLATE)
                    .setPingInterval(60 * 3 * 1000)
                    .connect();
            return ws;
        } catch (WebSocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void connect() {
        buildUrl();
    }


}
