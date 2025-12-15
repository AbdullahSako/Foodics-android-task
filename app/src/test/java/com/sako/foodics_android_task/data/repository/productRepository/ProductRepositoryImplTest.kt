package com.sako.foodics_android_task.data.repository.productRepository

import app.cash.turbine.test
import com.sako.foodics_android_task.utils.resultWrapper.NetworkError
import com.sako.foodics_android_task.utils.resultWrapper.Result
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.network.UnresolvedAddressException
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.junit.Test


class ProductRepositoryImplTest {
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
    fun loadProductList_emits_Loading_then_Success() = runTest {
        val client = mockHttpClient(
            status = HttpStatusCode.OK,
            responseBody = """
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
        """
        )

        val repo = ProductRepositoryImpl(client)

        repo.loadProductList().test {
            assert(awaitItem() is Result.Loading)

            val success = awaitItem() as Result.Success
            assertEquals(1, success.data?.size ?: 0)

            awaitComplete()
        }
    }

    @Test
    fun loadProductList_emits_Loading_then_Error_when_no_internet()= runTest {
        val client = mockHttpClient(status = HttpStatusCode.OK, exception = UnresolvedAddressException())
        val repo = ProductRepositoryImpl(client)

        repo.loadProductList().test {
            assert(awaitItem() is Result.Loading)

            val error = awaitItem() as Result.Error
            assertEquals(error.errorType , NetworkError.NO_INTERNET)

            awaitComplete()
        }

    }

    @Test
    fun loadProductList_emits_serialization_error() = runTest{
        val client = mockHttpClient(status = HttpStatusCode.OK, exception = SerializationException())
        val repo = ProductRepositoryImpl(client)

        repo.loadProductList().test {
            assert(awaitItem() is Result.Loading)

            val error = awaitItem() as Result.Error
            assertEquals(error.errorType , NetworkError.SERIALIZATION)

            awaitComplete()
        }
    }

    @Test
    fun loadProductList_emits_unauthorized_error() = runTest{
        val client = mockHttpClient(status = HttpStatusCode.Unauthorized)
        val repo = ProductRepositoryImpl(client)

        repo.loadProductList().test {
            assert(awaitItem() is Result.Loading)

            val error = awaitItem() as Result.Error
            assertEquals(error.errorType , NetworkError.UNAUTHORIZED)

            awaitComplete()
        }
    }

    @Test
    fun loadProductList_emits_conflict_error() = runTest{
        val client = mockHttpClient(status = HttpStatusCode.Conflict)
        val repo = ProductRepositoryImpl(client)

        repo.loadProductList().test {
            assert(awaitItem() is Result.Loading)

            val error = awaitItem() as Result.Error
            assertEquals(error.errorType , NetworkError.CONFLICT)

            awaitComplete()
        }
    }

    @Test
    fun loadProductList_emits_timeout_error() = runTest{
        val client = mockHttpClient(status = HttpStatusCode.RequestTimeout)
        val repo = ProductRepositoryImpl(client)

        repo.loadProductList().test {
            assert(awaitItem() is Result.Loading)

            val error = awaitItem() as Result.Error
            assertEquals(error.errorType , NetworkError.REQUEST_TIMEOUT)

            awaitComplete()
        }
    }

    @Test
    fun loadProductList_emits_payload_too_large_error() = runTest{
        val client = mockHttpClient(status = HttpStatusCode.PayloadTooLarge)
        val repo = ProductRepositoryImpl(client)

        repo.loadProductList().test {
            assert(awaitItem() is Result.Loading)

            val error = awaitItem() as Result.Error
            assertEquals(error.errorType , NetworkError.PAYLOAD_TOO_LARGE)

            awaitComplete()
        }
    }

    @Test
    fun loadProductList_emits_server_error() = runTest{
        val client = mockHttpClient(status = HttpStatusCode.InternalServerError)
        val repo = ProductRepositoryImpl(client)

        repo.loadProductList().test {
            assert(awaitItem() is Result.Loading)

            val error = awaitItem() as Result.Error
            assertEquals(error.errorType , NetworkError.SERVER_ERROR)

            awaitComplete()
        }
    }

    @Test
    fun loadProductList_emits_unknown_error() = runTest{
        val client = mockHttpClient(status = HttpStatusCode.Locked)
        val repo = ProductRepositoryImpl(client)

        repo.loadProductList().test {
            assert(awaitItem() is Result.Loading)

            val error = awaitItem() as Result.Error
            assertEquals(error.errorType , NetworkError.UNKNOWN)

            awaitComplete()
        }
    }


}
