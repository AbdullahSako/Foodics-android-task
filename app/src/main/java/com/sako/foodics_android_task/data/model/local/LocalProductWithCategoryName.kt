package com.sako.foodics_android_task.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.sako.foodics_android_task.data.model.external.Category
import com.sako.foodics_android_task.data.model.external.Product

data class LocalProductWithCategoryName(
    @Embedded
    val product: LocalProduct,
    @ColumnInfo(name = "categoryName")
    val categoryName: String
)

fun LocalProductWithCategoryName.toExternalProduct(): Product{
    return Product(
        category = Category(id = product.categoryId, name = categoryName),
        description = product.description,
        id = product.id,
        image = product.image,
        name = product.name,
        price = product.price
    )

}