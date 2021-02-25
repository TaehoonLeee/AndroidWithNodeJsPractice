package com.example.nodejs.UI.friend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.nodejs.Model.Friend
import com.example.nodejs.R
import com.example.nodejs.UI.profile.ProfileFragmentDirections
import kotlinx.android.synthetic.main.item_friend.view.*

class FriendAdapter : RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {
    private var friends : List<Friend> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)

        return FriendViewHolder(view)
    }

    override fun getItemCount(): Int = friends.size

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend = friends[position]
        holder.bind(friend)
    }

    fun setFriends(friends : List<Friend>) {
        this.friends = friends
        notifyDataSetChanged()
    }

    fun onSearchFriends(query : String) {
        val tmpList : MutableList<Friend> = mutableListOf()
        repeat(friends.size) {
            if(friends[it].name.contains(query)) tmpList.add(friends[it])
        }
        this.friends = tmpList
        notifyDataSetChanged()
    }

    inner class FriendViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        fun bind(friend : Friend) {
            itemView.setOnClickListener {
                val direction =
                    ProfileFragmentDirections.actionGlobalProfileFragment(friend.name, true)
                it.findNavController().navigate(direction)
            }
            itemView.apply {
                friendName.text = friend.name
            }
        }
    }
}