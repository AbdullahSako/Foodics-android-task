package com.sako.foodics_android_task

import android.app.Application
import com.sako.foodics_android_task.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class FoodicsAndroidTaskApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        setupKoin()
    }



    private fun setupKoin(){
        startKoin {
            androidLogger()
            androidContext(this@FoodicsAndroidTaskApplication)
            modules(appModule)
        }
    }
}