package com.example.nodejs.Repository

import com.example.nodejs.Model.NotificationBody
import com.example.nodejs.Model.Res_Message
import com.example.nodejs.Network.FcmService
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FcmRepository @Inject constructor(
    private val fcmService: FcmService
){
    fun sendNotification(sender : String, message : String, roomName : String) : Call<Res_Message> {
        return fcmService.sendNotification(sender, message, roomName)
    }
}