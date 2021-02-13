package com.example.nodejs.UI.chat

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nodejs.Model.Message
import com.example.nodejs.Model.Res_Message
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
        nodeRepository.addMessage(sender, message, timeStamp).enqueue(object : Callback<Res_Message> {
            override fun onFailure(call: Call<Res_Message>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<Res_Message>, response: Response<Res_Message>) {
                if( response.isSuccessful ) {
                    onGetMessages()
                }
            }
        })
    }
}