package com.example.nodejs.UI.chat

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.nodejs.Model.Friend
import com.example.nodejs.Model.Message
import com.example.nodejs.R
import com.example.nodejs.UI.profile.ProfileFragmentDirections
import com.example.nodejs.glide.GlideApp
import kotlinx.android.synthetic.main.item_chat_receiver.view.*
import kotlinx.android.synthetic.main.item_chat_sender.view.*

class ChatAdapter(
    val name : String,
    val onProfileClcik : (Friend) -> Unit
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
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
                        if(message.category == "txt") {
                            txtReceiverMessage.visibility = View.VISIBLE
                            imgReceiverMessage.visibility = View.GONE
                            txtReceiverMessage.text = message.message
                        }
                        else {
                            imgReceiverMessage.visibility = View.VISIBLE
                            txtReceiverMessage.visibility = View.GONE
                            GlideApp.with(imgReceiverMessage)
                                    .load(message.url)
                                    .transform(CenterCrop(), RoundedCorners(25))
                                    .into(imgReceiverMessage)
                        }
                        txtReceiverDateTime.text = message.timeStamp

                        chatReceiverImage.setOnClickListener {
                            onProfileClcik(Friend(message.sender))
                        }
                    }
                    SENDER -> {
                        if(message.category == "txt") {
                            txtSenderMessage.visibility = View.VISIBLE
                            imgSenderMessage.visibility = View.GONE
                        }
                        else {
                            Log.e("Chat Adapter", message.url!!)
                            imgSenderMessage.visibility = View.VISIBLE
                            txtSenderMessage.visibility = View.GONE
                            GlideApp.with(imgSenderMessage)
                                    .load(message.url)
                                    .transform(CenterCrop(), RoundedCorners(25))
                                    .into(imgSenderMessage)
                        }
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