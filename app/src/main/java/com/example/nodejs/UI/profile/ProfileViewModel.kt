package com.example.nodejs.UI.profile

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.nodejs.Model.Res_Message
import com.example.nodejs.Repository.NodeRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel @ViewModelInject constructor(
    private val nodeRepository: NodeRepository
): ViewModel() {


    fun addFriend(myName : String, userName : String, navController: NavController) {
        nodeRepository.addFriend(myName, userName).enqueue(object : Callback<Res_Message> {
            override fun onFailure(call: Call<Res_Message>, t: Throwable) {

            }

            override fun onResponse(call: Call<Res_Message>, response: Response<Res_Message>) {
                if (response.isSuccessful) {
                    navController.popBackStack()
                }
            }

        })
    }
}