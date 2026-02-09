package eu.peernetwork.wallet.data.repository

import eu.peernetwork.wallet.data.api.TransferApi
import eu.peernetwork.wallet.domain.model.Receipt
import eu.peernetwork.wallet.domain.repository.TransactionRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

internal class TransferRepositoryDelegateTest {
    private val api = mockk<TransferApi>()

    private lateinit var repository: TransactionRepository

    @Before
    fun setup() {
        repository = TransactionRepositoryDelegate(api)
    }

    @Test
    fun `test token transfer`(): Unit = runBlocking {
        val token = BigDecimal(1.0)
        val recipient = "<test-recipient>"
        val receipt = mockk<Receipt>()
        coEvery { api.send(any(), any()) } returns receipt

        val result = repository.send(recipient, token)

        assertEquals(result, receipt)
    }
}
