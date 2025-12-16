package com.sako.foodics_android_task.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sako.foodics_android_task.data.db.dao.CategoryDao
import com.sako.foodics_android_task.data.db.dao.ProductDao
import com.sako.foodics_android_task.data.model.local.LocalCategory
import com.sako.foodics_android_task.data.model.local.LocalProduct

@Database(entities = [LocalCategory::class, LocalProduct::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
}


fun createDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(
        context,
        AppDatabase::class.java, "FoodicsAndroidTaskDB"
    ).build()
}
