package com.example.nodejs.UI.addchatroom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nodejs.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.chat_room_add_fragment.*

@AndroidEntryPoint
class ChatRoomAddFragment : Fragment(R.layout.chat_room_add_fragment) {

    private val chatRoomAddViewModel : ChatRoomAddViewModel by viewModels()
    private lateinit var chatRoomAdapter : AllChatRoomAdapter
    private val args : ChatRoomAddFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatRoomAdapter = AllChatRoomAdapter()

        rvChatRoomList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatRoomAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        chatRoomAddViewModel.chatList.observe(viewLifecycleOwner, Observer {
            chatRoomAdapter.setChatList(it)
        })
    }

}