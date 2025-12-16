package com.sako.foodics_android_task.di

import com.sako.foodics_android_task.ui.MainActivityViewModel
import com.sako.foodics_android_task.ui.screens.tables.TablesScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::TablesScreenViewModel)
    viewModelOf(::MainActivityViewModel)
}