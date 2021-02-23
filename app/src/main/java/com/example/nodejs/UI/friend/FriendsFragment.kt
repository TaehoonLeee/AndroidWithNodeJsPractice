package com.example.nodejs.UI.friend

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nodejs.MainActivity
import com.example.nodejs.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.friends_fragment.*

@AndroidEntryPoint
class FriendsFragment : Fragment(R.layout.friends_fragment) {

    private val friendsViewModel : FriendsViewModel by viewModels()
    private lateinit var friendAdapter: FriendAdapter
    private lateinit var userName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userName = (activity as MainActivity).getUserName()
        friendsViewModel.onGetFriends(userName)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        friendAdapter = FriendAdapter()
        rvFriendsList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = friendAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).isShowBar(true)
        friendsViewModel.friends.observe(viewLifecycleOwner, Observer {
            it.forEachIndexed { _, friend ->
                Log.e("Friends", (friend.name))
            }
            friendAdapter.setFriends(it)
        })
    }

    override fun onResume() {
        super.onResume()
        Log.e("Friends", "onResume")
        friendsViewModel.onGetFriends(userName)
    }
}