package com.example.nodejs.Network

import com.example.nodejs.Model.Res_Message
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FcmService {

    @GET("/fcmsend")
    fun sendNotification(
        @Query("sender") sender : String,
        @Query("message") message : String,
        @Query("roomName") roomName : String
    ) : Call<Res_Message>
}