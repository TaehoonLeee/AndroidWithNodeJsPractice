package com.example.nodejs.UI.chat

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nodejs.Model.Message
import com.example.nodejs.Repository.NodeRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatViewModel @ViewModelInject constructor(
    private val nodeRepository: NodeRepository
): ViewModel() {

    private val _messages = MutableLiveData<List<Message>>()
    val messages : LiveData<List<Message>> = _messages

    init {
        onGetMessages()
    }

    private fun onGetMessages() {
        nodeRepository.getMessages()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ messages -> _messages.value = messages.messages }
    }

    fun addMessage(sender : String, message : String, timeStamp : String) {
        Log.e("ViewModel", "message : $message, $timeStamp")
        nodeRepository.addMessage(sender, message, timeStamp).enqueue(object : Callback<Message> {
            override fun onFailure(call: Call<Message>, t: Throwable) {

            }

            override fun onResponse(call: Call<Message>, response: Response<Message>) {

            }
        })
    }
}