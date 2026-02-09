package eu.peernetwork.wallet.data.repository

import eu.peernetwork.wallet.data.api.RewardApi
import eu.peernetwork.wallet.domain.model.Reward
import eu.peernetwork.wallet.domain.repository.RewardRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class RewardRepositoryDelegateTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    private val api = mockk<RewardApi>()

    private lateinit var repository: RewardRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = RewardRepositoryDelegate(api)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test reward refresh`(): Unit = runTest {
        val reward = mockk<Reward>()

        coEvery { api.get() } returns listOf(reward)

        val emissions = mutableListOf<List<Reward>>()
        val result = repository.get()

        repository.observe().take(1).toList(emissions)

        assertEquals(listOf(reward), result)
        assertEquals(listOf(listOf(reward)), emissions)
    }
}
