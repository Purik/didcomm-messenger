package com.socialsirius.messenger.service

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.neovisionaries.ws.client.*
import com.sirius.library.mobile.helpers.ChanelHelper
import com.sirius.library.utils.Logger
import com.socialsirius.messenger.service.ChannelMessageWrapper.WIRED_CONTENT_TYPE


class SiriusWebSocketListener() : WebSocketListener {
    val TAG = "CHANNEL_SOCKET"
/*
    @Throws(Exception::class)
    override fun onTextMessage(websocket: WebSocket?, data: ByteArray?) {
        Log.d(TAG, "onTextMessage websocket=$websocket data=$data")
    }

    @Throws(Exception::class)
    override fun onStateChanged(websocket: WebSocket?, newState: WebSocketState?) {
        Log.d(TAG, "onStateChanged websocket=$websocket newState=$newState")
    }

    @Throws(Exception::class)
    override fun onConnected(websocket: WebSocket?, headers: Map<String, List<String>>?) {
        Log.d(TAG, "onConnected websocket=$websocket headers=$headers")
    }

    @Throws(Exception::class)
    override fun onConnectError(websocket: WebSocket?, exception: WebSocketException?) {
        Log.d(TAG, "onConnectError websocket=$websocket exception=${exception?.localizedMessage}")
       // exception.printStackTrace()
    }

    @Throws(Exception::class)
    override fun onDisconnected(
        websocket: WebSocket?,
        serverCloseFrame: WebSocketFrame?,
        clientCloseFrame: WebSocketFrame?,
        closedByServer: Boolean
    ) {
        Log.d(TAG, "onDisconnected websocket=$websocket closedByServer=$closedByServer serverCloseFrame$serverCloseFrame clientCloseFrame=$clientCloseFrame")
    }

    @Throws(Exception::class)
    override fun onFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
        Log.d(TAG, "onFrame websocket=$websocket frame=$frame")
    }

    @Throws(Exception::class)
    override fun onContinuationFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
        Log.d(TAG, "onContinuationFrame websocket=$websocket frame=$frame")
    }

    @Throws(Exception::class)
    override fun onError(websocket: WebSocket?, cause: WebSocketException?) {
        Log.d("mylog500", "onError websocket=$websocket cause=$cause")
      //  cause.printStackTrace()
    }

    @Throws(Exception::class)
    override fun onFrameError(
        websocket: WebSocket?,
        cause: WebSocketException?,
        frame: WebSocketFrame?
    ) {
        Log.d(TAG, "onFrameError websocket=$websocket frame=$frame data=${cause?.localizedMessage}")
      //  cause.printStackTrace()
    }

    @Throws(Exception::class)
    override fun onMessageError(
        websocket: WebSocket?,
        cause: WebSocketException?,
        frames: List<WebSocketFrame>?
    ) {
        Log.d(TAG, "onMessageError websocket=$websocket data=${cause?.localizedMessage}")
        //cause.printStackTrace()
    }

    @Throws(Exception::class)
    override fun onMessageDecompressionError(
        websocket: WebSocket?,
        cause: WebSocketException?,
        compressed: ByteArray?
    ) {
        Log.d(TAG, "onMessageDecompressionError websocket=$websocket data=${cause?.localizedMessage}")
    //    cause.printStackTrace()
    }*/

    @Throws(Exception::class)
    override fun onBinaryMessage(websocket: WebSocket?, binary: ByteArray?) {
        binary?.let {
            val payloadText = String(binary)
            Log.d(
                TAG,
                "onBinaryMessage websocket=$websocket frame=$payloadText"
            )
            val messageWrapper = parseSocketMessage(payloadText)
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
        }

    }
/*
    @Throws(Exception::class)
    override fun onSendingFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
        Log.d(TAG, "onSendingFrame websocket=$websocket data=$frame")
    }

    @Throws(Exception::class)
    override fun onFrameSent(websocket: WebSocket?, frame: WebSocketFrame?) {
        Log.d(TAG, "onFrameSent websocket=$websocket data=$frame")
    }

    @Throws(Exception::class)
    override fun onFrameUnsent(websocket: WebSocket?, frame: WebSocketFrame?) {
        Log.d(TAG, "onFrameUnsent websocket=$websocket data=$frame")
    }

    @Throws(Exception::class)
    override fun onThreadCreated(websocket: WebSocket?, threadType: ThreadType?, thread: Thread?) {
        Log.d(TAG, "onThreadCreated websocket=$websocket data=")
    }

    @Throws(Exception::class)
    override fun onThreadStarted(websocket: WebSocket?, threadType: ThreadType?, thread: Thread?) {
        Log.d(TAG, "onThreadStarted websocket=$websocket data=")
    }

    @Throws(Exception::class)
    override fun onThreadStopping(websocket: WebSocket?, threadType: ThreadType?, thread: Thread?) {
        Log.d(TAG, "onThreadStopping websocket=$websocket data=")
    }

    @Throws(Exception::class)
    override fun onTextMessageError(
        websocket: WebSocket?,
        cause: WebSocketException?,
        data: ByteArray?
    ) {
        Log.d(TAG, "onTextMessageError websocket=$cause cause=$data datacause$cause")
       // cause.printStackTrace()
    }

    @Throws(Exception::class)
    override fun onSendError(
        websocket: WebSocket?,
        cause: WebSocketException?,
        frame: WebSocketFrame?
    ) {
        Log.d(TAG, "onSendError websocket=$websocket data=$cause")
      //  cause.printStackTrace()
    }

    @Throws(Exception::class)
    override fun onUnexpectedError(websocket: WebSocket?, cause: WebSocketException?) {
        Log.d(TAG, "onUnexpectedError websocket=$websocket cause=${cause?.localizedMessage}")
       // cause.printStackTrace()
    }

    @Throws(Exception::class)
    override fun handleCallbackError(websocket: WebSocket?, cause: Throwable?) {
        Log.d(TAG, "handleCallbackError websocket=$websocket data=${cause?.localizedMessage}")
    //    cause.printStackTrace()
    }

    @Throws(Exception::class)
    override fun onSendingHandshake(
        websocket: WebSocket?,
        requestLine: String?,
        headers: List<Array<String>>?
    ) {
        Log.d(TAG, "onSendingHandshake websocket=$websocket data=$requestLine")
    }*/

    @Throws(Exception::class)
    override fun onTextFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
        Log.d(
            TAG,
            "onTextFrame websocket=$websocket frame=$frame frame.payloadText=${frame?.payloadText}"
        )
        val payloadText = frame?.payloadText
        payloadText?.let {
            val messageWrapper = parseSocketMessage(payloadText)
            Log.d("mylog2090", "messageWrapper?.contentType=" + messageWrapper?.contentType);
            if (messageWrapper?.topic == "indy.transport") {

                Log.d(
                    "mylog2090",
                    "messageWrapper?.messageString=" + messageWrapper?.messageString
                );
                ChanelHelper.getInstance()
                    .parseMessage(messageWrapper?.messageFromMessageString ?: "")
            }
        }

    }

    override fun onBinaryFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
        Log.d(TAG, "onBinaryFrame ");
    }

    companion object {
        fun parseSocketMessage(messagePayload: String?): ChannelMessageWrapper? {
            try {
                val gson = GsonBuilder().create()
                val jelem: JsonElement = gson.fromJson(messagePayload, JsonElement::class.java)
                val jobj = jelem.asJsonObject
                if (jobj.has("protected")) {
                    val meta = ChannelMessageWrapper.MessageWrapperMeta()
                    return ChannelMessageWrapper(
                        "indy.transport",
                        messagePayload,
                        WIRED_CONTENT_TYPE,
                        null,
                        meta
                    )
                } else {
                    val topic = jobj["topic"].asString ?: ""
                    val messageStrig = jobj["event"].toString()
                    val did: String? = if (jobj.has("did")) jobj["did"].asString else null
                    val contentType: String =
                        if (jobj.has("content_type")) jobj["content_type"].asString else ""
                    val meta = ChannelMessageWrapper.MessageWrapperMeta()
                    try {
                        val metadata = if (jobj.has("meta")) {
                            if (!jobj["meta"].isJsonNull) {
                                jobj["meta"].asJsonObject
                            } else {
                                JsonObject()
                            }
                        } else JsonObject()
                        meta.uid = if (metadata.has("uid")) metadata["uid"].asString else null
                        meta.utc = if (metadata.has("utc")) metadata["utc"].asDouble else null
                        meta.content_type =
                            if (metadata.has("content_type")) metadata["content_type"].asString else null
                        meta.session_id =
                            if (metadata.has("session_id")) metadata["session_id"].asString else null
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    return ChannelMessageWrapper(topic, messageStrig, contentType, did, meta)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }

/*

    @Throws(Exception::class)
    override fun onBinaryFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
        Log.d(TAG, "onBinaryFrame websocket=$websocket data=$frame")
    }

    @Throws(Exception::class)
    override fun onCloseFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
        Log.d(TAG, "onCloseFrame websocket=$websocket data=$frame")
    }

    @Throws(Exception::class)
    override fun onPingFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
        Log.d(TAG, "onPingFrame websocket=$websocket data=$frame")
    }

    @Throws(Exception::class)
    override fun onPongFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
        Log.d(TAG, "onPongFrame websocket=$websocket data=$frame")
    }

    @Throws(Exception::class)
    override fun onTextMessage(websocket: WebSocket?, text: String?) {
        Log.d(TAG, "onTextMessage websocket=$websocket text=$text")
    }
*/


    @Throws(Exception::class)
    override fun onStateChanged(webSocket: WebSocket?, webSocketState: WebSocketState?) {
        Log.d(TAG, "onStateChanged ");
    }

    @Throws(java.lang.Exception::class)
    override fun onConnected(webSocket: WebSocket?, map: Map<String?, List<String?>?>?) {
        Log.d(TAG, "Connected");
    }

    @Throws(java.lang.Exception::class)
    override fun onConnectError(webSocket: WebSocket?, e: WebSocketException?) {
        Log.d(TAG, "Connect error")
    }

    @Throws(java.lang.Exception::class)
    override fun onDisconnected(
        webSocket: WebSocket?,
        webSocketFrame: WebSocketFrame?,
        webSocketFrame1: WebSocketFrame?,
        b: Boolean
    ) {
        Log.d(TAG, "onDisconnected websocket=$webSocket s=$")
    }

    @Throws(java.lang.Exception::class)
    override fun onFrame(webSocket: WebSocket?, webSocketFrame: WebSocketFrame?) {
        Log.d(TAG, "onFrame websocket=$webSocket s=$webSocketFrame")
    }

    @Throws(java.lang.Exception::class)
    override fun onContinuationFrame(webSocket: WebSocket?, webSocketFrame: WebSocketFrame?) {
        Log.d(TAG, "onContinuationFrame websocket=$webSocket s=$webSocketFrame")
    }


    @Throws(java.lang.Exception::class)
    override fun onCloseFrame(webSocket: WebSocket?, webSocketFrame: WebSocketFrame?) {
        Log.d(TAG, "onCloseFrame websocket=$webSocket s=$webSocketFrame")
    }

    @Throws(java.lang.Exception::class)
    override fun onPingFrame(webSocket: WebSocket?, webSocketFrame: WebSocketFrame?) {
    }

    @Throws(java.lang.Exception::class)
    override fun onPongFrame(webSocket: WebSocket?, webSocketFrame: WebSocketFrame?) {
    }

    @Throws(java.lang.Exception::class)
    override fun onTextMessage(webSocket: WebSocket?, s: String?) {
        Log.d(TAG, "onTextMessage websocket=$webSocket s=$s")
    }

    @Throws(java.lang.Exception::class)
    override fun onTextMessage(webSocket: WebSocket?, bytes: ByteArray?) {
        Log.d(TAG, "onTextMessage websocket=$webSocket bytes=$bytes")
    }


    @Throws(java.lang.Exception::class)
    override fun onSendingFrame(webSocket: WebSocket?, webSocketFrame: WebSocketFrame?) {
        Log.d(TAG, "onSendingFrame websocket=$webSocket webSocketFrame=$webSocketFrame")
    }

    @Throws(java.lang.Exception::class)
    override fun onFrameSent(webSocket: WebSocket?, webSocketFrame: WebSocketFrame?) {
        Log.d(TAG, "onFrameSent websocket=$webSocket webSocketFrame$webSocketFrame")
    }

    @Throws(java.lang.Exception::class)
    override fun onFrameUnsent(webSocket: WebSocket?, webSocketFrame: WebSocketFrame?) {
        Log.d(TAG, "onFrameUnsent websocket=$webSocket webSocketFrame$webSocketFrame")
    }

    @Throws(java.lang.Exception::class)
    override fun onThreadCreated(
        webSocket: WebSocket?,
        threadType: ThreadType?,
        thread: java.lang.Thread?
    ) {
        Log.d(TAG, "onThreadCreated websocket=$webSocket ")
    }

    @Throws(java.lang.Exception::class)
    override fun onThreadStarted(
        webSocket: WebSocket?,
        threadType: ThreadType?,
        thread: java.lang.Thread?
    ) {
        Log.d(TAG, "onThreadStarted websocket=$webSocket ")
    }

    @Throws(java.lang.Exception::class)
    override fun onThreadStopping(
        webSocket: WebSocket?,
        threadType: ThreadType?,
        thread: java.lang.Thread?
    ) {
        Log.d(TAG, "onThreadStopping websocket=$webSocket ")
    }

    @Throws(java.lang.Exception::class)
    override fun onError(webSocket: WebSocket?, e: WebSocketException?) {
        Log.d(TAG, "onError websocket=$webSocket e=$e ")
    }

    @Throws(java.lang.Exception::class)
    override fun onFrameError(
        webSocket: WebSocket?,
        e: WebSocketException?,
        webSocketFrame: WebSocketFrame?
    ) {
        Log.d(TAG, "onFrameError websocket=$webSocket e=$e webSocketFrame=$webSocketFrame")
    }

    @Throws(java.lang.Exception::class)
    override fun onMessageError(
        webSocket: WebSocket?,
        e: WebSocketException?,
        list: List<WebSocketFrame?>?
    ) {
        Log.d(TAG, "onMessageError websocket=$webSocket e=$e list=$list")
    }

    @Throws(java.lang.Exception::class)
    override fun onMessageDecompressionError(
        webSocket: WebSocket?,
        e: WebSocketException?,
        bytes: ByteArray?
    ) {
        Log.d(TAG, "onMessageDecompressionError websocket=$webSocket e=$e bytes=$bytes")
    }

    @Throws(java.lang.Exception::class)
    override fun onTextMessageError(
        webSocket: WebSocket?,
        e: WebSocketException?,
        bytes: ByteArray?
    ) {
        Log.d(TAG, "onTextMessageError websocket=$webSocket e=$e bytes=$bytes")
    }

    @Throws(java.lang.Exception::class)
    override fun onSendError(
        webSocket: WebSocket?,
        e: WebSocketException?,
        webSocketFrame: WebSocketFrame?
    ) {
        Log.d(TAG, "onSendError websocket=$webSocket e=$e webSocketFrame=$webSocketFrame")
    }

    @Throws(java.lang.Exception::class)
    override fun onUnexpectedError(webSocket: WebSocket?, e: WebSocketException?) {
        Log.d(TAG, "onUnexpectedError websocket=$webSocket e=$e")
    }

    @Throws(java.lang.Exception::class)
    override fun handleCallbackError(webSocket: WebSocket?, throwable: Throwable?) {
        Log.d(TAG, "handleCallbackError websocket=$webSocket s=$throwable")
    }

    @Throws(java.lang.Exception::class)
    override fun onSendingHandshake(
        webSocket: WebSocket?,
        s: String?,
        list: List<Array<String?>?>?
    ) {
        Log.d(TAG, "onSendingHandshake websocket=$webSocket s=$s list=$list")
    }

}