package com.sako.foodics_android_task.data.repository.categoryRepository

import com.sako.foodics_android_task.BuildConfig
import com.sako.foodics_android_task.data.Constants
import com.sako.foodics_android_task.data.model.external.Category
import com.sako.foodics_android_task.data.model.network.NetworkCategory
import com.sako.foodics_android_task.data.model.network.toExternal
import com.sako.foodics_android_task.utils.resultWrapper.Result
import com.sako.foodics_android_task.utils.ext.logd
import com.sako.foodics_android_task.utils.resultWrapper.NetworkError
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
import org.koin.core.annotation.InjectedParam

class CategoryRepositoryImpl(@InjectedParam val httpClient: HttpClient) : CategoryRepository {

    override suspend fun loadCategoriesList(): Flow<Result<List<Category>>> = flow {
        emit(Result.Loading())

        val response = try {
            httpClient.get(
                urlString = "${Constants.BASE_URL}/categories"
            ) {
                parameter("key", BuildConfig.mockaroo_api_key)
            }
        } catch (e: UnresolvedAddressException) {
            emit(Result.Error(errorType = NetworkError.NO_INTERNET))
            return@flow
        } catch (e: SerializationException) {
            emit(Result.Error(errorType = NetworkError.SERIALIZATION))
            return@flow
        }

        val result = when (response.status.value) {
            in 200..299 -> {
                val categoryList = response.body<List<NetworkCategory>>()
                Result.Success(data = categoryList.map { it.toExternal() })
            }
            401 -> Result.Error(errorType = NetworkError.UNAUTHORIZED)
            409 -> Result.Error(errorType = NetworkError.CONFLICT)
            408 -> Result.Error(errorType = NetworkError.REQUEST_TIMEOUT)
            413 -> Result.Error(errorType = NetworkError.PAYLOAD_TOO_LARGE)
            in 500..599 -> Result.Error(errorType = NetworkError.SERVER_ERROR)
            else -> Result.Error(errorType = NetworkError.UNKNOWN)
        }
        emit(result)
    }


}