package com.sako.foodics_android_task.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sako.foodics_android_task.data.model.external.Category
import com.sako.foodics_android_task.data.model.external.Product


@Entity
data class LocalProduct(
    val categoryId: Int,
    val description: String?,
    @PrimaryKey
    val id: String,
    val image: String?,
    val name: String?,
    val price: Double?
)
