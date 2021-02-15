package com.example.nodejs.UI.addchatroom

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.nodejs.Model.Res_Message
import com.example.nodejs.Repository.NodeRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateChatRoomViewModel @ViewModelInject constructor(
        private val nodeRepository: NodeRepository,
        @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val userName = savedStateHandle.get<String>("userName")
    fun createRoom(roomName : String, navController: NavController) {
        nodeRepository.createRoom(userName!!, roomName).enqueue(object : Callback<Res_Message> {
            override fun onFailure(call: Call<Res_Message>, t: Throwable) {

            }

            override fun onResponse(call: Call<Res_Message>, response: Response<Res_Message>) {
                if (response.isSuccessful){
                    val direction =
                            CreateChatRoomFragmentDirections.actionCreateChatRoomFragmentToChatFragment(userName, roomName)
                    navController.navigate(direction)
                }
            }
        })
    }
}