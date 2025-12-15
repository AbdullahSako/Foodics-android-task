package com.sako.foodics_android_task.data.repository.categoryRepository

import com.sako.foodics_android_task.BuildConfig
import com.sako.foodics_android_task.data.Constants
import com.sako.foodics_android_task.data.model.external.Category
import com.sako.foodics_android_task.data.model.network.NetworkCategory
import com.sako.foodics_android_task.data.model.network.toExternal
import com.sako.foodics_android_task.utils.Result
import com.sako.foodics_android_task.utils.ext.logd
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException

class CategoryRepositoryImpl(val httpClient: HttpClient) : CategoryRepository {

    override suspend fun loadCategoriesList(): Flow<Result<List<Category>>> = flow {
        emit(Result.Loading())

        val response = try {
            httpClient.get(
                urlString = "${Constants.BASE_URL}/categories"
            ) {
                parameter("key", BuildConfig.mockaroo_api_key)
            }
        } catch (e: UnresolvedAddressException) {
            emit(Result.Error(message = "No internet connection"))
            return@flow
        } catch (e: SerializationException) {
            emit(Result.Error(message = "serialization error"))
            return@flow
        }

        val result = when (response.status.value) {
            in 200..299 -> {
                val categoryList = response.body<List<NetworkCategory>>()
                Result.Success(data = categoryList.map { it.toExternal() })
            }
            401 -> Result.Error("not authorized")
            409 -> Result.Error("conflict")
            408 -> Result.Error("request timed-out")
            413 -> Result.Error("payload too large")
            in 500..599 -> Result.Error("server error")
            else -> Result.Error("unknown error")
        }
        emit(result)
    }


}