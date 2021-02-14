package com.example.nodejs.Repository

import android.util.Log
import com.example.nodejs.Model.*
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
    fun getMessages(name : String) : Single<Messages> {
        return nodeService.getMessages(name)
            .subscribeOn(Schedulers.io())
    }

    fun addMessage(sender : String, message : String, timeStamp : String, roomName : String) : Call<Res_Message> {
        return nodeService.addMessage(sender, message, timeStamp, roomName)
    }

    fun getChatList(name : String) : Single<ChatList> {
        return nodeService.getChatList(name)
    }

    fun getAllChatList(name : String) : Single<ChatList> {
        return nodeService.getAllChatList(name)
    }

    fun enterRoom(userName : String, roomName : String) : Call<Res_Message> {
        return nodeService.enterRoom(userName, roomName)
    }
}