package com.chongieball.salttest

import android.app.Application
import com.chongieball.salttest.di.appModule
import com.chongieball.salttest.di.networkModule
import com.chongieball.salttest.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)

            modules(listOf(appModule, networkModule, repositoryModule))
        }
    }
}