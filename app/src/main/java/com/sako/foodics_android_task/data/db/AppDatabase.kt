package com.sako.foodics_android_task.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sako.foodics_android_task.data.db.dao.CartDao
import com.sako.foodics_android_task.data.db.dao.CategoryDao
import com.sako.foodics_android_task.data.db.dao.ProductDao
import com.sako.foodics_android_task.data.model.local.LocalCartItem
import com.sako.foodics_android_task.data.model.local.LocalCategory
import com.sako.foodics_android_task.data.model.local.LocalProduct

@Database(entities = [LocalCategory::class, LocalProduct::class, LocalCartItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao

    abstract fun cartDao(): CartDao
}


fun createDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(
        context,
        AppDatabase::class.java, "FoodicsAndroidTaskDB"
    ).build()
}
