package com.example.nodejs.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.nodejs.Network.SocketService
import com.example.nodejs.Network.listener.EventListener
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketRepository @Inject constructor(
    private val socketService : SocketService
) : EventListener {

    private val _newMessage = MutableLiveData<String>()
    val newMessage = _newMessage

    private val state = MutableLiveData<String>()

    fun startListening(userName : String, roomName : String) {
        Log.e("SocketRepository", "onStartListening")
        socketService.registerListener(this)
        socketService.startListening(userName, roomName)
    }

    fun stopListening() {
        socketService.unregisterListener(this)
        socketService.stopListening()
    }

    fun sendMessage(roomName: String, message: String) {
        socketService.sendMessage(roomName, message)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { message -> _newMessage.value = message }
    }

    override fun onConnect() {
        state.postValue("onConnect")
    }

    override fun onDisconnect() {
        state.postValue("onDisconnect")
    }

    override fun onUpdateChat(message: String) {
        _newMessage.postValue(message)
    }
}