package eu.peernetwork.app.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.user.domain.model.User
import eu.peernetwork.user.domain.usecase.ObserveUserSearchUsecase
import eu.peernetwork.user.domain.usecase.UserSearchUsecase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class SearchViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val mockSearchUsecase = mockk<UserSearchUsecase>()
    private val mockObserveUsecase = mockk<ObserveUserSearchUsecase>()

    private val dispatcher = UnconfinedTestDispatcher()
    private val usersFlow = MutableStateFlow<List<User>>(emptyList())

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        // Mock observe usecase to return our test flow
        every { mockObserveUsecase.invoke() } returns usersFlow

        // Mock search usecase to just update our test flow when invoked
        coEvery { mockSearchUsecase.invoke(any()) } answers {
            val query = it.invocation.args[0] as String
            usersFlow.tryEmit(
                listOf(User("1", 1, query, "bio", "url"))
            )
        }

        viewModel = SearchViewModel(mockSearchUsecase, mockObserveUsecase)
    }

    @Test
    fun `initial state should be empty`() = runTest {
        viewModel.uiState.test {
            assertEquals(SearchUiState.Empty, awaitItem())
        }
    }

    @Test
    fun `should emit success state when users are received`() = runTest {
        val testUsers = listOf(
            User("1", 1, "user1", "bio1", "url1"),
            User("2", 2, "user2", "bio2", "url2")
        )

        usersFlow.tryEmit(testUsers)

        viewModel.uiState.test {
            assertEquals(SearchUiState.Empty, awaitItem()) // Initial state
            assertEquals(SearchUiState.Success(testUsers), awaitItem())
        }
    }

    @Test
    fun `searchUsers should update query and trigger search`() = runTest {
        val testQuery = "test query"

        viewModel.searchQuery.test {
            viewModel.searchUsers(testQuery)

            assertEquals("", awaitItem()) // Initial empty query
            assertEquals(testQuery, awaitItem())
        }

        coVerify { mockSearchUsecase.invoke(testQuery) }
    }

    @Test
    fun `should emit loading state during search`() = runTest {
        viewModel.uiState.test {
            assertEquals(SearchUiState.Empty, awaitItem()) // Initial state

            viewModel.searchUsers("query")

            assertEquals(SearchUiState.Loading, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit error state when search fails`() = runTest {
        val errorMessage = "Search failed"
        coEvery { mockSearchUsecase.invoke(any()) } throws RuntimeException(errorMessage)

        viewModel.uiState.test {
            assertEquals(SearchUiState.Empty, awaitItem()) // Initial state

            viewModel.searchUsers("query")

            assertEquals(SearchUiState.Loading, awaitItem())
            val errorState = awaitItem() as SearchUiState.Error
            assertEquals(errorMessage, errorState.message)
        }
    }
}