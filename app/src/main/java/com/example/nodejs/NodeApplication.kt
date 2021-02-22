package com.example.nodejs

import android.app.Application
import android.content.Context
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NodeApplication : Application() {
    companion object {
        lateinit var instance: NodeApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.configure(applicationContext)
            Log.e("NodeApplication", "Init Amplify")
        } catch (e: AmplifyException) {
            Log.e("NodeApplication", "Could not init amplify", e)
        }
    }

    fun context(): Context = applicationContext
}