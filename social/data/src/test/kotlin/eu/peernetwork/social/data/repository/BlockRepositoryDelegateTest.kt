package eu.peernetwork.social.data.repository

import eu.peernetwork.core.common.paging.Page
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.social.data.api.BlockApi
import eu.peernetwork.social.domain.model.Block
import eu.peernetwork.social.domain.repository.BlockRepository
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
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BlockRepositoryDelegateTest {
    private val api = mockk<BlockApi>()
    private val dispatcher = StandardTestDispatcher()
    private lateinit var repository: BlockRepository

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repository = BlockRepositoryDelegate(api)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test get blocked users`(): Unit = runTest {
        val id = "<test-id>"
        val page = mockk<Pageable>(relaxed = true)
        val members = mockk<Page<Block>>(relaxed = true)
        coEvery { api.get(any(), any()) } returns members
        repository.get(id, page)
        coVerify { api.get(id, page) }
    }

    @Test
    fun `test block user`(): Unit = runTest {
        val id = "<test-id>"
        val value = true
        coEvery { api.block(any()) } returns value
        val result = repository.block(id)
        assertEquals(result, value)
    }
}