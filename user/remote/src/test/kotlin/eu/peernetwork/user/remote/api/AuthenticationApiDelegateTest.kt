package eu.peernetwork.user.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import eu.peernetwork.core.common.exception.BusinessException
import eu.peernetwork.core.remote.model.Status
import eu.peernetwork.user.data.api.AuthenticationApi
import eu.peernetwork.user.domain.model.Token
import eu.peernetwork.user.remote.mock.AuthenticationMock
import eu.peernetwork.user.remote.mock.TokenMock
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import public.eu.peernetwork.user.remote.HelloQuery
import public.eu.peernetwork.user.remote.LoginMutation
import public.eu.peernetwork.user.remote.RefreshTokenMutation
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class AuthenticationApiDelegateTest {
    private val client = mockk<ApolloClient>()

    private val listener = mockk<AuthenticationApi.Listener>(relaxed = true)

    private lateinit var api: AuthenticationApi

    @Before
    fun setup() {
        api = AuthenticationApiDelegate(client, listener)
    }

    @Test
    fun `test authenticated user`(): Unit = runBlocking {
        val id = "<test-current-user-id>"
        val user = HelloQuery.Hello(id)
        val mockData = mockk<HelloQuery.Data>()
        val operation = mockk<Operation<HelloQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.hello } returns user
        coEvery { client.query(any<HelloQuery>()).execute() } returns mockResponse

        val result = api.authenticated()

        assertEquals(result, id)
    }

    @Test
    fun `test user login success`(): Unit = runBlocking {
        val login = AuthenticationMock.login()
        val mockData = mockk<LoginMutation.Data>()
        val operation = mockk<Operation<LoginMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.login } returns login
        coEvery { client.mutation(any<LoginMutation>()).execute() } returns mockResponse

        val result = api.login("<test-email>", "<test-password>")

        assertEquals(result, login.accessToken)
    }

    @Test
    fun `test user login error`(): Unit = runBlocking {
        val login = AuthenticationMock.login().copy(status = Status.ERROR.value)
        val mockData = mockk<LoginMutation.Data>()
        val operation = mockk<Operation<LoginMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.login } returns login
        coEvery { client.mutation(any<LoginMutation>()).execute() } returns mockResponse

        val result = try {
            api.login("<test-email>", "<test-password>")
        } catch (error: Throwable) {
            null
        }
        assertNull(result)
    }

    @Test
    fun `test refresh token success`(): Unit = runBlocking {
        val mockModel = TokenMock.refresh()
        val mockData = mockk<RefreshTokenMutation.Data>()
        val slot: CapturingSlot<Token> = slot()
        val operation = mockk<Operation<RefreshTokenMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.refreshToken } returns mockModel
        coEvery { listener.onAuthenticationChanged(capture(slot)) } returns Unit
        coEvery { client.mutation(any<RefreshTokenMutation>()).execute() } returns mockResponse

        api.refresh("<test-token>")

        assertEquals(slot.captured.access, mockModel.accessToken)
        coVerify { listener.onAuthenticationChanged(slot.captured) }
    }

    @Test
    fun `test refresh token error`(): Unit = runBlocking {
        val mockModel = TokenMock.refresh().copy(status = Status.ERROR.value)
        val mockData = mockk<RefreshTokenMutation.Data>()
        val operation = mockk<Operation<RefreshTokenMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.refreshToken } returns mockModel
        coEvery { client.mutation(any<RefreshTokenMutation>()).execute() } returns mockResponse

        try {
            api.refresh("<test-token>")
        } catch (error: Throwable) {
            assert(error is BusinessException)
        }
        coVerify(exactly = 0) { listener.onAuthenticationChanged(any()) }
    }

    @Test
    fun `test user logout`(): Unit = runBlocking {
        api.logout()
        coVerify { listener.onAuthenticationChanged(any()) }
    }
}
