package com.sako.foodics_android_task.data.repository.productRepository

import com.sako.foodics_android_task.BuildConfig
import com.sako.foodics_android_task.data.Constants
import com.sako.foodics_android_task.data.model.external.Category
import com.sako.foodics_android_task.data.model.external.Product
import com.sako.foodics_android_task.data.model.network.NetworkCategory
import com.sako.foodics_android_task.data.model.network.NetworkProduct
import com.sako.foodics_android_task.data.model.network.toExternal
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.SerializationException
import com.sako.foodics_android_task.utils.Result
import kotlinx.coroutines.flow.flow

class ProductRepositoryImpl(private val httpClient: HttpClient): ProductRepository {

    override fun loadProductList(): Flow<Result<List<Product>>> = flow {
        emit(Result.Loading())

        val response = try {
            httpClient.get(
                urlString = "${Constants.BASE_URL}/products"
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
                val productList = response.body<List<NetworkProduct>>()
                Result.Success(data = productList.map { it.toExternal() })
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

    override fun filterProductList(
        query: String?,
        category: Category?
    ): Flow<List<Product>> {
        TODO("Not yet implemented")
    }


}