package com.example.nodejs.UI.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.nodejs.MainActivity
import com.example.nodejs.R
import com.example.nodejs.glide.GlideApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_friend.view.*
import kotlinx.android.synthetic.main.profile_fragment.*

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.profile_fragment) {

    private val profileViewModel : ProfileViewModel by viewModels()
    private val args : ProfileFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user_name.text = args.userName

        val isFriend = args.isFriend
        friendAddBtn.visibility = if( isFriend ) View.INVISIBLE else View.VISIBLE
        friendChatBtn.visibility = if( isFriend ) View.VISIBLE else View.INVISIBLE
        GlideApp.with(profileImage)
            .load(R.drawable.my_image)
            .transform(CircleCrop())
            .thumbnail(0.5f)
            .into(profileImage)

        friendAddBtn.setOnClickListener {
            val myName = (activity as MainActivity).getUserName()
            profileViewModel.addFriend(myName, args.userName, it.findNavController())
        }

        friendChatBtn.setOnClickListener {
            val myName = (activity as MainActivity).getUserName()
            profileViewModel.onClickChat(myName, "$myName-${args.userName}", it.findNavController())
        }
    }

}