package com.example.nodejs.UI.friend

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.nodejs.Model.Friend
import com.example.nodejs.Repository.NodeRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

class AddFriendViewModel @ViewModelInject constructor(
    private val nodeRepository: NodeRepository,
    @Assisted savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _friendCandidateList : MutableLiveData<List<Friend>> = MutableLiveData()
    val friendCandidateList = _friendCandidateList

    fun onGetFriendCandidateList(regex : String, userName : String) {
        nodeRepository.getFriendCandidate(regex, userName)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ friendCandidateList -> _friendCandidateList.value = friendCandidateList.friends }
    }
}