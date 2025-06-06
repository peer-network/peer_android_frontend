package eu.peernetwork.user.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import eu.peernetwork.core.remote.model.Status
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.user.data.api.SettingsApi
import eu.peernetwork.user.remote.mock.SettingsMock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import protected.eu.peernetwork.user.remote.UpdateMailMutation
import java.util.UUID
import kotlin.test.assertNull

internal class EmailSettingsApiTest {
    private val client = mockk<ApolloClient>()

    private lateinit var api: SettingsApi.SecureAttribute<String>

    @Before
    fun setup() {
        api = EmailSettingsApi(object : RequestClient {
            override fun invoke(): ApolloClient = client
        })
    }

    @Test
    fun `test email update success`(): Unit = runBlocking {
        val email = "<test-email>"
        val password = "<test-password>"
        val mockModel = SettingsMock.email()
        val mockData = mockk<UpdateMailMutation.Data>()
        val operation = mockk<Operation<UpdateMailMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.updateEmail } returns mockModel
        coEvery { client.mutation(any<UpdateMailMutation>()).execute() } returns mockResponse

        api(email, password)

        coVerify { client.mutation(UpdateMailMutation(email, password)) }
    }

    @Test
    fun `test email update error`(): Unit = runBlocking {
        val email = "<test-email>"
        val password = "<test-password>"
        val mockModel = SettingsMock.email().copy(status = Status.ERROR.value)
        val mockData = mockk<UpdateMailMutation.Data>()
        val operation = mockk<Operation<UpdateMailMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.updateEmail } returns mockModel
        coEvery { client.mutation(any<UpdateMailMutation>()).execute() } returns mockResponse

        val result = try {
            api(email, password)
        } catch (_: Throwable) {
            null
        }
        assertNull(result)
        coVerify { client.mutation(UpdateMailMutation(email, password)) }
    }
}
