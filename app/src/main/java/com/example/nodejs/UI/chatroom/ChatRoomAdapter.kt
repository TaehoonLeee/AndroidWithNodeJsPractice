package com.example.nodejs.UI.chatroom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.nodejs.Model.ChatList
import com.example.nodejs.Model.ChatRoom
import com.example.nodejs.R
import com.example.nodejs.glide.GlideApp
import kotlinx.android.synthetic.main.item_chat_room.view.*

class ChatRoomAdapter(val userName : String) : RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder>() {

    private var chatList : List<ChatRoom> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_chat_room, parent, false)

        return ChatRoomViewHolder(view)
    }

    override fun getItemCount(): Int = chatList.size

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        val name = chatList[position]
        holder.bind(name)
    }

    fun setChatList(chatList : List<ChatRoom>) {

        this.chatList = chatList.sortedWith(Comparator {
            o1, o2 -> o2.topIndex!!.compareTo(o1.topIndex!!)
        })
        notifyDataSetChanged()
    }

    fun onSearchList(query : String) {
        val tmpList : MutableList<ChatRoom> = mutableListOf()
        repeat(chatList.size) {
            if(chatList[it].name.contains(query)) {
                tmpList.add(chatList[it])
            }
        }
        this.chatList = tmpList
        notifyDataSetChanged()
    }

    inner class ChatRoomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(chatRoom : ChatRoom) {
            itemView.setOnClickListener {
                val direction =
                    ChatRoomFragmentDirections.actionChatRoomFragmentToChatFragment(userName, chatRoom.name)
                it.findNavController().navigate(direction)
            }
            itemView.apply {
                GlideApp.with(chatProfile)
                    .load(R.drawable.my_image)
                    .transform(CircleCrop())
                    .thumbnail(0.5f)
                    .into(chatProfile)
                roomName.text = chatRoom.name.split("-").filter { it != userName }.joinToString()
                access.visibility = if (chatRoom.access != "public") View.GONE else View.VISIBLE
                topMessage.text = chatRoom.topMessage
                topTimeStamp.text = chatRoom.topTimeStamp
                memberNumber.text = chatRoom.memberNumber.toString()
            }
        }
    }
}