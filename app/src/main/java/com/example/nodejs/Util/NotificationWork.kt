package com.example.nodejs.Util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.nodejs.MainActivity
import com.example.nodejs.R
import com.example.nodejs.UI.chatroom.ChatRoomFragmentDirections

class NotificationWork(appContext : Context, workerParameters: WorkerParameters) : Worker(appContext, workerParameters) {
    override fun doWork(): Result {
        val sender = inputData.getString("sender")
        val message = inputData.getString("message")
        val roomName = inputData.getString("roomName")
        val userName = inputData.getString("userName")

        createNotification(userName!!, roomName!!, message!!, sender!!)

        return Result.success()
    }

    private fun createNotification(userName : String, roomName : String, message : String, sender : String) {
        val pendingIntent = NavDeepLinkBuilder(applicationContext)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.sec_nav_graph)
            .setDestination(R.id.chatFragment)
            .setArguments(
                ChatRoomFragmentDirections.actionChatRoomFragmentToChatFragment(
                    userName, roomName
                ).arguments
            )
            .createPendingIntent()

        val builder = NotificationCompat.Builder(applicationContext, "default")
            .setSmallIcon(R.drawable.ic_baseline_chat_bubble_outline_24)
            .setContentTitle(sender)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(NotificationChannel("default", "default channel", NotificationManager.IMPORTANCE_DEFAULT))

        NotificationManagerCompat.from(this.applicationContext).notify(1, builder.build())
    }

}