package com.example.nodejs.UI.friend

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nodejs.Model.Friend
import com.example.nodejs.Repository.NodeRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

class FriendsViewModel @ViewModelInject constructor(
        private val nodeRepository: NodeRepository
): ViewModel() {
    private val _friends : MutableLiveData<List<Friend>> = MutableLiveData()
    val friends : LiveData<List<Friend>> = _friends

    fun onGetFriends(userName : String) {
        nodeRepository.getFriends(userName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ friends -> _friends.value = friends.friends }
    }
}