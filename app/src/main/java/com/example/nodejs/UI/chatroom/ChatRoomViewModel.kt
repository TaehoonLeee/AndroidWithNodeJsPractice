package com.example.nodejs.UI.chatroom

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.nodejs.Model.ChatList
import com.example.nodejs.Model.ChatRoom
import com.example.nodejs.Model.Res_Message
import com.example.nodejs.Repository.NodeRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import okhttp3.internal.userAgent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatRoomViewModel @ViewModelInject constructor(
    private val nodeRepository: NodeRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _chatList : MutableLiveData<List<ChatRoom>> = MutableLiveData()
    val chatList : LiveData<List<ChatRoom>> = _chatList

//    private val name = savedStateHandle.get<String>("name")!!
//
//    init {
//        onGetChatList(name)
//    }

    fun onGetChatList(name: String) {
        nodeRepository.getChatList(name)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { chatList -> _chatList.value = chatList.chatRoomList }
    }

    fun exitRoom(userName : String, chatRoom: ChatRoom) {
        nodeRepository.exitRoom(userName, chatRoom.name).enqueue(object : Callback<Res_Message> {
            override fun onFailure(call: Call<Res_Message>, t: Throwable) {

            }

            override fun onResponse(call: Call<Res_Message>, response: Response<Res_Message>) {
                onGetChatList(userName)
            }
        })
    }
}