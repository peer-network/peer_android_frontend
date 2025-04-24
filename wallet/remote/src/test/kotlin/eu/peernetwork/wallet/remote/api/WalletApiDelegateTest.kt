package eu.peernetwork.wallet.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import eu.peernetwork.wallet.data.api.WalletApi
import eu.peernetwork.wallet.remote.mapper.toBigDecimalOrNull
import eu.peernetwork.wallet.remote.mock.WalletMock
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import wallet.wallet.eu.peernetwork.wallet.remote.CurrentliquidityQuery
import java.util.UUID
import kotlin.test.assertEquals

internal class WalletApiDelegateTest {
    private val client = mockk<ApolloClient>()

    private lateinit var api: WalletApi

    @Before
    fun setup() {
        api = WalletApiDelegate(client)
    }

    @Test
    fun `test get wallet success`(): Unit = runBlocking {
        val wallet = WalletMock.wallet()
        val mockData = mockk<CurrentliquidityQuery.Data>()
        val operation = mockk<Operation<CurrentliquidityQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.currentliquidity } returns wallet
        coEvery { client.query(any<CurrentliquidityQuery>()).execute() } returns mockResponse

        val result = api.get()
        assertEquals(result.balance, wallet.currentliquidity.toBigDecimalOrNull())
    }
}
