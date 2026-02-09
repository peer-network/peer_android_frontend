package eu.peernetwork.wallet.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.core.remote.model.Status
import eu.peernetwork.wallet.data.api.RewardApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import wallet.wallet.eu.peernetwork.wallet.remote.DailyRewardsQuery
import java.util.UUID
import kotlin.test.assertEquals

internal class RewardApiDelegateTest {
    private val client = mockk<ApolloClient>()

    private lateinit var api: RewardApi

    @Before
    fun setup() {
        api = RewardApiDelegate(object : RequestClient {
            override fun invoke(): ApolloClient = client
        })
    }

    @Test
    fun `test reward success`(): Unit = runBlocking {
        val name = "<test-name>"
        val user = DailyRewardsQuery.GetDailyFreeStatus(
            status = Status.SUCCESS.value,
            ResponseCode = "<test-response-code>",
            affectedRows = listOf(
                DailyRewardsQuery.AffectedRow(
                    name = name,
                    used = 0,
                    available = 0
                )
            )
        )
        val mockData = mockk<DailyRewardsQuery.Data>()
        val operation = mockk<Operation<DailyRewardsQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.getDailyFreeStatus } returns user
        coEvery { client.query(any<DailyRewardsQuery>()).execute() } returns mockResponse

        val result = api.get()

        assertEquals(result.first().type, name)
    }

    @Test
    fun `test reward error`(): Unit = runBlocking {
        val name = "<test-name>"
        val user = DailyRewardsQuery.GetDailyFreeStatus(
            status = Status.ERROR.value,
            ResponseCode = "<test-response-code>",
            affectedRows = listOf(
                DailyRewardsQuery.AffectedRow(
                    name = name,
                    used = 0,
                    available = 0
                )
            )
        )
        val mockData = mockk<DailyRewardsQuery.Data>()
        val operation = mockk<Operation<DailyRewardsQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.getDailyFreeStatus } returns user
        coEvery { client.query(any<DailyRewardsQuery>()).execute() } returns mockResponse

        val result = try {
            api.get()
        } catch (_: Throwable) {
            null
        }
        assertEquals(result, null)
        coVerify { client.query(any<DailyRewardsQuery>()).execute() }
    }
}
