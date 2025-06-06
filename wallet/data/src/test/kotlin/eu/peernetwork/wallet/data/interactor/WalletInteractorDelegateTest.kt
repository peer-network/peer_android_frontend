package eu.peernetwork.wallet.data.interactor

import eu.peernetwork.wallet.domain.interactor.WalletInteractor
import eu.peernetwork.wallet.domain.model.Transfer
import eu.peernetwork.wallet.domain.model.Wallet
import eu.peernetwork.wallet.domain.repository.TransferRepository
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

    private val transferRepository = mockk<TransferRepository>()

    private lateinit var interactor: WalletInteractor

    @Before
    fun setup() {
        interactor = WalletInteractorDelegate(walletRepository, transferRepository)
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
        val transfer = Transfer("<test-recipient>", BigDecimal(1.0))
        coEvery { walletRepository.get() } returns wallet
        coEvery { transferRepository.send(any(), any()) } returns transfer
        val result = interactor.send(transfer.recipient, transfer.token)
        assertEquals(result, transfer)
        assertEquals(wallet.balance, interactor.observe().first().balance)
    }

    @Test
    fun `test send token with no existing balance`(): Unit = runBlocking {
        val wallet = Wallet(BigDecimal(2.0), 0.1f, "$")
        val transfer = Transfer("<test-recipient>", BigDecimal(1.0))
        coEvery { walletRepository.get() } returns wallet
        coEvery { transferRepository.send(any(), any()) } returns transfer
        interactor.get()
        val result = interactor.send(transfer.recipient, transfer.token)
        assertEquals(result, transfer)
        assertEquals(wallet.balance - transfer.token, interactor.observe().first().balance)
    }
}
