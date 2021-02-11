package com.example.nodejs.UI.GET

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nodejs.Model.User
import com.example.nodejs.R
import kotlinx.android.synthetic.main.item_user.view.*

class GETAdapter : RecyclerView.Adapter<GETAdapter.GETViewHolder>() {

    private var users : List<User> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GETViewHolder {
        return GETViewHolder.from(parent)
    }

    override fun getItemCount() : Int {
        return users.size
    }

    override fun onBindViewHolder(holder: GETViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
    }

    fun setUsers(users : List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    class GETViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        fun bind(user : User) {
            itemView.apply {
                userId.text = user.id
                userPW.text = user.password
            }
        }
        companion object {
            fun from(parent : ViewGroup) : GETViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val itemView = inflater.inflate(R.layout.item_user, parent, false)

                return GETViewHolder(itemView)
            }

        }

    }
}