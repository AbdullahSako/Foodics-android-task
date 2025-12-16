package com.sako.foodics_android_task.data.repository.productRepository

import com.sako.foodics_android_task.data.model.external.Category
import com.sako.foodics_android_task.data.model.external.Product
import kotlinx.coroutines.flow.Flow
import com.sako.foodics_android_task.utils.resultWrapper.RefreshResult

interface ProductRepository {

    fun refreshProductList(): Flow<RefreshResult>

    fun loadProductList(): Flow<List<Product>>

    fun filterProductList(query: String? = null, category: Category? = null): Flow<List<Product>>

}