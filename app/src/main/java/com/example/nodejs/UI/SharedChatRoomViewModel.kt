package com.example.nodejs.UI

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.nodejs.Model.ChatRoom
import com.example.nodejs.Model.Res_Message
import com.example.nodejs.Repository.NodeRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SharedChatRoomViewModel @ViewModelInject constructor(
        private val nodeRepository: NodeRepository,
        @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _chatList : MutableLiveData<List<ChatRoom>> = MutableLiveData()
    val chatList : LiveData<List<ChatRoom>> = _chatList

    private val name = savedStateHandle.get<String>("name")!!

    fun onGetChatList(name: String) {
        nodeRepository.getChatList(name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { chatList -> _chatList.value = chatList.chatRoomList }
    }

    fun onGetChatRoomList(name : String) {
        nodeRepository.getAllChatList(name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ chatList -> _chatList.value = chatList.chatRoomList }
    }

    fun onClickRoom(userName : String, roomName : String) {
        nodeRepository.enterRoom(userName, roomName).enqueue(object : Callback<Res_Message> {
            override fun onFailure(call: Call<Res_Message>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<Res_Message>, response: Response<Res_Message>) {
                if (response.isSuccessful) {

                }
            }
        })
    }
}