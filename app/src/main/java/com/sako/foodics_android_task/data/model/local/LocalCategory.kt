package com.sako.foodics_android_task.data.model.local


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sako.foodics_android_task.data.model.external.Category
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity
data class LocalCategory(
    @PrimaryKey
    val id: Int,
    val name: String?
)

fun LocalCategory.toExternal(): Category {
    return Category(
        id = id,
        name = name
    )
}


