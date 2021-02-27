package com.example.nodejs.Network

import android.util.Log
import com.example.nodejs.Model.MessageJsonAdapter
import com.squareup.moshi.Moshi
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class SocketService {

    private val TAG = this.javaClass.simpleName
    private val SOCKET_URL = "https://10.0.2.2:3000/"
    private val EVENT_CONNECT = Socket.EVENT_CONNECT
    private val EVENT_RECONNECT = Socket.EVENT_RECONNECT
    private val EVENT_DISCONNECT = Socket.EVENT_DISCONNECT
    private val EVENT_CONNECT_ERROR = Socket.EVENT_CONNECT_ERROR

    lateinit var socket : Socket
    lateinit var userName : String
    lateinit var roomName : String

    private val connectListener : Emitter.Listener = Emitter.Listener {
        Log.e(TAG, "onConnect...")
        socket.emit("Add user", userName, roomName)
    }

    private val reconnectListener : Emitter.Listener = Emitter.Listener {
        Log.e(TAG, "onReconnect...")
    }

    private val connectionErrorListener : Emitter.Listener = Emitter.Listener {
        Log.e(TAG, "onConnectionError...")
    }

    private val disconnectListener : Emitter.Listener = Emitter.Listener {
        Log.e(TAG, "onDisconnect...")
        socket.off()
    }

    private val newMessageListener : Emitter.Listener = Emitter.Listener {
        val rawMessage = it[0].toString()
        Log.e(TAG, "onMessage: $rawMessage")
        try {
            val currentTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("h:mm a")
            val formatted = currentTime.format(formatter)
            val adapter = MessageJsonAdapter(moshi = Moshi.Builder().build())

        } catch (e:Exception) {e.printStackTrace()}
    }
}