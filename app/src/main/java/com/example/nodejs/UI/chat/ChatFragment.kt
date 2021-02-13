package com.example.nodejs.UI.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nodejs.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.chat_fragment.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ChatFragment : Fragment(R.layout.chat_fragment) {

    private val chatViewModel : ChatViewModel by viewModels<ChatViewModel>()
    private lateinit var chatAdapter : ChatAdapter

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
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        chatViewModel.messages.observe(viewLifecycleOwner, Observer {
            chatAdapter.setMessages(it)
        })
    }

}