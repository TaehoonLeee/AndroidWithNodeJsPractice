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