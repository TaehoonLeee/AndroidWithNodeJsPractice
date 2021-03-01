package com.example.nodejs.Network.listener

interface EventListener {

    fun onConnect()

    fun onDisconnect()

    fun onUpdateChat(message : String)
}