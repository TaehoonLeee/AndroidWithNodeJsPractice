package com.example.nodejs.UI.chat

import android.util.Log
import android.view.View
import android.widget.ScrollView
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.nodejs.MainActivity
import com.example.nodejs.Model.*
import com.example.nodejs.Repository.FcmRepository
import com.example.nodejs.Repository.NodeRepository
import com.example.nodejs.Repository.SocketRepository
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatViewModel @ViewModelInject constructor(
    private val nodeRepository: NodeRepository,
    private val socketRepository: SocketRepository,
    private val fcmRepository: FcmRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _messages = MutableLiveData<List<Message>>()
    val messages : LiveData<List<Message>> = _messages

    private val _friendList = MutableLiveData<List<Friend>>()
    val friendList : LiveData<List<Friend>> = _friendList

    val newMessage : LiveData<String>

    private var roomName : String = savedStateHandle.get<String>("roomName")!!
    private var userName : String = savedStateHandle.get<String>("userName")!!

    init {
        socketRepository.startListening(userName, roomName)
        newMessage = socketRepository.newMessage

        onGetMessages(roomName, userName)
        onGetRelationship(userName)
    }

    fun onGetMessages(userName : String, roomName : String) {
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

    fun addMessage(sender : String, message : String, timeStamp : String, roomName : String, scrollView : ScrollView, token : String) {
        nodeRepository.addMessage(sender, message, timeStamp, roomName).enqueue(object : Callback<Res_Message> {
            override fun onFailure(call: Call<Res_Message>, t: Throwable) {
                Log.e("ChatViewModel", t.message!!)
            }

            override fun onResponse(call: Call<Res_Message>, response: Response<Res_Message>) {
                if (response.isSuccessful) {
                    onCallbackGetMessages(roomName, sender, scrollView)
                    socketRepository.sendMessage(roomName, message)
                    fcmRepository.sendNotification(sender, message, roomName).enqueue(object : Callback<Res_Message> {
                        override fun onFailure(call: Call<Res_Message>, t: Throwable) {

                        }

                        override fun onResponse(
                            call: Call<Res_Message>,
                            response: Response<Res_Message>
                        ) {
                            if(response.isSuccessful) Log.e("FirebaseMessaging", "FCM sendNoti Success")
                        }

                    })
                }
            }
        })
    }

    fun uploadeImage(image : MultipartBody.Part, name : RequestBody,
                     sender : RequestBody, roomName : RequestBody,
                     timeStamp : RequestBody, scrollView: ScrollView) {

        nodeRepository.uploadImage(image, name, sender, roomName, timeStamp).enqueue(object : Callback<Res_Message> {
            override fun onFailure(call: Call<Res_Message>, t: Throwable) {

            }

            override fun onResponse(call: Call<Res_Message>, response: Response<Res_Message>) {
                if(response.isSuccessful) {
                    onCallbackGetMessages(this@ChatViewModel.roomName, userName, scrollView)
                    socketRepository.sendMessage(this@ChatViewModel.roomName, "upload image")
                    fcmRepository.sendNotification(userName, "send Image", this@ChatViewModel.roomName).enqueue(object : Callback<Res_Message> {
                        override fun onFailure(call: Call<Res_Message>, t: Throwable) {

                        }

                        override fun onResponse(call: Call<Res_Message>, response: Response<Res_Message>) {

                        }
                    })
                }
            }
        })
    }

    private fun onGetRelationship(userName : String) {
        nodeRepository.getFriends(userName)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ friends -> _friendList.value = friends.friends }
    }

    fun closeSocket() {
        socketRepository.stopListening()
    }
}