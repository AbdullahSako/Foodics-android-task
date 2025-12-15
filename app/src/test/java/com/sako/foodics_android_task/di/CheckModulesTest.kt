package com.sako.foodics_android_task.di

import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.verify.verify

class CheckModulesTest: KoinTest {
    @Test
    fun checkAllModules() {
        appModule.verify()
        networkModule.verify()
        dataModule.verify()
    }
}