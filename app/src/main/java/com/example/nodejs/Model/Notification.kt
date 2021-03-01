package com.example.nodejs.Model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NotificationBody(
    val to : String,
    val data : NotificationData
)

@JsonClass(generateAdapter = true)
data class NotificationData(
    val sender : String,
    val message : String,
    val roomName : String
)