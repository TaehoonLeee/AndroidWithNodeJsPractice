package com.example.nodejs.UI.profile

import android.util.Log
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

    fun onClickChat(userName : String, roomName : String, navController: NavController) {
        nodeRepository.createRoom(userName, roomName, "private").enqueue(object : Callback<Res_Message> {
            override fun onFailure(call: Call<Res_Message>, t: Throwable) {
                Log.e("onClickChat", t.message!!)
            }

            override fun onResponse(call: Call<Res_Message>, response: Response<Res_Message>) {
                if (response.isSuccessful) {
                    nodeRepository.enterRoom(userName, roomName).enqueue(object : Callback<Res_Message> {
                        override fun onFailure(call: Call<Res_Message>, t: Throwable) {
                            Log.e("onclickchat", t.message!!)
                        }

                        override fun onResponse(
                            call: Call<Res_Message>,
                            response: Response<Res_Message>
                        ) {
                            Log.e("onclickchat onresponse", response.message())
                            if(response.isSuccessful) {
                                val direction =
                                    ProfileFragmentDirections.actionProfileFragmentToChatFragment(
                                        userName,
                                        roomName
                                    )
                                navController.navigate(direction)
                            }
                        }
                    })
                }
            }
        })
    }
}