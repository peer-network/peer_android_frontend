package eu.peernetwork.user.remote.api

import eu.peernetwork.core.remote.exception.NetworkException
import eu.peernetwork.user.data.api.ResourceApi
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

internal class ResourceApiDelegateTest {
    private val url = "http://localhost"

    private val call = mockk<Call>()

    private val response = mockk<Response>()

    private val responseBody = mockk<ResponseBody>()

    private val client = mockk<OkHttpClient>()

    private lateinit var api: ResourceApi

    @Before
    fun setup() {
        api = ResourceApiDelegate(url, client)
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
