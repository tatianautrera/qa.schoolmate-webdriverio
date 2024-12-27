package com.fsacchi.schoolmate

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.fsacchi.schoolmate.core.di.presentationModules
import com.fsacchi.schoolmate.core.platform.worker.scheduleDailyJob
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

open class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initiateKoin()
        FirebaseApp.initializeApp(this)
        scheduleDailyJob(this)
    }

    private fun initiateKoin() {
        startKoin {
            androidContext(this@App)
            modules(provideDependency())
        }
    }

    internal open fun provideDependency() = presentationModules
}
