package com.example.nodejs.UI.entry

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.nodejs.Model.Res_Message
import com.example.nodejs.Repository.NodeRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel @ViewModelInject constructor(
    private val nodeRepository: NodeRepository
): ViewModel() {
    fun onCreateMember(userName : String) {
        nodeRepository.createMember(userName).enqueue(object : Callback<Res_Message> {
            override fun onFailure(call: Call<Res_Message>, t: Throwable) {

            }

            override fun onResponse(call: Call<Res_Message>, response: Response<Res_Message>) {

            }

        })
    }
}