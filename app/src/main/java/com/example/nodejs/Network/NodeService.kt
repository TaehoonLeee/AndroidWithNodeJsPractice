package com.example.nodejs.Network

import com.example.nodejs.Model.User
import com.example.nodejs.Model.Users
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
}