package com.sako.foodics_android_task.data.repository.categoryRepository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.sako.foodics_android_task.data.db.AppDatabase
import com.sako.foodics_android_task.data.model.local.LocalCategory
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
import org.junit.runners.JUnit4
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class CategoryRepositoryImplTest {
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
    fun refreshCategories_insertsIntoDB_emits_success()= runTest {
        val json = """
            [
              {
                "id": 1,
                "name": "Fruits"
                },
                { "id": 2,
                 "name": "Pizza"
                }
            ]
        """.trimIndent()

        val client = mockHttpClient(status = HttpStatusCode.OK, responseBody = json)
        val repo = CategoryRepositoryImpl(client,db)

        repo.refreshCategories().test {
            assert(awaitItem() is RefreshResult.Success)
            awaitComplete()
        }

        //check db
        db.categoryDao().getAllCategories().test {
            val itemList = awaitItem()
            assertEquals(2, itemList.size)
            assertEquals("Fruits", itemList[0].name)
            cancel()
        }

    }



    @Test
    fun refreshCategories_emits_Error_when_no_internet()= runTest {
        val client = mockHttpClient(status = HttpStatusCode.OK, exception = io.ktor.util.network.UnresolvedAddressException())
        val repo = CategoryRepositoryImpl(client,db)

        repo.refreshCategories().test {
            val error = awaitItem() as RefreshResult.Error
            assertEquals(error.errorType , NetworkError.NO_INTERNET)
            awaitComplete()
        }

    }

    @Test
    fun refreshCategories_emits_serialization_error() = runTest{
        val client = mockHttpClient(status = HttpStatusCode.OK, exception = SerializationException())
        val repo = CategoryRepositoryImpl(client,db)

        repo.refreshCategories().test {
            val error = awaitItem() as RefreshResult.Error
            assertEquals(error.errorType , NetworkError.SERIALIZATION)
            awaitComplete()
        }
    }

    @Test
    fun refreshCategories_emits_unauthorized_error() = runTest{
        val client = mockHttpClient(status = HttpStatusCode.Unauthorized)
        val repo = CategoryRepositoryImpl(client,db)

        repo.refreshCategories().test {
            val error = awaitItem() as RefreshResult.Error
            assertEquals(error.errorType , NetworkError.UNAUTHORIZED)
            awaitComplete()
        }
    }

    @Test
    fun refreshCategories_emits_conflict_error() = runTest{
        val client = mockHttpClient(status = HttpStatusCode.Conflict)
        val repo = CategoryRepositoryImpl(client,db)

        repo.refreshCategories().test {

            val error = awaitItem() as RefreshResult.Error
            assertEquals(error.errorType , NetworkError.CONFLICT)
            awaitComplete()
        }
    }

    @Test
    fun refreshCategories_emits_timeout_error() = runTest{
        val client = mockHttpClient(status = HttpStatusCode.RequestTimeout)
        val repo = CategoryRepositoryImpl(client,db)

        repo.refreshCategories().test {
            val error = awaitItem() as RefreshResult.Error
            assertEquals(error.errorType , NetworkError.REQUEST_TIMEOUT)
            awaitComplete()
        }
    }

    @Test
    fun refreshCategories_emits_payload_too_large_error() = runTest{
        val client = mockHttpClient(status = HttpStatusCode.PayloadTooLarge)
        val repo = CategoryRepositoryImpl(client,db)

        repo.refreshCategories().test {
            val error = awaitItem() as RefreshResult.Error
            assertEquals(error.errorType , NetworkError.PAYLOAD_TOO_LARGE)
            awaitComplete()
        }
    }

    @Test
    fun refreshCategories_emits_server_error() = runTest{
        val client = mockHttpClient(status = HttpStatusCode.InternalServerError)
        val repo = CategoryRepositoryImpl(client,db)

        repo.refreshCategories().test {
            val error = awaitItem() as RefreshResult.Error
            assertEquals(error.errorType , NetworkError.SERVER_ERROR)
            awaitComplete()
        }
    }

    @Test
    fun refreshCategories_emits_unknown_error() = runTest{
        val client = mockHttpClient(status = HttpStatusCode.Locked)
        val repo = CategoryRepositoryImpl(client,db)

        repo.refreshCategories().test {
            val error = awaitItem() as RefreshResult.Error
            assertEquals(error.errorType , NetworkError.UNKNOWN)
            awaitComplete()
        }
    }


    @Test
    fun loadCategories_returnsMappedExternalModels() = runTest {
        // Arrange
        db.categoryDao().upsertCategoryList(
            listOf(LocalCategory(id = 1, name = "Drinks"))
        )

        val repository = CategoryRepositoryImpl(
            httpClient = mockHttpClient(HttpStatusCode.OK),
            db = db
        )

        repository.loadCategories().test {
            val categories = awaitItem()

            assertEquals(1, categories.size)
            assertEquals("Drinks", categories.first().name)
            cancel()
        }


    }




    @After
    fun tearDown() {
        db.close()
    }

}