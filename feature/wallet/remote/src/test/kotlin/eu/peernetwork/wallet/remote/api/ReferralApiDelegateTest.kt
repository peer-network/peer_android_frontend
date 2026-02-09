package eu.peernetwork.wallet.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.core.remote.model.Status
import eu.peernetwork.wallet.data.api.ReferralApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import wallet.wallet.eu.peernetwork.wallet.remote.GetReferralQuery
import java.util.UUID
import kotlin.test.assertEquals

internal class ReferralApiDelegateTest {
    private val client = mockk<ApolloClient>()

    private lateinit var api: ReferralApi

    @Before
    fun setup() {
        api = ReferralApiDelegate(object : RequestClient {
            override fun invoke(): ApolloClient = client
        })
    }

    @Test
    fun `test get referral success`(): Unit = runBlocking {
        val name = "<test-name>"
        val user = GetReferralQuery.GetUserInfo(
            status = Status.SUCCESS.value,
            ResponseCode = "<test-response-code>",
            affectedRows = GetReferralQuery.AffectedRows(name)
        )
        val mockData = mockk<GetReferralQuery.Data>()
        val operation = mockk<Operation<GetReferralQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.getUserInfo } returns user
        coEvery { client.query(any<GetReferralQuery>()).execute() } returns mockResponse

        val result = api.get()

        assertEquals(result, name)
    }

    @Test
    fun `test get referral error`(): Unit = runBlocking {
        val name = "<test-name>"
        val user = GetReferralQuery.GetUserInfo(
            status = Status.ERROR.value,
            ResponseCode = "<test-response-code>",
            affectedRows = GetReferralQuery.AffectedRows(name)
        )
        val mockData = mockk<GetReferralQuery.Data>()
        val operation = mockk<Operation<GetReferralQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.getUserInfo } returns user
        coEvery { client.query(any<GetReferralQuery>()).execute() } returns mockResponse

        val result = try {
            api.get()
        } catch (_: Throwable) {
            null
        }
        assertEquals(result, null)
        coVerify { client.query(any<GetReferralQuery>()).execute() }
    }
}
