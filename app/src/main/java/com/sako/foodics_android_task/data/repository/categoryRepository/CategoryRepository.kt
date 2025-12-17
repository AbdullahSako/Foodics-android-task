package com.sako.foodics_android_task.data.repository.categoryRepository

import com.sako.foodics_android_task.data.model.external.Category
import com.sako.foodics_android_task.utils.resultWrapper.RefreshResult
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun refreshCategories(): Flow<RefreshResult>
    suspend fun loadCategories(): Flow<List<Category>>

}


