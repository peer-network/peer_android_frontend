package eu.peernetwork.blog.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.google.gson.Gson
import eu.peernetwork.blog.data.api.EligibilityApi
import eu.peernetwork.blog.remote.content.PostEligibilityQuery
import eu.peernetwork.core.common.interactor.SessionInteractor
import eu.peernetwork.core.remote.api.RequestClient
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import type.ContentFilterType
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


class EligibilityApiDelegateTest {
    private val gson = Gson()

    private val client = mockk<ApolloClient>()

    private val sessionInteractor = mockk<SessionInteractor>(relaxed = true)

    private val url = "http://locahost"

    private lateinit var api: EligibilityApi

    @Before
    fun setup() {
        coEvery { sessionInteractor.mode() } returns ContentFilterType.MYGRANDMALIKES.name
        api = EligibilityApiDelegate(object : RequestClient {
            override fun invoke(): ApolloClient = client
        })
    }

    @Test
    fun `test fetch`(): Unit = runBlocking {
        val expectedToken = "fake-eligibility-token"
        val postEligibility = PostEligibilityQuery.PostEligibility(
            status = "OK",
            ResponseCode = "",
            eligibilityToken = expectedToken
        )
        val mockData = mockk<PostEligibilityQuery.Data>()
        val operation = mockk<Operation<PostEligibilityQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.postEligibility } returns postEligibility
        coEvery { client.query(any<PostEligibilityQuery>()).execute() } returns mockResponse

        val result = api.fetch()

        assertNotNull(result.token.first())
        verify { client.query(PostEligibilityQuery()) }
    }
}