package eu.peernetwork.user.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.core.remote.model.Status
import eu.peernetwork.user.data.api.PreferenceApi
import eu.peernetwork.user.remote.mock.PreferenceMock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import protected.eu.peernetwork.user.remote.PreferenceQuery
import java.util.UUID
import kotlin.test.assertNull

internal class PreferenceApiDelegateTest {
    private val client = mockk<ApolloClient>()

    private lateinit var api: PreferenceApi

    @Before
    fun setup() {
        api = PreferenceApiDelegate(object : RequestClient {
            override fun invoke(): ApolloClient = client
        })
    }

    @Test
    fun `test preference success`(): Unit = runBlocking {
        val mockModel = PreferenceMock.get()
        val mockData = mockk<PreferenceQuery.Data>()
        val operation = mockk<Operation<PreferenceQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.getUserInfo } returns mockModel
        coEvery { client.query(any<PreferenceQuery>()).execute() } returns mockResponse

        api.get()

        coVerify { client.query(PreferenceQuery()) }
    }

    @Test
    fun `test preference error`(): Unit = runBlocking {
        val mockModel = PreferenceMock.get().copy(status = Status.ERROR.value)
        val mockData = mockk<PreferenceQuery.Data>()
        val operation = mockk<Operation<PreferenceQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.getUserInfo } returns mockModel
        coEvery { client.query(any<PreferenceQuery>()).execute() } returns mockResponse

        val result = try {
            api.get()
        } catch (_: Throwable) { null }

        assertNull(result)
        coVerify { client.query(PreferenceQuery()) }
    }
}
