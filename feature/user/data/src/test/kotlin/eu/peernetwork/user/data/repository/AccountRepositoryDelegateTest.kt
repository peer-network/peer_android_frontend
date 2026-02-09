package eu.peernetwork.user.data.repository

import eu.peernetwork.user.data.api.AccountApi
import eu.peernetwork.user.data.api.SettingsApi
import eu.peernetwork.user.data.mapper.mapToDomain
import eu.peernetwork.user.data.model.AccountModel
import eu.peernetwork.user.data.provider.SettingsProvider
import eu.peernetwork.user.domain.model.Status
import eu.peernetwork.user.domain.model.UserDetail
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
        val mockResponse = AccountModel(
            id = "<test-id>",
            username = "<test-username>",
            slug = System.currentTimeMillis().toInt(),
            imageUrl = "<test-img>",
            biography = "<test-biography>",
            followed = 0,
            posts = 0,
            follower = 0,
            isfollowed = false,
            isfollowing = false,
            peers = 0,
            isAccessible = true,
            status = Status.ILLEGAL,
        )
        coEvery { api.get(any()) } returns mockResponse

        val result = repository.get(id)

        coVerify { api.get(id) }
        assertEquals(mockResponse.mapToDomain(), result)
    }

    @Test
    fun `test register user`(): Unit = runBlocking {
        val code = "<test-code>"
        val mockResponse = "<test-user-id>"
        val mockUser = mockk<UserDetail>()
        coEvery { api.register(any(), any()) } returns mockResponse

        val result = repository.register(mockUser, code)

        coVerify { api.register(mockUser, code) }
        assertEquals(mockResponse, result)
    }

    @Test
    fun `test update user attribute`(): Unit = runBlocking {
        val attr = Pair("<test-key>", 0)
        val settings = mockk<SettingsApi.Attribute<Int>>()
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
        val settings = mockk<SettingsApi.SecureAttribute<String>>()
        coEvery { settings(any(), any()) } returns Unit
        coEvery { provider.get(any()) } returns settings

        repository.update(mapOf(attr), password)

        coVerify { provider.get(attr.first) }
        coVerify { settings(attr.second, password) }
    }

    @Test
    fun `test update user secured attribute error`(): Unit = runBlocking {
        val attr = Pair("<test-key>", "<test-value>")
        val settings = mockk<SettingsApi.SecureAttribute<String>>()
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
    fun `test request password reset`(): Unit = runBlocking {
        val email = "<test-email>"
        coEvery { api.passwordReset(any()) } returns Unit

        repository.passwordReset(email)

        coVerify { api.passwordReset(email) }
    }

    @Test
    fun `test reset password`(): Unit = runBlocking {
        val token = "<test-token>"
        val email = "<test-email>"
        coEvery { api.resetPassword(any(), any()) } returns Unit

        repository.resetPassword(token, email)

        coVerify { api.resetPassword(token, email) }
    }

    @Test
    fun `test activate user`(): Unit = runBlocking {
        val code = "<test-code>"
        coEvery { api.activate(any()) } returns Unit

        repository.activate(code)

        coVerify { api.activate(code) }
    }

    @Test
    fun `test delete user`(): Unit = runBlocking {
        val password = "<test-password>"
        coEvery { api.delete(any()) } returns Unit

        repository.delete(password)

        coVerify { api.delete(password) }
    }
}
