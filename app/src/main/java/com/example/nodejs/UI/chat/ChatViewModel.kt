package com.example.nodejs.UI.chat

import android.util.Log
import android.view.View
import android.widget.ScrollView
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.nodejs.Model.Friend
import com.example.nodejs.Model.Message
import com.example.nodejs.Model.Res_Message
import com.example.nodejs.Repository.NodeRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatViewModel @ViewModelInject constructor(
    private val nodeRepository: NodeRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _messages = MutableLiveData<List<Message>>()
    val messages : LiveData<List<Message>> = _messages

    private val _friendList = MutableLiveData<List<Friend>>()
    val friendList : LiveData<List<Friend>> = _friendList

    private lateinit var socket: Socket
    private var roomName : String = savedStateHandle.get<String>("roomName")!!
    private var userName : String = savedStateHandle.get<String>("userName")!!

    init {
        try {
            socket = IO.socket("http://10.0.2.2:3001/")
        } catch (e : Exception) { e.printStackTrace() }

        socket.connect()
        socket.on(Socket.EVENT_CONNECT, Emitter.Listener {
            socket.emit("enter", JSONObject("{\"userName\":\"${userName}\", \"roomName\":\"${roomName}\"}"))
        })
        socket.on("updateChat", Emitter.Listener {
            Log.e("Socket Service : UpdateChat", it[0] as String)
            Log.e("Socket Service : UpdateChat", "test")
            onGetMessages(it[0] as String, userName)
        })

        onGetMessages(roomName, userName)
        onGetRelationship(userName)
    }

    private fun onGetMessages(userName : String, roomName : String) {
        nodeRepository.getMessages(roomName, userName)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ messages -> _messages.value = messages.messages }
    }

    private fun onCallbackGetMessages(userName : String, roomName : String, scrollView: ScrollView) {
        nodeRepository.getMessages(roomName, userName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { messages ->
                    _messages.value = messages.messages
                    scrollView.post {
                        scrollView.fullScroll(View.FOCUS_DOWN)
                    }
                }
    }

    fun addMessage(sender : String, message : String, timeStamp : String, roomName : String, scrollView : ScrollView) {
        nodeRepository.addMessage(sender, message, timeStamp, roomName).enqueue(object : Callback<Res_Message> {
            override fun onFailure(call: Call<Res_Message>, t: Throwable) {
                Log.e("ChatViewModel", t.message!!)
            }

            override fun onResponse(call: Call<Res_Message>, response: Response<Res_Message>) {
                if (response.isSuccessful) {
                    onCallbackGetMessages(roomName, sender, scrollView)
                    socket.emit("newMessage", roomName)
                }
            }
        })
    }

    fun onGetRelationship(userName : String) {
        nodeRepository.getFriends(userName)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ friends -> _friendList.value = friends.friends }
    }
}