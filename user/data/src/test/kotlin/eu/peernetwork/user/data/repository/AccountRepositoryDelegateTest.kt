package eu.peernetwork.user.data.repository

import eu.peernetwork.user.data.api.AccountApi
import eu.peernetwork.user.data.api.SettingsApi
import eu.peernetwork.user.data.provider.SettingsProvider
import eu.peernetwork.user.domain.model.Account
import eu.peernetwork.user.domain.model.AccountDetail
import eu.peernetwork.user.domain.repository.AccountRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class AccountRepositoryDelegateTest {
    private val api = mockk<AccountApi>()

    private val provider = mockk<SettingsProvider>()

    private lateinit var repository: AccountRepository

    @Before
    fun setup() {
        repository = AccountRepositoryDelegate(api, provider)
    }

    @Test
    fun `test get user by id`(): Unit = runBlocking {
        val id = "<test-id>"
        val mockResponse = mockk<Account>()
        coEvery { api.get(any()) } returns mockResponse

        val result = repository.get(id)

        coVerify { api.get(id) }
        assertEquals(mockResponse, result)
    }

    @Test
    fun `test register user`(): Unit = runBlocking {
        val mockResponse = "<test-user-id>"
        val mockUser = mockk<AccountDetail>()
        coEvery { api.register(any()) } returns mockResponse

        val result = repository.register(mockUser)

        coVerify { api.register(mockUser) }
        assertEquals(mockResponse, result)
    }

    @Test
    fun `test update user attribute`(): Unit = runBlocking {
        val attr = Pair("<test-key>", 0)
        val settings = mockk<SettingsApi.Updatable<Int>>()
        coEvery { settings(any()) } returns Unit
        coEvery { provider.get(any()) } returns settings

        repository.update(mapOf(attr))

        coVerify { provider.get(attr.first) }
        coVerify { settings(attr.second) }
    }

    @Test
    fun `test update user secured attribute success`(): Unit = runBlocking {
        val attr = Pair("<test-key>", "<test-value>")
        val password = "<test-password>"
        val settings = mockk<SettingsApi.SecureUpdatable<String>>()
        coEvery { settings(any(), any()) } returns Unit
        coEvery { provider.get(any()) } returns settings

        repository.update(mapOf(attr), password)

        coVerify { provider.get(attr.first) }
        coVerify { settings(attr.second, password) }
    }

    @Test
    fun `test update user secured attribute error`(): Unit = runBlocking {
        val attr = Pair("<test-key>", "<test-value>")
        val settings = mockk<SettingsApi.SecureUpdatable<String>>()
        coEvery { settings(any(), any()) } returns Unit
        coEvery { provider.get(any()) } returns settings

        val result = try {
            repository.update(mapOf(attr))
        } catch (error: Throwable) {
            null
        }
        assertNull(result)
        coVerify { provider.get(attr.first) }
        coVerify(exactly = 0) { settings(attr.second, any()) }
    }

    @Test
    fun `test change user password`(): Unit = runBlocking {
        val oldPassword = "<test-old-password>"
        val newPassword = "<test-new-password>"
        coEvery { api.changePassword(any(), any()) } returns Unit

        repository.changePassword(oldPassword, newPassword)

        coVerify { api.changePassword(oldPassword, newPassword) }
    }

    @Test
    fun `test delete user`(): Unit = runBlocking {
        val password = "<test-password>"
        coEvery { api.delete(any()) } returns Unit

        repository.delete(password)

        coVerify { api.delete(password) }
    }
}
