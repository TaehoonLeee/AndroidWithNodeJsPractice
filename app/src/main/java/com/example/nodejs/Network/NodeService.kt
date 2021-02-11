package com.example.nodejs.Network

import com.example.nodejs.Model.Users
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface NodeService {

    @GET("create")
    fun getData() : Single<Users>

}