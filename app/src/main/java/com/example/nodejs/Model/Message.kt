package com.example.nodejs.Model

import com.squareup.moshi.JsonClass
import java.sql.Time

@JsonClass(generateAdapter = true)
data class Messages(
        val resultCode : Int,
        val msg : String,
        val messages : List<Message>
)

@JsonClass(generateAdapter = true)
data class Message(
        val sender : String,
        val message : String,
        val timeStamp : String
)

@JsonClass(generateAdapter = true)
data class Res_Message(
        val code : Int,
        val msg : String
)