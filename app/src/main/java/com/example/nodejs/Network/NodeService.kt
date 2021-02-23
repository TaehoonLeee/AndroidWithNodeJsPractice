package com.example.nodejs.Network

import com.example.nodejs.Model.*
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.*

interface NodeService {

    @GET("chat/{name}")
    fun getMessages(@Path("name") name : String) : Single<Messages>

    @FormUrlEncoded
    @POST("chat")
    fun addMessage(@Field("sender") sender : String,
                   @Field("message") message : String,
                   @Field("timeStamp") timeStamp : String,
                    @Field("roomName") roomName : String) : Call<Res_Message>

    @GET("/chatList/{name}")
    fun getChatList(@Path("name") name : String) : Single<ChatList>

    @GET("/chatList")
    fun getAllChatList(@Query("name") name : String) : Single<ChatList>

    @FormUrlEncoded
    @POST("/enter")
    fun enterRoom(
        @Field("userName") userName : String,
        @Field("roomName") roomName : String) : Call<Res_Message>

    @FormUrlEncoded
    @POST("/createroom")
    fun createRoom(
            @Field("userName") userName : String,
            @Field("roomName") roomName : String) : Call<Res_Message>

    @GET("/chatmember/{roomName}")
    fun getChatMembers(@Path("roomName") roomName : String) : Single<Members>

    @FormUrlEncoded
    @POST("/createmember")
    fun createMember(@Field("userName") userName : String) : Call<Res_Message>

    @GET("/friends/{userName}")
    fun getFriends(@Path("userName") userName : String) : Single<Friends>
}