package com.sako.foodics_android_task.data.repository.productRepository

import androidx.room.withTransaction
import com.sako.foodics_android_task.BuildConfig
import com.sako.foodics_android_task.data.Constants
import com.sako.foodics_android_task.data.db.AppDatabase
import com.sako.foodics_android_task.data.model.external.Category
import com.sako.foodics_android_task.data.model.external.Product
import com.sako.foodics_android_task.data.model.local.toExternalProduct
import com.sako.foodics_android_task.data.model.local.toExternalProductList
import com.sako.foodics_android_task.data.model.network.NetworkProduct
import com.sako.foodics_android_task.data.model.network.toLocal
import com.sako.foodics_android_task.utils.resultWrapper.NetworkError
import com.sako.foodics_android_task.utils.resultWrapper.RefreshResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import org.koin.core.annotation.InjectedParam

class ProductRepositoryImpl(
    @InjectedParam private val httpClient: HttpClient,
    @InjectedParam private val db: AppDatabase
) : ProductRepository {
    private val _filterQuery = MutableStateFlow<Pair<String?, Category?>>(null to null)

    /**
     * Refreshes the stored data in the database using data retrieved from a network data source
     *
     * @return Flow<RefreshResult> Returns the result of the api Call (Success/Error)
     * */
    override fun refreshProductList(): Flow<RefreshResult> = flow {

        val response = try {
            httpClient.get(
                urlString = "${Constants.BASE_URL}/products"
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

                //write product list to DB on success
                val productList = response.body<List<NetworkProduct>>()

                db.withTransaction {
                    //clear all previous data
                    db.productDao().clearAllProducts()
                    db.productDao().upsertProductList(productList.map { it.toLocal() })
                }

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
     * Retrieves data from the database and filters the data if any filter options are selected
     *
     * @return Flow<List<Product>> Returns a flow of a list of products
     * */
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun loadProductList(): Flow<List<Product>> {
        return _filterQuery.flatMapLatest { (query, category) ->
            db.productDao().getAllProductsWithCategoryName()
                .map { list ->
                    list.map { it.toExternalProduct() }.filter {product ->
                        val matchesQuery = query.isNullOrEmpty() || product.name?.contains(query, ignoreCase = true) == true
                        val matchesCategory = category == null || product.category?.id == category.id
                        matchesQuery && matchesCategory
                    }
                }
        }
    }


    override fun setFilterOptions(
        query: String?,
        category: Category?
    ) {
        _filterQuery.value = query to category

    }


}