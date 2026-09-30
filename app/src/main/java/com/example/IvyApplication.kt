package com.example

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

class IvyApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    initializeFirebaseSafely()
  }

  private fun initializeFirebaseSafely() {
    try {
      if (FirebaseApp.getApps(this).isEmpty()) {
        val options = FirebaseOptions.Builder()
          .setApplicationId("1:1033674134835:android:4b8d70d811bffd67d85a46")
          .setApiKey("AIzaSyDdQnsHh4o6tHa557tu_yQIcVk46GWXVzE")
          .setProjectId("ivy-connect")
          .setStorageBucket("ivy-connect.firebasestorage.app")
          .setGcmSenderId("1033674134835")
          .build()

        FirebaseApp.initializeApp(this, options)
        Log.i(TAG, "FirebaseApp initialized with explicit fallback options")
      } else {
        Log.i(TAG, "FirebaseApp auto-initialized successfully by Google Services")
      }
    } catch (e: Exception) {
      Log.e(TAG, "FirebaseApp initialization encountered an exception: ${e.message}", e)
    }
  }

  companion object {
    private const val TAG = "IvyApplication"
  }
}
