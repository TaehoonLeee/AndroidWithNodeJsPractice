package com.example.nodejs.Repository

import android.util.Log
import com.example.nodejs.Model.Messages
import com.example.nodejs.Model.User
import com.example.nodejs.Model.Users
import com.example.nodejs.Network.NodeService
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NodeRepository @Inject constructor(
    private val nodeService: NodeService
){
    fun getData() : Single<Users> {
        return nodeService.getData()
            .subscribeOn(Schedulers.io())
    }

    fun setData(id : String, password : String) : Call<User> {
        return nodeService.setData(id, password)
    }

    fun getMessages() : Single<Messages> {
        return nodeService.getMessages()
            .subscribeOn(Schedulers.io())
    }
}