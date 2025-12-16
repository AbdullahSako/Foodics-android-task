package com.sako.foodics_android_task.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sako.foodics_android_task.data.model.external.CartItem

@Entity
data class LocalCartItem(
    @PrimaryKey
    val id: String,
    val name: String,
    val price: Double,
    val quantity: Int
)

fun LocalCartItem.toExternal(): CartItem{
    return CartItem(
        id = id,
        name = name,
        price = price,
        quantity = quantity
    )

}