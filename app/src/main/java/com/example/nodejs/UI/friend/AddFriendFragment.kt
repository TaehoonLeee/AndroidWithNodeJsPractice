package com.example.nodejs.UI.friend

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nodejs.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.add_friend_fragment.*

@AndroidEntryPoint
class AddFriendFragment : BottomSheetDialogFragment() {

    private val addFriendsViewModel : AddFriendViewModel by viewModels()
    private lateinit var friendCandidateAdapter: FriendAdapter
    private val args : AddFriendFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.add_friend_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        friendCandidateAdapter = FriendAdapter()
        rvFriendsCandidate.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = friendCandidateAdapter
        }

        findBtn.setOnClickListener {
            findFriendCandidateList()
            rvFriendsCandidate.visibility = View.VISIBLE
        }

    }

    private fun findFriendCandidateList() {
        val regex = "${findFriendText.text}%"
        addFriendsViewModel.onGetFriendCandidateList(regex, args.userName)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        addFriendsViewModel.friendCandidateList.observe(viewLifecycleOwner, Observer {
            friendCandidateAdapter.setFriends(it)
        })
    }

}