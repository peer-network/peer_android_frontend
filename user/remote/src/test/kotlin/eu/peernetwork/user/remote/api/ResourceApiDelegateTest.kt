package eu.peernetwork.user.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import eu.peernetwork.core.remote.exception.NetworkException
import eu.peernetwork.core.remote.model.Status
import eu.peernetwork.user.data.api.ResourceApi
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import protected.eu.peernetwork.user.remote.DailyfreestatusQuery
import java.util.UUID
import kotlin.test.assertEquals

internal class ResourceApiDelegateTest {
    private val url = "http://localhost"

    private val call = mockk<Call>()

    private val response = mockk<Response>()

    private val responseBody = mockk<ResponseBody>()

    private val client = mockk<OkHttpClient>()

    private val apolloClient = mockk<ApolloClient>()

    private lateinit var api: ResourceApi

    @Before
    fun setup() {
        api = ResourceApiDelegate(url, client, apolloClient)
    }

    @Test
    fun `test user point`(): Unit = runBlocking {
        val name = "<test-name>"
        val user = DailyfreestatusQuery.Dailyfreestatus(
            status = Status.SUCCESS.value,
            ResponseCode = "<test-response-code>",
            affectedRows = listOf(
                DailyfreestatusQuery.AffectedRow(
                    name = name,
                    used = 0,
                    available = 0
                )
            )
        )
        val mockData = mockk<DailyfreestatusQuery.Data>()
        val operation = mockk<Operation<DailyfreestatusQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.dailyfreestatus } returns user
        coEvery { apolloClient.query(any<DailyfreestatusQuery>()).execute() } returns mockResponse

        val result = api.points()

        assertEquals(result.first().type, name)
    }

    @Test
    fun `test get string resource`(): Unit = runBlocking {
        val path = "<test-path>"
        val mockData = "<test-data>"

        every { client.newCall(any()) } returns call
        every { call.execute() } returns response
        every { response.isSuccessful } returns true
        every { response.body } returns responseBody
        every { responseBody.string() } returns mockData

        val result = api.string(path)
        assertEquals(result, mockData)
    }

    @Test
    fun `test get string resource error`(): Unit = runBlocking {
        val path = "<test-path>"
        val error = "<test-error>"

        every { client.newCall(any()) } returns call
        every { call.execute() } returns response
        every { response.isSuccessful } returns false
        every { response.message } returns error

        val result = try {
            api.string(path)
        } catch (error: Throwable) {
            assert(error is NetworkException)
            null
        }
        assertEquals(result, null)
    }
}
