package com.sako.foodics_android_task.data.model.external


import com.sako.foodics_android_task.data.model.network.NetworkCategory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Product(
    val category: Category?,
    val description: String?,
    val id: String?,
    val image: String?,
    val name: String?,
    val price: Double?
)