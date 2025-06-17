package eu.peernetwork.wallet.data.interactor

import eu.peernetwork.wallet.domain.interactor.WalletInteractor
import eu.peernetwork.wallet.domain.model.Receipt
import eu.peernetwork.wallet.domain.model.Wallet
import eu.peernetwork.wallet.domain.repository.TransactionRepository
import eu.peernetwork.wallet.domain.repository.WalletRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

internal class WalletInteractorDelegateTest {
    private val walletRepository = mockk<WalletRepository>()

    private val transactionRepository = mockk<TransactionRepository>()

    private lateinit var interactor: WalletInteractor

    @Before
    fun setup() {
        interactor = WalletInteractorDelegate(walletRepository, transactionRepository)
    }

    @Test
    fun `test get balance`(): Unit = runBlocking {
        val wallet = mockk<Wallet>()
        coEvery { walletRepository.get() } returns wallet
        val result = interactor.get()
        assertEquals(result, wallet)
        assertEquals(result, interactor.observe().first())
    }

    @Test
    fun `test send token`(): Unit = runBlocking {
        val wallet = Wallet(BigDecimal(2.0), 0.1f, "$")
        val receipt = Receipt("<test-recipient>", BigDecimal(1.0))
        coEvery { walletRepository.get() } returns wallet
        coEvery { transactionRepository.send(any(), any()) } returns receipt
        val result = interactor.send(receipt.recipient, receipt.price)
        assertEquals(result, receipt)
        assertEquals(wallet.balance, interactor.observe().first().balance)
    }

    @Test
    fun `test send token with no existing balance`(): Unit = runBlocking {
        val wallet = Wallet(BigDecimal(2.0), 0.1f, "$")
        val receipt = Receipt("<test-recipient>", BigDecimal(1.0))
        coEvery { walletRepository.get() } returns wallet
        coEvery { transactionRepository.send(any(), any()) } returns receipt
        interactor.get()
        val result = interactor.send(receipt.recipient, receipt.price)
        assertEquals(result, receipt)
        assertEquals(wallet.balance - receipt.price, interactor.observe().first().balance)
    }
}
