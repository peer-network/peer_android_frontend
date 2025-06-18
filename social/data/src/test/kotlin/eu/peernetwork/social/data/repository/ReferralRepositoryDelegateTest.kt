package eu.peernetwork.social.data.repository

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.data.api.ReferralApi
import eu.peernetwork.social.domain.model.Referral
import eu.peernetwork.social.domain.repository.ReferralRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class ReferralRepositoryDelegateTest {
    private val api = mockk<ReferralApi>()
    private val dispatcher = StandardTestDispatcher()
    private lateinit var repository: ReferralRepository

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repository = ReferralRepositoryDelegate(api)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test get referrals`(): Unit = runTest {
        val id = "<test-id>"
        val page = mockk<Pageable>(relaxed = true)
        val members = mockk<Page<Referral>>(relaxed = true)
        coEvery { api.get(any(), any()) } returns members
        repository.getAll(id, page)
        coVerify { api.get(id, page) }
    }
}