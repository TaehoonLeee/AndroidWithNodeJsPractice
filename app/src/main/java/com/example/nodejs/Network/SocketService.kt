package com.example.nodejs.Network

import android.util.Log
import com.example.nodejs.Network.listener.EventListener
import com.example.nodejs.Util.BaseObservable
import io.reactivex.rxjava3.core.Single
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import java.net.URISyntaxException

class SocketService : BaseObservable<EventListener>(){

    lateinit var socket : Socket
    lateinit var userName : String
    lateinit var roomName : String

    private val onConnectListener = Emitter.Listener {
        socket.emit("enter", JSONObject("{\"userName\":\"${userName}\", \"roomName\":\"${roomName}\"}"))
        getListeners().forEachIndexed { _, eventListener ->
            eventListener.onConnect()
        }
    }

    private val onUpdateChatListener = Emitter.Listener {
        getListeners().forEachIndexed { _, eventListener ->
            eventListener.onUpdateChat(it[0] as String)
        }
    }

    private val onDisconnectListener = Emitter.Listener {
        socket.off()
        getListeners().forEachIndexed { _, eventListener ->
            eventListener.onDisconnect()
        }
    }

    private val onSendImageListener = Emitter.Listener {
        getListeners().forEachIndexed { _, eventListener ->
            eventListener.onSendImage()
        }
    }

    fun startListening(userName : String, roomName : String) {
        this.userName = userName
        this.roomName = roomName

        try {
            socket = IO.socket(SOCKET_URL)
        } catch (e : URISyntaxException) { e.printStackTrace() }

        socket.on(EVENT_CONNECT, onConnectListener)
        socket.on(EVENT_UPDATECHAT, onUpdateChatListener)
        socket.on(EVENT_DISCONNECT, onDisconnectListener)
        socket.on(EVENT_SENDIMAGE, onSendImageListener)
        socket.connect()
    }

    fun stopListening() {
        socket?.let { it.disconnect() }
    }

    fun sendMessage(roomName : String, message : String) : Single<String> {
        return Single.create {
            socket.emit(EVENT_NEWMESSAGE, JSONObject("{\"message\":\"${message}\", \"roomName\":\"${roomName}\"}"))
            it.onSuccess(message)
        }
    }

    companion object {
        private const val SOCKET_URL = "http://10.0.2.2:3001/"
        private const val EVENT_CONNECT = Socket.EVENT_CONNECT
        private const val EVENT_DISCONNECT = Socket.EVENT_DISCONNECT
        private const val EVENT_UPDATECHAT = "updateChat"
        private const val EVENT_NEWMESSAGE = "newMessage"
        private const val EVENT_SENDIMAGE = "sendImage"

    }
}