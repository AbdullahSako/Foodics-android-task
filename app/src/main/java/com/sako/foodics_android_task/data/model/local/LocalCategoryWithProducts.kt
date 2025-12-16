package com.sako.foodics_android_task.data.model.local

import androidx.room.Embedded
import androidx.room.Relation
import com.sako.foodics_android_task.data.model.external.Product

data class LocalCategoryWithProducts(
    @Embedded val category: LocalCategory,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val products: List<LocalProduct>
)

fun LocalCategoryWithProducts.toExternalProductList(): List<Product> {
    return products.map {
        Product(category.toExternal(), it.description, it.id, it.image, it.name, it.price)
    }
}
