package eu.peernetwork.wallet.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.core.remote.model.Status
import eu.peernetwork.wallet.data.api.TransferApi
import eu.peernetwork.wallet.remote.mock.TransferMock
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import wallet.wallet.eu.peernetwork.wallet.remote.ResolveTransferMutation
import java.math.BigDecimal
import java.util.UUID
import kotlin.test.assertEquals

internal class TransferApiDelegateTest {
    private val client = mockk<ApolloClient>()

    private lateinit var api: TransferApi

    @Before
    fun setup() {
        api = TransferApiDelegate(object : RequestClient {
            override fun invoke(): ApolloClient = client
        })
    }

    @Test
    fun `test token transfer success`(): Unit = runBlocking {
        val recipient = "<test-recipient>"
        val tokens = BigDecimal(1.0)
        val transfer = TransferMock.transaction()
        val mockData = mockk<ResolveTransferMutation.Data>()
        val operation = mockk<Operation<ResolveTransferMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.resolveTransfer } returns transfer
        coEvery { client.mutation(any<ResolveTransferMutation>()).execute() } returns mockResponse

        val result = api.send(recipient, tokens)

        assertEquals(result.price, tokens)
        assertEquals(result.recipient, recipient)

        coEvery { client.mutation(any<ResolveTransferMutation>()).execute() } returns mockResponse
    }

    @Test
    fun `test token transfer error`(): Unit = runBlocking {
        val recipient = "<test-recipient>"
        val tokens = BigDecimal(1.0)
        val transfer = TransferMock.transaction().copy(status = Status.ERROR.value)
        val mockData = mockk<ResolveTransferMutation.Data>()
        val operation = mockk<Operation<ResolveTransferMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.resolveTransfer } returns transfer
        coEvery { client.mutation(any<ResolveTransferMutation>()).execute() } returns mockResponse

        val result = try {
            api.send(recipient, tokens)
        } catch (_: Throwable) {
            null
        }
        assertEquals(result, null)
        coEvery { client.mutation(any<ResolveTransferMutation>()).execute() } returns mockResponse
    }
}
