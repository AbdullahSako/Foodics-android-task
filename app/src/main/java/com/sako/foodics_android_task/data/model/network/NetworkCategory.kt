package com.sako.foodics_android_task.data.model.network


import com.sako.foodics_android_task.data.model.external.Category
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkCategory(
    @SerialName("id")
    val id: Int?,
    @SerialName("name")
    val name: String?
)

fun NetworkCategory.toExternal(): Category{
    return Category(id = this.id, name = this.name)
}

