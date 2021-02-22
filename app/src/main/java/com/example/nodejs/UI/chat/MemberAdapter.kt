package com.example.nodejs.UI.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nodejs.Model.Member
import com.example.nodejs.R
import kotlinx.android.synthetic.main.item_member.view.*

class MemberAdapter : RecyclerView.Adapter<MemberAdapter.MemberViewHolder>(){
    private var members : List<Member> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_member, parent, false)

        return MemberViewHolder(view)
    }

    override fun getItemCount(): Int = members.size

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val member = members[position]
        holder.bind(member)
    }

    fun setMembers(members : List<Member>) {
        this.members = members
    }
    inner class MemberViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        fun bind(member : Member) {
            itemView.apply {
                memberName.text = member.name
            }
        }

    }
}