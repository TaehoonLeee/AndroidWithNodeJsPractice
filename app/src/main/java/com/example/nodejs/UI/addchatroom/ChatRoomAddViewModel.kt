package com.example.nodejs.UI.addchatroom

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.nodejs.Model.ChatRoom
import com.example.nodejs.Repository.NodeRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

class ChatRoomAddViewModel @ViewModelInject constructor(
    private val nodeRepository: NodeRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _chatList = MutableLiveData<List<ChatRoom>>()
    val chatList : LiveData<List<ChatRoom>> = _chatList

    private val userName = savedStateHandle.get<String>("name")!!

    init {
        onGetChatRoomList(userName)
    }

    private fun onGetChatRoomList(name : String) {
        nodeRepository.getAllChatList(name)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ chatList -> _chatList.value = chatList.chatRoomList }
    }
}