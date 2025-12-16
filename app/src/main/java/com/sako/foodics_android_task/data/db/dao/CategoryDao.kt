package com.sako.foodics_android_task.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.sako.foodics_android_task.data.model.local.LocalCategory
import com.sako.foodics_android_task.data.model.local.LocalCategoryWithProducts
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("Select * from LocalCategory")
    fun getAllCategories(): Flow<List<LocalCategory>>

    @Query("Select * from LocalCategory where id = :id ")
    fun getCategoryById(id: Int): LocalCategory

    @Upsert
    fun upsertCategoryList(categoryList: List<LocalCategory>)

    @Transaction
    @Query("SELECT * FROM localcategory WHERE id = :categoryId")
    suspend fun getCategoryWithProducts(
        categoryId: Long
    ): LocalCategoryWithProducts?


    @Query("DELETE FROM localcategory")
    fun clearAllCategories()
}