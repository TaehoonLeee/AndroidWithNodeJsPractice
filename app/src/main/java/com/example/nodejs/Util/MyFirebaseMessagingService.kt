package com.example.nodejs.Util

import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
//        super.onMessageReceived(p0)
        val data = Data.Builder()
            .putString("sender", p0.data["sender"])
            .putString("message", p0.data["message"])
            .putString("roomName", p0.data["roomName"])
            .putString("userName", p0.data["userName"])
            .build()

        val workRequest = OneTimeWorkRequestBuilder<NotificationWork>()
            .setInputData(data)
            .build()

        val workManager = WorkManager.getInstance(applicationContext)
        workManager.enqueue(workRequest)
    }
    override fun onNewToken(p0: String) {
        Log.e(TAG, "Refreshed Token : $p0")
    }

    companion object {
        private val TAG = "MyFirebaseMessagingService"
    }
}