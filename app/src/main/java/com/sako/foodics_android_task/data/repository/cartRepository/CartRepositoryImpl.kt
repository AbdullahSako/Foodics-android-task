package com.sako.foodics_android_task.data.repository.cartRepository

import com.sako.foodics_android_task.data.db.AppDatabase
import com.sako.foodics_android_task.data.model.external.CartItem
import com.sako.foodics_android_task.data.model.external.Product
import com.sako.foodics_android_task.data.model.local.LocalCartItem
import com.sako.foodics_android_task.data.model.local.toExternal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.InjectedParam

class CartRepositoryImpl(
    @InjectedParam val db: AppDatabase
) : CartRepository {
    override suspend fun addToCart(product: Product) {
        val storedCartItem = db.cartDao().getCartItemById(product.id ?: "")

        if (storedCartItem != null) {
            db.cartDao().upsertCartItem(storedCartItem.copy(quantity = storedCartItem.quantity + 1))
        } else {
            db.cartDao().upsertCartItem(
                LocalCartItem(
                    id = product.id ?: "",
                    name = product.name ?: "",
                    price = product.price ?: 0.0,
                    quantity = 1
                )
            )
        }
    }


    override fun getCartItemList(): Flow<List<CartItem>> {
        return db.cartDao().getAllCartItems().map { list -> list.map { it.toExternal() } }
    }

    override fun clearCart() {
        db.cartDao().clearCart()
    }
}