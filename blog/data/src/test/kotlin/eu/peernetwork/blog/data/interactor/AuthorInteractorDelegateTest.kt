package eu.peernetwork.blog.data.interactor

import eu.peernetwork.blog.domain.interactor.AuthorInteractor
import eu.peernetwork.user.domain.model.Account
import eu.peernetwork.user.domain.usecase.AuthUserUsecase
import eu.peernetwork.user.domain.usecase.ProfileUsecase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

internal class AuthorInteractorDelegateTest {
    private val usecase = mockk<ProfileUsecase>()

    private val authUserUsecase = mockk<AuthUserUsecase>()

    private lateinit var interactor: AuthorInteractor

    @Before
    fun setup() {
        interactor = AuthorInteractorDelegate(usecase, authUserUsecase)
    }

    @Test
    fun `test get author`(): Unit = runBlocking {
        val mock = mockk<Account>(relaxed = true)
        coEvery { authUserUsecase() } returns mock
        val result = interactor.get()
        assertEquals(result.id, mock.id)
        coVerify { authUserUsecase() }
    }

    @Test
    fun `test get author by id`(): Unit = runBlocking {
        val mock = mockk<Account>(relaxed = true)
        coEvery { usecase(any()) } returns mock
        val result = interactor.get(mock.id)
        assertEquals(result.id, mock.id)
        coVerify { usecase(mock.id) }
    }
}
