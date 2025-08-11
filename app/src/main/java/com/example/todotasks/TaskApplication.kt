package com.example.todotasks

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TaskApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize any libraries or components here if needed
        Log.d("App", "TaskApplication onCreate called")
    }
}