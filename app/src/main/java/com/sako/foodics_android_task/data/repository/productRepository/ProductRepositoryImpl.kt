package com.sako.foodics_android_task.data.repository.productRepository

import com.sako.foodics_android_task.BuildConfig
import com.sako.foodics_android_task.data.Constants
import com.sako.foodics_android_task.data.model.external.Category
import com.sako.foodics_android_task.data.model.external.Product
import com.sako.foodics_android_task.data.model.network.NetworkProduct
import com.sako.foodics_android_task.data.model.network.toExternal
import com.sako.foodics_android_task.utils.resultWrapper.NetworkError
import com.sako.foodics_android_task.utils.resultWrapper.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerializationException
import org.koin.core.annotation.InjectedParam

class ProductRepositoryImpl(@InjectedParam private val httpClient: HttpClient) : ProductRepository {

    override fun loadProductList(): Flow<Result<List<Product>>> = flow {
        emit(Result.Loading())

        val response = try {
            httpClient.get(
                urlString = "${Constants.BASE_URL}/products"
            ) {
                parameter("key", BuildConfig.mockaroo_api_key)
            }
        } catch (e: UnresolvedAddressException) {
            emit(Result.Error(errorType = NetworkError.NO_INTERNET))
            return@flow
        } catch (e: SerializationException) {
            emit(Result.Error(errorType = NetworkError.SERIALIZATION))
            return@flow
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(errorType = NetworkError.UNKNOWN))
            return@flow
        }

        val result = when (response.status.value) {
            in 200..299 -> {
                val productList = response.body<List<NetworkProduct>>()
                Result.Success(data = productList.map { it.toExternal() })
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

    override fun filterProductList(
        query: String?,
        category: Category?
    ): Flow<List<Product>> {
        TODO("Not yet implemented")
    }


}