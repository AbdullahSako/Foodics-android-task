package com.sako.foodics_android_task.data.repository.categoryRepository

import com.sako.foodics_android_task.BuildConfig
import com.sako.foodics_android_task.data.Constants
import com.sako.foodics_android_task.data.db.AppDatabase
import com.sako.foodics_android_task.data.model.external.Category
import com.sako.foodics_android_task.data.model.local.toExternal
import com.sako.foodics_android_task.data.model.network.NetworkCategory
import com.sako.foodics_android_task.data.model.network.toLocal
import com.sako.foodics_android_task.utils.resultWrapper.NetworkError
import com.sako.foodics_android_task.utils.resultWrapper.RefreshResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import org.koin.core.annotation.InjectedParam

class CategoryRepositoryImpl(
    @InjectedParam val httpClient: HttpClient,
    @InjectedParam val db: AppDatabase
) : CategoryRepository {


    /**
     * Refreshes the stored data in the database using data retrieved from a network data source
     *
     * @return Flow<RefreshResult> Returns the result of the api Call (Success/Error)
     * */
    override suspend fun refreshCategories(): Flow<RefreshResult> = flow {
        val response = try {
            httpClient.get(
                urlString = "${Constants.BASE_URL}/categories"
            ) {
                parameter("key", BuildConfig.mockaroo_api_key)
            }
        } catch (e: UnresolvedAddressException) {
            emit(RefreshResult.Error(errorType = NetworkError.NO_INTERNET))
            return@flow
        } catch (e: SerializationException) {
            emit(RefreshResult.Error(errorType = NetworkError.SERIALIZATION))
            return@flow
        } catch (e: Exception) {
            e.printStackTrace()
            emit(RefreshResult.Error(errorType = NetworkError.UNKNOWN))
            return@flow
        }

        val result = when (response.status.value) {
            in 200..299 -> {
                //write data to DB on success
                val categoryList = response.body<List<NetworkCategory>>()
                db.categoryDao().upsertCategoryList(categoryList.map { it.toLocal() })

                RefreshResult.Success()
            }

            401 -> RefreshResult.Error(errorType = NetworkError.UNAUTHORIZED)
            409 -> RefreshResult.Error(errorType = NetworkError.CONFLICT)
            408 -> RefreshResult.Error(errorType = NetworkError.REQUEST_TIMEOUT)
            413 -> RefreshResult.Error(errorType = NetworkError.PAYLOAD_TOO_LARGE)
            in 500..599 -> RefreshResult.Error(errorType = NetworkError.SERVER_ERROR)
            else -> RefreshResult.Error(errorType = NetworkError.UNKNOWN)
        }
        emit(result)
    }


    /**
     * Retrieves data from the database
     *
     * @return Flow<List<Category>> Returns a flow of a list of categories
     * */
    override suspend fun loadCategories(): Flow<List<Category>> {
        return db.categoryDao().getAllCategories().map { list -> list.map { it.toExternal() } }
    }


}