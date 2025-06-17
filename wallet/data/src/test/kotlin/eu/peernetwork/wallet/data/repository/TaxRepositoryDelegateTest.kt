package eu.peernetwork.wallet.data.repository

import eu.peernetwork.wallet.data.api.ReferralApi
import eu.peernetwork.wallet.domain.repository.TaxRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

internal class TaxRepositoryDelegateTest {
    private val api = mockk<ReferralApi>()

    private lateinit var repository: TaxRepository

    @Before
    fun setup() {
        repository = TaxRepositoryDelegate(api)
    }

    @Test
    fun `test get tax`(): Unit = runBlocking {
        val referral = "<test-referral>"
        coEvery { api.get() } returns referral
        val result = repository.getTax()
        assertEquals(result.percentage, 5.0)
    }

    @Test
    fun `test get tax with null referral`(): Unit = runBlocking {
        coEvery { api.get() } returns null
        val result = repository.getTax()
        assertEquals(result.percentage, 4.0)
    }

    @Test
    fun `test get tax with empty referral`(): Unit = runBlocking {
        coEvery { api.get() } returns ""
        val result = repository.getTax()
        assertEquals(result.percentage, 4.0)
    }
}
