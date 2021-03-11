package com.example.nodejs.UI.friend

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nodejs.MainActivity
import com.example.nodejs.R
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.friends_fragment.*
import kotlinx.android.synthetic.main.search_layout.*

@AndroidEntryPoint
class FriendsFragment : Fragment(R.layout.friends_fragment) {

    private val friendsViewModel : FriendsViewModel by viewModels()
    private lateinit var friendAdapter: FriendAdapter
    private lateinit var userName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userName = (activity as MainActivity).getUserName()
        friendsViewModel.onGetFriends(userName)
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if(!it.isSuccessful) {
                Log.e("FirebaseMessaging", it.exception!!.message!!)
                return@addOnCompleteListener
            }

            val token = it.result
            (activity as MainActivity).setToken(token!!)
            Log.e("FirebaseMessaging", "Token : $token")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        friendAdapter = FriendAdapter()
        rvFriendsList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = friendAdapter
        }

        friendsToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.plusBtn -> {
                    val direction =
                        FriendsFragmentDirections.actionFriendsFragmentToAddFriendFragment(userName)
                    findNavController().navigate(direction)

                    true
                }
                else -> false
            }
        }

        searchView.queryHint = "친구 이름을 검색하세요."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(!newText.isNullOrEmpty()) {
                    friendAdapter.setFriends(friendsViewModel.friends.value!!)
                    friendAdapter.onSearchFriends(newText)
                }
                else {
                    friendAdapter.setFriends(friendsViewModel.friends.value!!)
                }

                return false
            }

        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).isShowBar(true)
        friendsViewModel.friends.observe(viewLifecycleOwner, Observer {
            friendAdapter.setFriends(it)
        })
    }

    override fun onResume() {
        super.onResume()
        friendsViewModel.onGetFriends(userName)
    }
}