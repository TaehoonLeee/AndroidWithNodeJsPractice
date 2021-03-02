package com.example.nodejs.UI.chatroom

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nodejs.MainActivity
import com.example.nodejs.R
import com.example.nodejs.Util.SwipeHelperCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_chat_room.*
import kotlinx.android.synthetic.main.search_layout.*

@AndroidEntryPoint
class ChatRoomFragment : Fragment(R.layout.fragment_chat_room) {

    private val chatRoomViewModel : ChatRoomViewModel by viewModels()
    private lateinit var chatRoomAdapter : ChatRoomAdapter
    private lateinit var userName : String

//    private val args: ChatRoomFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userName = (activity as MainActivity).getUserName()
        chatRoomViewModel.onGetChatList(userName)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatRoomAdapter = ChatRoomAdapter(userName, {
            chatRoomViewModel.exitRoom(it)
        })

        val swipeHelperCallback = SwipeHelperCallback().apply {
            setClamp(300f)
        }
        val itemTouchHelper = ItemTouchHelper(swipeHelperCallback)
        itemTouchHelper.attachToRecyclerView(rvChatList)
        rvChatList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatRoomAdapter
            setOnTouchListener { _, _ ->
                swipeHelperCallback.removePreviousClamp(this)
                false
            }
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
                        ChatRoomFragmentDirections.actionChatRoomFragmentToChatRoomAddFragment(name = userName)
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

        (activity as MainActivity).isShowBar(true)
    }

    override fun onResume() {
        super.onResume()
        chatRoomViewModel.onGetChatList(userName)
    }
}