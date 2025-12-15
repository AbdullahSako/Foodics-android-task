package com.sako.foodics_android_task.data.repository.categoryRepository

import com.sako.foodics_android_task.data.model.external.Category
import com.sako.foodics_android_task.utils.resultWrapper.Result
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun loadCategoriesList(): Flow<Result<List<Category>>>

}