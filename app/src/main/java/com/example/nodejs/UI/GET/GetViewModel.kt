package com.example.nodejs.UI.GET

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nodejs.Model.User
import com.example.nodejs.Repository.NodeRepository
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

class GetViewModel @ViewModelInject constructor(
    private val nodeRepository: NodeRepository
) : ViewModel() {
    private val _Users = MutableLiveData<List<User>>()

    val Users : LiveData<List<User>>
        get() = _Users

    init {
        onGetData()
    }

    private fun onGetData() {
        nodeRepository.getData()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { user -> _Users.value = user.users }
    }
}