package eu.peernetwork.blog.remote.api

import android.content.Context
import com.google.gson.Gson
import eu.peernetwork.blog.domain.repository.EligibilityRepository
import eu.peernetwork.blog.remote.helper.RequestHelper
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class MultipartApiDelegateTest {
    private val gson = Gson()
    private val url = "http://localhost"
    private val mockClient = mockk<OkHttpClient>(relaxed = true)
    private val mockEligibilityRepo = mockk<EligibilityRepository>()

    private val requestHelper = mockk<RequestHelper>()

    private val context = mockk<Context>()

    private lateinit var api: MultipartApiDelegate

    @Before
    fun setup() {
        api = MultipartApiDelegate(
            gson = gson,
            helper = requestHelper,
            url = url,
            rest = mockClient,
        )
    }

    @Test
    fun `upload should return Content when successful`(): Unit = runBlocking {
        val token = "test-token"
        val type = "application/octet-stream"
        coEvery { requestHelper.getType(any()) } returns type
        coEvery { mockEligibilityRepo.get() } returns token
        val file = createTempFile().apply {
            writeText("dummy content")
            deleteOnExit()
        }
        val mockResponseBody = """{
            "ResponseCode": "123",
            "uploadedFiles": "${file.name}"
        }"""
        val mockResponse = mockk<Response>(relaxed = true) {
            every { isSuccessful } returns true
            every { body } returns ResponseBody.create("application/json".toMediaType(), mockResponseBody)
        }

        val mockCall = mockk<Call>()
        every { mockCall.execute() } returns mockResponse
        every { mockClient.newCall(any()) } returns mockCall

        val content = api.upload("<test-token>", listOf(file.path))

        assertNotNull(content)
        assertEquals(file.name, content)
    }
}
