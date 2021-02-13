package com.example.nodejs.UI.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nodejs.Model.Message
import com.example.nodejs.R
import kotlinx.android.synthetic.main.item_chat_receiver.view.*
import kotlinx.android.synthetic.main.item_chat_sender.view.*

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    private var messages : List<Message> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        if (viewType == RECEIVER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_receiver, parent, false)
            return ChatViewHolder(view)
        }
        else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_sender, parent, false)
            return ChatViewHolder(view)
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].sender == "master") SENDER else RECEIVER
    }

    fun setMessages(messages : List<Message>) {
        this.messages = messages
        notifyDataSetChanged()
    }

    inner class ChatViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: Message) {
            itemView.apply {
                when (itemViewType) {
                    RECEIVER -> {
                        txtReceiverMessage.text = message.message
                        txtReceiverDateTime.text = message.timeStamp.toString()
                    }
                    SENDER -> {
                        txtSenderMessage.text = message.message
                        txtSenderDateTime.text = message.timeStamp.toString()
                    }
                }
            }
        }
    }

    companion object {
        const val RECEIVER = 0
        const val SENDER = 1
    }
}