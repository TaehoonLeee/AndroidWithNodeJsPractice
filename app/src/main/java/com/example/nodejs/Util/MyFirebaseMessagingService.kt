package com.example.nodejs.Util

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

    }
    override fun onNewToken(p0: String) {
        Log.e(TAG, "Refreshed Token : $p0")
    }

    companion object {
        private val TAG = "MyFirebaseMessagingService"
    }
}