package com.sako.foodics_android_task.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sako.foodics_android_task.data.model.local.LocalCartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("Select * from localcartitem")
    fun getAllCartItems(): Flow<List<LocalCartItem>>

    @Query("SELECT * FROM LocalCartItem WHERE id = :id")
    suspend fun getCartItemById(id: String): LocalCartItem?

    @Upsert
    fun upsertCartItem(cartItem: LocalCartItem)

    @Query("DELETE FROM LocalCartItem")
    fun clearCart()

}