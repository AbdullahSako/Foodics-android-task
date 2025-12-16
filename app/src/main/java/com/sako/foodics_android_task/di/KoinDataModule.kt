package com.sako.foodics_android_task.di

import com.sako.foodics_android_task.data.db.createDatabase
import com.sako.foodics_android_task.data.repository.cartRepository.CartRepository
import com.sako.foodics_android_task.data.repository.cartRepository.CartRepositoryImpl
import com.sako.foodics_android_task.data.repository.categoryRepository.CategoryRepository
import com.sako.foodics_android_task.data.repository.categoryRepository.CategoryRepositoryImpl
import com.sako.foodics_android_task.data.repository.productRepository.ProductRepository
import com.sako.foodics_android_task.data.repository.productRepository.ProductRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    singleOf(::ProductRepositoryImpl) bind ProductRepository::class
    singleOf(::CategoryRepositoryImpl) bind CategoryRepository::class
    singleOf(::CartRepositoryImpl) bind CartRepository::class
    single { createDatabase(androidContext()) }
}