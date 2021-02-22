package com.example.nodejs.UI.chat

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.nodejs.Model.Member
import com.example.nodejs.Repository.NodeRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

class MemberInfoViewModel @ViewModelInject constructor(
    private val nodeRepository: NodeRepository,
    @Assisted savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _members : MutableLiveData<List<Member>> = MutableLiveData()
    val members : LiveData<List<Member>> = _members

    private val roomName = savedStateHandle.get<String>("roomName")!!

    init {
        onGetChatMembers()
    }

    fun onGetChatMembers() {
        nodeRepository.getChatMemebers(roomName)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ members -> _members.value = members.members }
    }
}