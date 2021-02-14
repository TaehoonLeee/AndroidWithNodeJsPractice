package com.example.nodejs.Model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Users(
    val resultCode : Int,
    val msg : String,
    val users : List<User>
)

@JsonClass(generateAdapter = true)
data class User(
    val id : String,
    val password : String
)