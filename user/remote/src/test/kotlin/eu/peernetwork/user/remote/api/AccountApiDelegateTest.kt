package eu.peernetwork.user.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import eu.peernetwork.user.data.api.AccountApi
import eu.peernetwork.user.remote.mock.ProfileMock
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import `protected`.eu.peernetwork.user.remote.ProfileQuery
import java.util.UUID
import kotlin.test.assertEquals

internal class AccountApiDelegateTest {
    private val client = mockk<ApolloClient>()

    private lateinit var api: AccountApi

    @Before
    fun setup() {
        api = AccountApiDelegate(client)
    }

    @Test
    fun `test get user by id`(): Unit = runBlocking {
        val profile = ProfileMock.profile()
        val mockData = mockk<ProfileQuery.Data>()
        val operation = mockk<Operation<ProfileQuery.Data>>(relaxed = true)

        coEvery { client.query(any<ProfileQuery>()).execute() } returns ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()
        every { mockData.profile.affectedRows } returns profile

        val result = api.get("<test-id>")

        assertEquals(result.id, profile.id)
        assertEquals(result.slug, profile.slug)
    }
}
