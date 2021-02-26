package com.example.nodejs.UI.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.nodejs.Model.Message
import com.example.nodejs.R
import com.example.nodejs.UI.profile.ProfileFragmentDirections
import com.example.nodejs.glide.GlideApp
import kotlinx.android.synthetic.main.item_chat_receiver.view.*
import kotlinx.android.synthetic.main.item_chat_sender.view.*

class ChatAdapter(val name : String) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
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
        return if (messages[position].sender == name) SENDER else RECEIVER
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
                        GlideApp.with(chatReceiverImage)
                            .load(R.drawable.my_image)
                            .transform(CircleCrop())
                            .thumbnail(0.1f)
                            .into(chatReceiverImage)
                        receiverName.text = message.sender
                        txtReceiverMessage.text = message.message
                        txtReceiverDateTime.text = message.timeStamp

                        chatReceiverImage.setOnClickListener {
                            val direction =
                                ProfileFragmentDirections.actionGlobalProfileFragment(message.sender, true)
                            findNavController().navigate(direction)
                        }
                    }
                    SENDER -> {
                        txtSenderMessage.text = message.message
                        txtSenderDateTime.text = message.timeStamp
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