package com.sako.foodics_android_task.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sako.foodics_android_task.data.model.local.LocalProduct

@Dao
interface ProductDao {

    @Query("Select * from LocalProduct")
    fun getAllProducts(): List<LocalProduct>

    @Upsert
    fun upsertProductList(productList: List<LocalProduct>)

}