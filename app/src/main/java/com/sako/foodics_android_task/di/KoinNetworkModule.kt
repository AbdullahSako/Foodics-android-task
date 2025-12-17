package com.sako.foodics_android_task.di

import com.sako.foodics_android_task.data.network.createNetworkClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val networkModule = module {
    singleOf(::createNetworkClient)
}