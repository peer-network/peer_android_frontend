package eu.peernetwork.social.data.repository

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.data.api.FollowApi
import eu.peernetwork.social.domain.model.Member
import eu.peernetwork.social.domain.repository.FollowRepository
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
internal class FollowRepositoryDelegateTest {
    private val api = mockk<FollowApi>()

    private val dispatcher = StandardTestDispatcher()

    private lateinit var repository: FollowRepository

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repository = FollowRepositoryDelegate(api)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test follow member`(): Unit = runTest {
        val id = "<test-id>"
        val value = true
        coEvery { api.follow(any()) } returns value
        val result = repository.follow(id)
        assertEquals(result, value)
    }

    @Test
    fun `test get followers`(): Unit = runTest {
        val id = "<test-id>"
        val page = mockk<Pageable>(relaxed = true)
        val members = mockk<Page<Member>>(relaxed = true)
        coEvery { api.followers(any(), any()) } returns members
        repository.followers(id, page)
        coVerify { api.followers(id, page) }
    }

    @Test
    fun `test get followings`(): Unit = runTest {
        val id = "<test-id>"
        val page = mockk<Pageable>(relaxed = true)
        val members = mockk<Page<Member>>(relaxed = true)
        coEvery { api.following(any(), any()) } returns members
        repository.following(id, page)
        coVerify { api.following(id, page) }
    }

    @Test
    fun `test get friends`(): Unit = runTest {
        val page = mockk<Pageable>(relaxed = true)
        val members = mockk<Page<Member>>(relaxed = true)
        coEvery { api.friends(any()) } returns members
        repository.friends(page)
        coVerify { api.friends(page) }
    }
}
