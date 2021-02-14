package com.example.nodejs.UI.addchatroom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.nodejs.Model.ChatRoom
import com.example.nodejs.R
import kotlinx.android.synthetic.main.item_chat_room.view.*

class AllChatRoomAdapter(val onRoomClick : (ChatRoom) -> Unit) : RecyclerView.Adapter<AllChatRoomAdapter.AllChatRoomViewHolder>() {

    private var chatList : List<ChatRoom> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllChatRoomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_chat_room, parent, false)

        return AllChatRoomViewHolder(view)
    }

    override fun getItemCount(): Int = chatList.size

    override fun onBindViewHolder(holder: AllChatRoomViewHolder, position: Int) {
        val name = chatList[position]
        holder.bind(name)
    }

    fun setChatList(chatList : List<ChatRoom>) {
        this.chatList = chatList
        notifyDataSetChanged()
    }


    inner class AllChatRoomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(chatRoom : ChatRoom) {
            itemView.setOnClickListener {
                onRoomClick(chatRoom)
            }
            itemView.apply {
                roomName.text = chatRoom.name
                memberNumber.text = chatRoom.memberNumber.toString()
            }
        }
    }

}
