package com.getstream

import android.app.Application
import timber.log.Timber

class GetStreamPerusalApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}