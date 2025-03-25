package eu.peernetwork.user.data.repository

import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.user.data.api.SearchApi
import eu.peernetwork.user.domain.model.User
import eu.peernetwork.user.domain.repository.SearchRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class SearchRepositoryDelegateTest {
    private val api = mockk<SearchApi>()

    private lateinit var repository: SearchRepository

    @Before
    fun setup() {
        repository = SearchRepositoryDelegate(api)
    }

    @Test
    fun `test filter users by username`(): Unit = runBlocking {
        val username = "<test-username>"
        val pageable = mockk<Pageable>()
        val expectedUsers = listOf(mockk<User>())

        coEvery { api.findByUsername(username, pageable) } returns expectedUsers

        repository.filterByUsername(username, pageable)

        coVerify { api.findByUsername(username, pageable) }
        repository.observe().take(count = 1).collect { users ->
            assertEquals(expectedUsers, users)
        }
    }
}
