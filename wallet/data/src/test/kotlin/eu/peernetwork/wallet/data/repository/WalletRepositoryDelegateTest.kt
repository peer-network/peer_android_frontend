package eu.peernetwork.wallet.data.repository

import eu.peernetwork.wallet.data.api.WalletApi
import eu.peernetwork.wallet.domain.model.Wallet
import eu.peernetwork.wallet.domain.repository.WalletRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

internal class WalletRepositoryDelegateTest {
    private val api = mockk<WalletApi>()

    private lateinit var repository: WalletRepository

    @Before
    fun setup() {
        repository = WalletRepositoryDelegate(api)
    }

    @Test
    fun `test get wallet`(): Unit = runBlocking {
        val wallet = mockk<Wallet>()
        coEvery { api.get() } returns wallet

        val result = repository.get()

        assertEquals(result, wallet)
    }
}
