package com.sako.foodics_android_task.di

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.verify.verify

class CheckModulesTest: KoinTest {
    @Test
    fun checkAllModules() {
        appModule.verify()
        networkModule.verify(listOf(HttpClientEngine::class, HttpClientConfig::class)) //https://github.com/InsertKoinIO/koin/issues/2029#issuecomment-2940373255
        dataModule.verify()
    }
}