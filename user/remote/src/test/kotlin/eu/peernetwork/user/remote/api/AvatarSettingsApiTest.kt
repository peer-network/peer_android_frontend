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
import protected.eu.peernetwork.user.remote.UpdateProfilePictureMutation
import java.util.UUID
import kotlin.test.assertNull

internal class AvatarSettingsApiTest {
    private val client = mockk<ApolloClient>()

    private lateinit var api: SettingsApi.Attribute<String>

    @Before
    fun setup() {
        api = AvatarSettingsApi(object : RequestClient {
            override fun invoke(): ApolloClient = client
        })
    }

    @Test
    fun `test avatar update success`(): Unit = runBlocking {
        val avatar = "<test-avatar>"
        val mockModel = SettingsMock.avatar()
        val mockData = mockk<UpdateProfilePictureMutation.Data>()
        val operation = mockk<Operation<UpdateProfilePictureMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.updateProfileImage } returns mockModel
        coEvery { client.mutation(any<UpdateProfilePictureMutation>()).execute() } returns mockResponse

        api(avatar)

        coVerify { client.mutation(UpdateProfilePictureMutation(avatar)) }
    }

    @Test
    fun `test avatar update error`(): Unit = runBlocking {
        val avatar = "<test-avatar>"
        val mockModel = SettingsMock.avatar().copy(status = Status.ERROR.value)
        val mockData = mockk<UpdateProfilePictureMutation.Data>()
        val operation = mockk<Operation<UpdateProfilePictureMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.updateProfileImage } returns mockModel
        coEvery { client.mutation(any<UpdateProfilePictureMutation>()).execute() } returns mockResponse

        val result = try {
            api(avatar)
        } catch (error: Throwable) {
            null
        }
        assertNull(result)
        coVerify { client.mutation(UpdateProfilePictureMutation(avatar)) }
    }
}
