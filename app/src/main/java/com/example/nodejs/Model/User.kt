package com.example.nodejs.Model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Members(
    val members : List<Member>
)

@JsonClass(generateAdapter = true)
data class Member(
    val name : String
)

@JsonClass(generateAdapter = true)
data class Friends(
        val friends : List<Friend>
)
@JsonClass(generateAdapter = true)
data class Friend(
        val name : String
)