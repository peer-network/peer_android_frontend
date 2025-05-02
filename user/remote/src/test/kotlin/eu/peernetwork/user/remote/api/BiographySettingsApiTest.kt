package eu.peernetwork.user.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import eu.peernetwork.core.remote.model.Status
import eu.peernetwork.user.data.api.SettingsApi
import eu.peernetwork.user.remote.mock.SettingsMock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import protected.eu.peernetwork.user.remote.UpdateBiographyMutation
import java.util.UUID
import kotlin.test.assertNull

internal class BiographySettingsApiTest {
    private val client = mockk<ApolloClient>()

    private lateinit var api: SettingsApi.Updatable<String>

    @Before
    fun setup() {
        api = BiographySettingsApi(client)
    }

    @Test
    fun `test biography update success`(): Unit = runBlocking {
        val bio = "<test-bio>"
        val mockModel = SettingsMock.bio()
        val mockData = mockk<UpdateBiographyMutation.Data>()
        val operation = mockk<Operation<UpdateBiographyMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.updateBio } returns mockModel
        coEvery { client.mutation(any<UpdateBiographyMutation>()).execute() } returns mockResponse

        api(bio)

        coVerify { client.mutation(UpdateBiographyMutation(bio)) }
    }

    @Test
    fun `test biography update error`(): Unit = runBlocking {
        val bio = "<test-bio>"
        val mockModel = SettingsMock.bio().copy(status = Status.ERROR.value)
        val mockData = mockk<UpdateBiographyMutation.Data>()
        val operation = mockk<Operation<UpdateBiographyMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.updateBio } returns mockModel
        coEvery { client.mutation(any<UpdateBiographyMutation>()).execute() } returns mockResponse

        val result = try {
            api(bio)
        } catch (error: Throwable) {
            null
        }
        assertNull(result)
        coVerify { client.mutation(UpdateBiographyMutation(bio)) }
    }
}
