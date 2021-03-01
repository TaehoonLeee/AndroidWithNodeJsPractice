package com.example.nodejs.Util

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotiWorkManager(appContext : Context, workerParameters: WorkerParameters) : Worker(appContext, workerParameters) {
    override fun doWork(): Result {
        val sender = inputData.getString("sender")
        val message = inputData.getString("message")
        val roomName = inputData.getString("roomName")

        return Result.success()
    }

    private fun createNotification() {

    }

}