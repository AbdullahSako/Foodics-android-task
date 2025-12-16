package com.sako.foodics_android_task.data.repository.productRepository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.sako.foodics_android_task.data.db.AppDatabase
import com.sako.foodics_android_task.data.model.local.LocalCategory
import com.sako.foodics_android_task.data.model.local.LocalProduct
import com.sako.foodics_android_task.utils.resultWrapper.NetworkError
import com.sako.foodics_android_task.utils.resultWrapper.RefreshResult
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ProductRepositoryImplTest {
    lateinit var db: AppDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    fun mockHttpClient(
        status: HttpStatusCode,
        responseBody: String = "{}",
        exception: Throwable? = null
    ): HttpClient {
        return HttpClient(MockEngine) {
            engine {
                addHandler { _ ->
                    exception?.let { throw it }

                    respond(
                        content = responseBody,
                        status = status,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
            }
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }


    @Test
    fun refreshProductList_insertsIntoDB_emits_success() = runTest {
        val json = """
            [
              {
                "id": "p1",
                "name": "Pancakes",
                "description": "Fluffy pancakes with syrup",
                "image": "pancakes.jpg",
                "price": 5.0,
                "category": { "id": "7", "name": "Bakery" }
                }
            ]
        """.trimIndent()

        val client = mockHttpClient(status = HttpStatusCode.OK, responseBody = json)
        val repo = ProductRepositoryImpl(client, db)

        repo.refreshProductList().test {
            assert(awaitItem() is RefreshResult.Success)
            awaitComplete()
        }

        //check db
        db.productDao().getAllProducts().test {
            val itemList = awaitItem()
            assertEquals(1, itemList.size)
            assertEquals("p1", itemList[0].id)
            cancel()
        }

    }


    @Test
    fun refreshProductList_emits_Error_when_no_internet() = runTest {
        val client = mockHttpClient(
            status = HttpStatusCode.OK,
            exception = io.ktor.util.network.UnresolvedAddressException()
        )
        val repo = ProductRepositoryImpl(client, db)

        repo.refreshProductList().test {
            val error = awaitItem() as RefreshResult.Error
            assertEquals(error.errorType, NetworkError.NO_INTERNET)
            awaitComplete()
        }

    }

    @Test
    fun refreshProductList_emits_serialization_error() = runTest {
        val client =
            mockHttpClient(status = HttpStatusCode.OK, exception = SerializationException())
        val repo = ProductRepositoryImpl(client, db)

        repo.refreshProductList().test {
            val error = awaitItem() as RefreshResult.Error
            assertEquals(error.errorType, NetworkError.SERIALIZATION)
            awaitComplete()
        }
    }

    @Test
    fun refreshProductList_emits_unauthorized_error() = runTest {
        val client = mockHttpClient(status = HttpStatusCode.Unauthorized)
        val repo = ProductRepositoryImpl(client, db)

        repo.refreshProductList().test {
            val error = awaitItem() as RefreshResult.Error
            assertEquals(error.errorType, NetworkError.UNAUTHORIZED)
            awaitComplete()
        }
    }

    @Test
    fun refreshProductList_emits_conflict_error() = runTest {
        val client = mockHttpClient(status = HttpStatusCode.Conflict)
        val repo = ProductRepositoryImpl(client, db)

        repo.refreshProductList().test {

            val error = awaitItem() as RefreshResult.Error
            assertEquals(error.errorType, NetworkError.CONFLICT)
            awaitComplete()
        }
    }

    @Test
    fun refreshProductList_emits_timeout_error() = runTest {
        val client = mockHttpClient(status = HttpStatusCode.RequestTimeout)
        val repo = ProductRepositoryImpl(client, db)

        repo.refreshProductList().test {
            val error = awaitItem() as RefreshResult.Error
            assertEquals(error.errorType, NetworkError.REQUEST_TIMEOUT)
            awaitComplete()
        }
    }

    @Test
    fun refreshProductList_emits_payload_too_large_error() = runTest {
        val client = mockHttpClient(status = HttpStatusCode.PayloadTooLarge)
        val repo = ProductRepositoryImpl(client, db)

        repo.refreshProductList().test {
            val error = awaitItem() as RefreshResult.Error
            assertEquals(error.errorType, NetworkError.PAYLOAD_TOO_LARGE)
            awaitComplete()
        }
    }

    @Test
    fun refreshProductList_emits_server_error() = runTest {
        val client = mockHttpClient(status = HttpStatusCode.InternalServerError)
        val repo = ProductRepositoryImpl(client, db)

        repo.refreshProductList().test {
            val error = awaitItem() as RefreshResult.Error
            assertEquals(error.errorType, NetworkError.SERVER_ERROR)
            awaitComplete()
        }
    }

    @Test
    fun refreshProductList_emits_unknown_error() = runTest {
        val client = mockHttpClient(status = HttpStatusCode.Locked)
        val repo = ProductRepositoryImpl(client, db)

        repo.refreshProductList().test {
            val error = awaitItem() as RefreshResult.Error
            assertEquals(error.errorType, NetworkError.UNKNOWN)
            awaitComplete()
        }
    }


    @Test
    fun loadProducts_returnsMappedExternalModels() = runTest {
        // Arrange
        db.categoryDao().upsertCategoryList(
            listOf(LocalCategory(id = 7, name = "Breakfast"))
        )

        db.productDao().upsertProductList(
            listOf(
                LocalProduct(
                    id = "p1",
                    name = "Pancakes",
                    description = "Fluffy pancakes with syrup",
                    image = "pancakes.jpg",
                    price = 5.0,
                    categoryId = 7
                )
            )
        )

        val repository = ProductRepositoryImpl(
            httpClient = mockHttpClient(HttpStatusCode.OK),
            db = db
        )

        repository.loadProductList().test {
            val products = awaitItem()

            assertEquals(1, products.size)
            assertEquals("Pancakes", products.first().name)
            cancel()
        }


    }


    @After
    fun tearDown() {
        db.close()
    }


}
