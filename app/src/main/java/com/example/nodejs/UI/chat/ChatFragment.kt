package com.example.nodejs.UI.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nodejs.MainActivity
import com.example.nodejs.Model.Friend
import com.example.nodejs.R
import com.example.nodejs.UI.profile.ProfileFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.chat_fragment.*
import kotlinx.android.synthetic.main.item_chat_room.view.*
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ChatFragment : Fragment(R.layout.chat_fragment) {

    private lateinit var socket: Socket
    private val chatViewModel : ChatViewModel by viewModels<ChatViewModel>()
    private lateinit var chatAdapter : ChatAdapter
    private val args : ChatFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userName = args.userName
        val roomName = args.roomName

        chatAdapter = ChatAdapter(userName, {friend ->
            val isFriend = chatViewModel.friendList.value!!.contains(friend)
            val direction =
                ProfileFragmentDirections.actionGlobalProfileFragment(friend.name, isFriend)
            findNavController().navigate(direction)
        })

        rvChat.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
        }

        btnSend.setOnClickListener {
            val currentTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("h:mm a")
            val formatted = currentTime.format(formatter)

            chatViewModel.addMessage(userName, msgToSend.text.toString(), formatted, roomName, chatScrollView)
            msgToSend.text = null
        }

        chatToolbar.title = roomName.split("-").filter { it != userName }.joinToString()

        focusDownfab.setOnClickListener {
            focusDown(false)
        }

        chatToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.infoBtn -> {
                    val direction =
                        ChatFragmentDirections.actionChatFragmentToMemberInfoFragment(roomName)
                    findNavController().navigate(direction)

                    true
                }
                else -> false
            }
        }
    }

    private fun focusDown(isStart : Boolean) {
        // 채팅 뷰 하단으로 내리기
        when(isStart) {
            true -> {
                chatScrollView.postDelayed ({
                    chatScrollView.fullScroll(View.FOCUS_DOWN)
                }, 1000)
            }
            false -> {
                chatScrollView.post {
                    chatScrollView.fullScroll(View.FOCUS_DOWN)
                }
            }
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        chatViewModel.messages.observe(viewLifecycleOwner, Observer {
            if(it.isEmpty()) {
                topText.visibility = View.GONE
            }
            chatAdapter.setMessages(it)
        })

        chatViewModel.newMessage.observe(viewLifecycleOwner, Observer {
            chatViewModel.onGetMessages(args.roomName, args.userName)
            focusDown(true)
        })

        focusDown(true)
        chatScrollView?.viewTreeObserver?.addOnScrollChangedListener {
            if (chatScrollView != null) {
                val scrollY = chatScrollView.scrollY
                Log.e("VIEWTREE", scrollY.toString())
                val totalHeight = chatScrollView.getChildAt(0).height - chatScrollView.height
                if (totalHeight - scrollY > 200) {
                    focusDownfab.show()
                }
                else {
                    focusDownfab.hide()
                }
            }
        }

        (activity as MainActivity).isShowBar(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        chatViewModel.closeSocket()
    }
}