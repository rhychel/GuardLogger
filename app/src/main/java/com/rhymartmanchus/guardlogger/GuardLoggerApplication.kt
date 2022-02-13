package com.rhymartmanchus.guardlogger

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GuardLoggerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

    }

}