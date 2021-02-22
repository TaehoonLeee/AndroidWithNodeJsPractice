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
import com.example.nodejs.Model.Message
import com.example.nodejs.Model.Res_Message
import com.example.nodejs.Repository.NodeRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatViewModel @ViewModelInject constructor(
    private val nodeRepository: NodeRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _messages = MutableLiveData<List<Message>>()
    val messages : LiveData<List<Message>> = _messages

    private var roomName : String = savedStateHandle.get<String>("roomName")!!

    init {
        onGetMessages(roomName)
    }

    private fun onGetMessages(name : String) {
        nodeRepository.getMessages(name)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ messages -> _messages.value = messages.messages }
    }

    private fun onCallbackGetMessages(name : String, scrollView: ScrollView) {
        nodeRepository.getMessages(name)
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
                    onCallbackGetMessages(roomName, scrollView)
                }
            }
        })
    }
}