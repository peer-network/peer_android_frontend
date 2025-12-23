package eu.peernetwork.wallet.data.repository

import eu.peernetwork.wallet.data.api.ReferralApi
import eu.peernetwork.wallet.data.api.TaxApi
import eu.peernetwork.wallet.domain.model.Tax
import eu.peernetwork.wallet.domain.repository.TaxRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

internal class TaxRepositoryDelegateTest {
    private val api = mockk<ReferralApi>()

    private val taxApi = mockk<TaxApi>()

    private lateinit var repository: TaxRepository

    @Before
    fun setup() {
        repository = TaxRepositoryDelegate(api, taxApi)
    }

    @Test
    fun `test get tax`(): Unit = runBlocking {
        val referral = "<test-referral>"
        val tax = mockk<Tax>(relaxed = true)
        coEvery { tax.percentage } returns 4.0
        coEvery { api.get() } returns referral
        coEvery { taxApi.getTax() } returns tax
        val result = repository.getTax()
        assertEquals(result.percentage, 4.0)
    }

    @Test
    fun `test get tax with null referral`(): Unit = runBlocking {
        val tax = mockk<Tax>(relaxed = true)
        coEvery { tax.percentage } returns 4.0
        coEvery { api.get() } returns null
        coEvery { taxApi.getTax() } returns tax
        val result = repository.getTax()
        assertEquals(result.percentage, 0.0)
    }

    @Test
    fun `test get tax with empty referral`(): Unit = runBlocking {
        val tax = mockk<Tax>(relaxed = true)
        coEvery { tax.percentage } returns 4.0
        coEvery { api.get() } returns ""
        coEvery { taxApi.getTax() } returns tax
        val result = repository.getTax()
        assertEquals(result.percentage, 0.0)
    }
}
