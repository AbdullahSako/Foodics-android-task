package com.sako.foodics_android_task.data.model.network


import com.sako.foodics_android_task.data.model.external.Product
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkProduct(
    @SerialName("category")
    val category: NetworkCategory?,
    @SerialName("description")
    val description: String?,
    @SerialName("id")
    val id: String?,
    @SerialName("image")
    val image: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("price")
    val price: Double?
)

fun NetworkProduct.toExternal() : Product {
    return Product(
        category = category?.toExternal(),
        description = description,
        id = id,
        image = image,
        name = name,
        price = price
    )
}
