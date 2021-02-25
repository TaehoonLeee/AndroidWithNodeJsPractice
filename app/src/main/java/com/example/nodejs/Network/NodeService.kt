package com.example.nodejs.Network

import com.example.nodejs.Model.*
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.*

interface NodeService {

    @GET("chat/{roomName}")
    fun getMessages(@Path("roomName") roomName : String,
                    @Query("userName") userName : String) : Single<Messages>

    @FormUrlEncoded
    @POST("chat")
    fun addMessage(@Field("sender") sender : String,
                   @Field("message") message : String,
                   @Field("timeStamp") timeStamp : String,
                    @Field("roomName") roomName : String) : Call<Res_Message>

    @GET("/tmpchatList/{name}")
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
            @Field("roomName") roomName : String,
            @Field("access") access : String) : Call<Res_Message>

    @GET("/chatmember/{roomName}")
    fun getChatMembers(@Path("roomName") roomName : String) : Single<Members>

    @FormUrlEncoded
    @POST("/createmember")
    fun createMember(@Field("userName") userName : String) : Call<Res_Message>

    @GET("/friends/{userName}")
    fun getFriends(@Path("userName") userName : String) : Single<Friends>

    @GET("/friendcandidate")
    fun getFriendCandidate(
        @Query("regex") regex : String,
        @Query("userName") userName : String) : Single<Friends>

    @FormUrlEncoded
    @POST("/friendadd")
    fun addFriend(
        @Field("myName") myName : String,
        @Field("userName") userName : String) : Call<Res_Message>
}