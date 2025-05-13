package eu.peernetwork.user.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import eu.peernetwork.core.remote.model.Status
import eu.peernetwork.user.data.api.AccountApi
import eu.peernetwork.user.remote.mock.AccountMock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import protected.eu.peernetwork.user.remote.DeleteAccountMutation
import `protected`.eu.peernetwork.user.remote.ProfileQuery
import protected.eu.peernetwork.user.remote.UpdatePasswordMutation
import public.eu.peernetwork.user.remote.RegisterMutation
import public.eu.peernetwork.user.remote.VerifiedAccountMutation
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class AccountApiDelegateTest {
    private val url = "http://locahost"

    private val client = mockk<ApolloClient>()

    private lateinit var api: AccountApi

    @Before
    fun setup() {
        api = AccountApiDelegate(url, client)
    }

    @Test
    fun `test get user by id success`(): Unit = runBlocking {
        val profile = AccountMock.profile()
        val mockData = mockk<ProfileQuery.Data>()
        val operation = mockk<Operation<ProfileQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.getProfile } returns profile
        coEvery { client.query(any<ProfileQuery>()).execute() } returns mockResponse

        val result = api.get("<test-id>")

        assertEquals(result.id, profile.affectedRows?.id)
        assertEquals(result.slug, profile.affectedRows?.slug)
    }

    @Test
    fun `test get user by id error`(): Unit = runBlocking {
        val profile = AccountMock.profile().copy(status = Status.ERROR.value)
        val mockData = mockk<ProfileQuery.Data>()
        val operation = mockk<Operation<ProfileQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.getProfile } returns profile
        coEvery { client.query(any<ProfileQuery>()).execute() } returns mockResponse

        val result = try {
            api.get("<test-id>")
        } catch (error: Throwable) {
            null
        }
        assertNull(result)
    }

    @Test
    fun `test register user success`(): Unit = runBlocking {
        val mockModel = AccountMock.register()
        val mockData = mockk<RegisterMutation.Data>()
        val operation = mockk<Operation<RegisterMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.register } returns mockModel
        coEvery { client.mutation(any<RegisterMutation>()).execute() } returns mockResponse

        val result = api.register(AccountMock.user())

        assertEquals(result, mockModel.userid)
    }

    @Test
    fun `test register user error`(): Unit = runBlocking {
        val mockModel = AccountMock.register().copy(status = Status.ERROR.value)
        val mockData = mockk<RegisterMutation.Data>()
        val operation = mockk<Operation<RegisterMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.register } returns mockModel
        coEvery { client.mutation(any<RegisterMutation>()).execute() } returns mockResponse

        val result = try {
            api.register(AccountMock.user())
        } catch (error: Throwable) {
            null
        }
        assertNull(result)
    }

    @Test
    fun `test change user password success`(): Unit = runBlocking {
        val password = "<test-password>"
        val newPassword = "<test-new-password>"
        val mockModel = AccountMock.password()
        val mockData = mockk<UpdatePasswordMutation.Data>()
        val operation = mockk<Operation<UpdatePasswordMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.updatePassword } returns mockModel
        coEvery { client.mutation(any<UpdatePasswordMutation>()).execute() } returns mockResponse

        api.changePassword(password, newPassword)

        coVerify { client.mutation(UpdatePasswordMutation(newPassword, password)) }
    }

    @Test
    fun `test change user password error`(): Unit = runBlocking {
        val password = "<test-password>"
        val newPassword = "<test-new-password>"
        val mockModel = AccountMock.password().copy(status = Status.ERROR.value)
        val mockData = mockk<UpdatePasswordMutation.Data>()
        val operation = mockk<Operation<UpdatePasswordMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.updatePassword } returns mockModel
        coEvery { client.mutation(any<UpdatePasswordMutation>()).execute() } returns mockResponse

        val result = try {
            api.changePassword(password, newPassword)
        } catch (error: Throwable) {
            null
        }
        assertNull(result)
        coVerify { client.mutation(UpdatePasswordMutation(newPassword, password)) }
    }

    @Test
    fun `test user verification success`(): Unit = runBlocking {
        val code = "<test-code>"
        val mockModel = AccountMock.verification()
        val mockData = mockk<VerifiedAccountMutation.Data>()
        val operation = mockk<Operation<VerifiedAccountMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.verifyAccount } returns mockModel
        coEvery { client.mutation(any<VerifiedAccountMutation>()).execute() } returns mockResponse

        api.activate(code)

        coVerify { client.mutation(VerifiedAccountMutation(code)) }
    }

    @Test
    fun `test user verification error`(): Unit = runBlocking {
        val code = "<test-code>"
        val mockModel = AccountMock.verification().copy(status = Status.ERROR.value)
        val mockData = mockk<VerifiedAccountMutation.Data>()
        val operation = mockk<Operation<VerifiedAccountMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.verifyAccount } returns mockModel
        coEvery { client.mutation(any<VerifiedAccountMutation>()).execute() } returns mockResponse

        val result = try {
            api.activate(code)
        } catch (error: Throwable) {
            null
        }
        assertNull(result)
        coVerify { client.mutation(VerifiedAccountMutation(code)) }
    }

    @Test
    fun `test delete user success`(): Unit = runBlocking {
        val password = "<test-password>"
        val mockModel = AccountMock.delete()
        val mockData = mockk<DeleteAccountMutation.Data>()
        val operation = mockk<Operation<DeleteAccountMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.deleteAccount } returns mockModel
        coEvery { client.mutation(any<DeleteAccountMutation>()).execute() } returns mockResponse

        api.delete(password)

        coVerify { client.mutation(DeleteAccountMutation(password)) }
    }

    @Test
    fun `test delete user error`(): Unit = runBlocking {
        val password = "<test-password>"
        val mockModel = AccountMock.delete().copy(status = Status.ERROR.value)
        val mockData = mockk<DeleteAccountMutation.Data>()
        val operation = mockk<Operation<DeleteAccountMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.deleteAccount } returns mockModel
        coEvery { client.mutation(any<DeleteAccountMutation>()).execute() } returns mockResponse

        val result = try {
            api.delete(password)
        } catch (error: Throwable) {
            null
        }
        assertNull(result)
        coVerify { client.mutation(DeleteAccountMutation(password)) }
    }
}
