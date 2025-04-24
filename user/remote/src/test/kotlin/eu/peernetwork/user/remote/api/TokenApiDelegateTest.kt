package eu.peernetwork.user.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import eu.peernetwork.core.common.exception.BusinessException
import eu.peernetwork.core.remote.model.Status
import eu.peernetwork.user.data.api.TokenApi
import eu.peernetwork.user.remote.mock.TokenMock
import eu.peernetwork.user.remote.usecase.JwtLifecycleUsecase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import public.eu.peernetwork.user.remote.RefreshTokenMutation
import java.util.UUID
import kotlin.test.assertEquals

class TokenApiDelegateTest {
    private val client = mockk<ApolloClient>()

    private val usecase = mockk<JwtLifecycleUsecase>()

    private lateinit var api: TokenApi

    @Before
    fun setup() {
        every { usecase(any()) } returns 1L
        api = TokenApiDelegate(client, usecase)
    }

    @Test
    fun `test refresh token success`(): Unit = runBlocking {
        val mockModel = TokenMock.refresh()
        val mockData = mockk<RefreshTokenMutation.Data>()
        val operation = mockk<Operation<RefreshTokenMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.refreshToken } returns mockModel
        coEvery { client.mutation(any<RefreshTokenMutation>()).execute() } returns mockResponse

        val result = api.refresh("<test-token>")
        assertEquals(result.access, mockModel.accessToken)
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

        val result = try {
            api.refresh("<test-token>")
        } catch (error: Throwable) {
            assert(error is BusinessException)
            null
        }
        assertEquals(result, null)
    }
}
