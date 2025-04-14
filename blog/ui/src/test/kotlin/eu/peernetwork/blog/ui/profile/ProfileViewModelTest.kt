package eu.peernetwork.blog.ui.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import eu.peernetwork.blog.domain.model.Author
import eu.peernetwork.blog.domain.usecase.CurrentAuthorUsecase
import eu.peernetwork.blog.ui.author.AuthorViewModel
import eu.peernetwork.blog.ui.mapper.mapFromDomain
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class ProfileViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val usecase = mockk<CurrentAuthorUsecase>()

    private lateinit var viewModel: AuthorViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = AuthorViewModel(usecase)
    }

    @Test
    fun `test get author success`() = runTest {
        val mockData = mockk<Author>(relaxed = true)
        coEvery { usecase() } coAnswers {
            delay(100)
            mockData
        }
        viewModel.getAuthor()
        viewModel.state.test {
            assertEquals(AuthorViewModel.State.Loading, awaitItem())
            assertEquals(AuthorViewModel.State.Success(mockData.mapFromDomain()), awaitItem())
        }
    }

    @Test
    fun `test get author error`() = runTest {
        val error = RuntimeException()
        coEvery { usecase() } throws error
        viewModel.getAuthor()
        viewModel.state.test {
            assertEquals(AuthorViewModel.State.Error(error), awaitItem())
        }
    }
}
