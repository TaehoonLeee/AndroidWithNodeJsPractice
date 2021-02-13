package com.example.nodejs.Network

import com.example.nodejs.Model.*
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.*

interface NodeService {

    @GET("create")
    fun getData() : Single<Users>

    @FormUrlEncoded
    @POST("create")
    fun setData(@Field("id") id : String,
                @Field("password") password : String) : Call<User>

    @GET("messages/{name}")
    fun getMessages(@Path("name") name : String) : Single<Messages>

    @FormUrlEncoded
    @POST("messages")
    fun addMessage(@Field("sender") sender : String,
                   @Field("message") message : String,
                   @Field("timeStamp") timeStamp : String) : Call<Res_Message>
}