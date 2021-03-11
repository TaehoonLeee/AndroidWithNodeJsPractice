package com.example.nodejs.Repository

import android.util.Log
import com.example.nodejs.Model.*
import com.example.nodejs.Network.NodeService
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NodeRepository @Inject constructor(
    private val nodeService: NodeService
){
    fun getMessages(userName : String, roomName : String) : Single<Messages> {
        return nodeService.getMessages(roomName, userName)
            .subscribeOn(Schedulers.io())
    }

    fun addMessage(sender : String, message : String, timeStamp : String, roomName : String) : Call<Res_Message> {
        return nodeService.addMessage(sender, message, timeStamp, roomName)
    }

    fun getChatList(name : String) : Single<ChatList> {
        return nodeService.getChatList(name)
            .subscribeOn(Schedulers.io())
    }

    fun getAllChatList(name : String) : Single<ChatList> {
        return nodeService.getAllChatList(name)
            .subscribeOn(Schedulers.io())
    }

    fun enterRoom(userName : String, roomName : String) : Call<Res_Message> {
        return nodeService.enterRoom(userName, roomName)
    }

    fun createRoom(userName : String, roomName : String, access : String) : Call<Res_Message> {
        return nodeService.createRoom(userName, roomName, access)
    }

    fun getChatMemebers(roomName : String) : Single<Members> {
        return nodeService.getChatMembers(roomName)
            .subscribeOn(Schedulers.io())
    }

    fun createMember(userName : String) : Call<Res_Message> {
        return nodeService.createMember(userName)
    }

    fun getFriends(userName : String) : Single<Friends> {
        return nodeService.getFriends(userName)
            .subscribeOn(Schedulers.io())
    }

    fun getFriendCandidate(regex : String, userName : String) : Single<Friends> {
        return nodeService.getFriendCandidate(regex, userName)
            .subscribeOn(Schedulers.io())
    }

    fun addFriend(myName : String, userName : String) : Call<Res_Message> {
        return nodeService.addFriend(myName, userName)
    }

    fun exitRoom(userName: String, roomName: String) : Call<Res_Message> {
        return nodeService.exitRoom(userName, roomName)
    }

    fun uploadImage(
            image : MultipartBody.Part,
            name : RequestBody,
            sender : RequestBody,
            roomName: RequestBody,
            timeStamp: RequestBody
    ) : Call<Res_Message> {
        return nodeService.uploadImage(image, name, sender, roomName, timeStamp)
    }
}