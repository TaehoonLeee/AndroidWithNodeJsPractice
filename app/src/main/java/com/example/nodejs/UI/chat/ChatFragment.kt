package com.example.nodejs.UI.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nodejs.Model.Message
import com.example.nodejs.R
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.chat_fragment.*
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ChatFragment : Fragment(R.layout.chat_fragment) {

    private val chatViewModel : ChatViewModel by viewModels<ChatViewModel>()
    private lateinit var chatAdapter : ChatAdapter
    private lateinit var socket : Socket
//    private var firstMessageList : MutableList<Message> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        try {
//            socket = IO.socket("http://10.0.2.2:3001/")
//        } catch (e : Exception) { e.printStackTrace() }
//
//        socket.connect()
//        socket.on(Socket.EVENT_CONNECT, Emitter.Listener {
//            socket.emit("first")
//        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatAdapter = ChatAdapter()

        rvChat.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
        }

        btnSend.setOnClickListener {
            val currentTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("h:mm a")
            val formatted = currentTime.format(formatter)

            chatViewModel.addMessage("master", msgToSend.text.toString(), formatted)
            msgToSend.text = null
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        socket.on("first", Emitter.Listener {
//            val jsonArray = it[0] as JSONArray
//            repeat(jsonArray.length()) { index ->
//                val jsonObject = jsonArray[index] as JSONObject
//                val sender = jsonObject.getString("sender")
//                val message = jsonObject.getString("message")
//                val timeStamp = jsonObject.getString("timeStamp")
//                firstMessageList.add(Message(sender, message, timeStamp))
//                Log.e("By server", "Data is ${jsonObject.getString("message")}")
//            }
//        })

        chatViewModel.messages.observe(viewLifecycleOwner, Observer {
            chatAdapter.setMessages(it)
        })
//        repeat(firstMessageList.size) {
//            Log.e("ActivityCreated", firstMessageList[it].timeStamp)
//        }
//        chatAdapter.setMessages(firstMessageList)
    }

}