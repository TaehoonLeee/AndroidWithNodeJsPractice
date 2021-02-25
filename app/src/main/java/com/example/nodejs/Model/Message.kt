package com.example.nodejs.Model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.sql.Time

@JsonClass(generateAdapter = true)
data class Messages(
        val resultCode : Int?,
        val msg : String?,
        val messages : List<Message>
)

@JsonClass(generateAdapter = true)
data class Message(
        val index : Int?,
        val sender : String,
        val message : String,
        val timeStamp : String
)

@JsonClass(generateAdapter = true)
data class Res_Message(
        val code : Int,
        val msg : String
)

@JsonClass(generateAdapter = true)
data class Person(
        val name : String,
        val chatRoomList : List<Int>
)

@JsonClass(generateAdapter = true)
data class ChatList(
    val chatRoomList : List<ChatRoom>
)

@JsonClass(generateAdapter = true)
data class ChatRoom(
        val name : String,
        val memberNumber : Int,
        val topMessage : String?,
        val topTimeStamp : String?,
        val access : String,
        val topIndex : Int
)