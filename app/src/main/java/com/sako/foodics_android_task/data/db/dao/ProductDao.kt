package com.sako.foodics_android_task.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sako.foodics_android_task.data.model.local.LocalProduct
import com.sako.foodics_android_task.data.model.local.LocalProductWithCategoryName
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("Select * from LocalProduct")
    fun getAllProducts(): Flow<List<LocalProduct>>

    @Query("SELECT localproduct.*, localcategory.name AS categoryName FROM localproduct "+
    "INNER JOIN localcategory ON localproduct.categoryId = localcategory.id")
    fun getAllProductsWithCategoryName(): Flow<List<LocalProductWithCategoryName>>

    @Upsert
    fun upsertProductList(productList: List<LocalProduct>)

    @Query("DELETE FROM localproduct")
    fun clearAllProducts()

}



