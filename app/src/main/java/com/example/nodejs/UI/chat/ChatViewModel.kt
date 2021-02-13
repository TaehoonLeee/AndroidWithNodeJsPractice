package com.example.nodejs.UI.chat

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nodejs.Model.Message
import com.example.nodejs.Repository.NodeRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import java.sql.Time

class ChatViewModel @ViewModelInject constructor(
    private val nodeRepository: NodeRepository
): ViewModel() {

    private val _messages = MutableLiveData<List<Message>>()
    val messages : LiveData<List<Message>> = _messages

    init {
//        onGetMessages()
        val tmpMsg = Message(
            sender = "notMaster",
            message = "testMessage",
            timeStamp = Time(System.currentTimeMillis()/1000)
        )

        val tmpMsg2 = Message(
            sender = "master",
            message = "testMessage",
            timeStamp = Time(System.currentTimeMillis()/1000)
        )

        val tmpMessageList = listOf<Message>(tmpMsg, tmpMsg2)
        _messages.value = tmpMessageList
    }

    private fun onGetMessages() {
        nodeRepository.getMessages()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ messages -> _messages.value = messages.messages }
    }
}