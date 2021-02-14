package com.example.nodejs.UI.POST

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.nodejs.Model.User
import com.example.nodejs.Repository.NodeRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostViewModel @ViewModelInject constructor(
    private val nodeRepository: NodeRepository
) : ViewModel(){

    fun setData(id : String, password : String) {
        nodeRepository.setData(id, password).enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {

            }

            override fun onResponse(call: Call<User>, response: Response<User>) {

            }

        })
    }

}