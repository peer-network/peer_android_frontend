package eu.peernetwork.social.data.interactor

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.domain.interactor.SearchInteractor
import eu.peernetwork.user.domain.model.User
import eu.peernetwork.user.domain.repository.SearchRepository
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
internal class SearchInteractorDelegateTest {
    private val repository = mockk<SearchRepository>()

    private val dispatcher = StandardTestDispatcher()

    private lateinit var interactor: SearchInteractor

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        interactor = SearchInteractorDelegate(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test find member`(): Unit = runTest {
        val username = "<test-username>"
        val page = mockk<Pageable>(relaxed = true)
        val users = mockk<Page<User>>(relaxed = true)
        coEvery { repository.filterByUsername(any(), any()) } returns users
        interactor.findMember(username, page)
        coVerify { repository.filterByUsername(username, page) }
    }
}
