package com.example.nodejs.UI.chatroom

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nodejs.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.chat_fragment.*
import kotlinx.android.synthetic.main.fragment_chat_room.*
import kotlinx.android.synthetic.main.search_layout.*

@AndroidEntryPoint
class ChatRoomFragment : Fragment(R.layout.fragment_chat_room) {

    private val chatRoomViewModel : ChatRoomViewModel by viewModels()
    private lateinit var chatRoomAdapter : ChatRoomAdapter
    private val args: ChatRoomFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = args.name
        chatRoomAdapter = ChatRoomAdapter(name)

        rvChatList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatRoomAdapter
        }

        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(!newText.isNullOrEmpty()) {
                    chatRoomAdapter.setChatList(chatRoomViewModel.chatList.value!!)
                    chatRoomAdapter.onSearchList(newText)
                }
                else {
                    chatRoomAdapter.setChatList(chatRoomViewModel.chatList.value!!)
                }

                return false
            }
        })

        chatRoomToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.plusBtn -> {
                    val direction =
                        ChatRoomFragmentDirections.actionChatRoomFragmentToChatRoomAddFragment(name = args.name)
                    findNavController().navigate(direction)
                    true
                }

                else -> false
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        chatRoomViewModel.chatList.observe(viewLifecycleOwner, Observer {
            chatRoomAdapter.setChatList(it)
        })
    }
}