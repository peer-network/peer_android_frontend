package eu.peernetwork.user.data.repository

import eu.peernetwork.user.data.api.AuthenticationApi
import eu.peernetwork.user.domain.repository.AuthenticationRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

internal class AuthenticationRepositoryDelegateTest {
    private val api = mockk<AuthenticationApi>()

    private lateinit var repository: AuthenticationRepository

    @Before
    fun setup() {
        repository = AuthenticationRepositoryDelegate(api)
    }

    @Test
    fun `test authenticated user`(): Unit = runBlocking {
        val id = "<test-current-user-id>"
        coEvery { api.authenticated() } returns id
        val result = repository.authenticated()
        assertEquals(result, id)
        coVerify { api.authenticated() }
    }

    @Test
    fun `test user login`(): Unit = runBlocking {
        val email = "<test-email>"
        val password = "<test-password>"
        val mockToken = "<test-mock-token>"

        coEvery { api.login(any(), any()) } returns mockToken

        val result = repository.login(email, password)

        assertEquals(result, mockToken)
        coVerify { api.login(email, password) }
    }

    @Test
    fun `test user logout`(): Unit = runBlocking {
        coEvery { api.logout() } returns Unit

        repository.logout()

        coVerify { api.logout() }
    }
}
