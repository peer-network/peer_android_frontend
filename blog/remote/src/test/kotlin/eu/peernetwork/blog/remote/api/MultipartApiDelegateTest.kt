package eu.peernetwork.blog.remote.api

import com.google.gson.Gson
import eu.peernetwork.blog.domain.repository.EligibilityRepository
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

    private lateinit var api: MultipartApiDelegate

    @Before
    fun setup() {
        api = MultipartApiDelegate(
            gson = gson,
            url = url,
            rest = mockClient,
            eligibilityRepository = mockEligibilityRepo
        )
    }

    @Test
    fun `upload should return Content when successful`(): Unit = runBlocking {
        val token = "test-token"
        coEvery { mockEligibilityRepo.get() } returns token

        val file = createTempFile().apply {
            writeText("dummy content")
            deleteOnExit()
        }

        val mockResponseBody = """{
            "id": "123",
            "title": "Test",
            "description": "desc",
            "media": [],
            "author": {"id":"1","username":"user"},
            "type": "TEXT",
            "createdAt": 0,
            "likes":0,
            "dislikes":0,
            "isLiked":false,
            "isDisliked":false,
            "isViewed":false,
            "views":0,
            "comment":0,
            "url":"http://localhost/file"
        }"""
        val mockResponse = mockk<Response>(relaxed = true) {
            every { isSuccessful } returns true
            every { body } returns ResponseBody.create("application/json".toMediaType(), mockResponseBody)
        }

        val mockCall = mockk<Call>()
        every { mockCall.execute() } returns mockResponse
        every { mockClient.newCall(any()) } returns mockCall

        val content = api.upload(file)

        assertNotNull(content)
        assertEquals("123", content.id)
        assertEquals("Test", content.title)
        assertEquals("TEXT", content.type.name)
    }
}