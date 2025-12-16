package com.sako.foodics_android_task.data.repository.cartRepository

import com.sako.foodics_android_task.data.model.external.CartItem
import com.sako.foodics_android_task.data.model.external.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    suspend fun addToCart(product: Product)

    fun getCartItemList(): Flow<List<CartItem>>

    fun clearCart()
}