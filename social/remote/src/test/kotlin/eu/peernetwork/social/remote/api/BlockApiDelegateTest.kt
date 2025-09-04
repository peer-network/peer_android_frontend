package eu.peernetwork.social.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.api.Optional
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.core.remote.model.Status
import eu.peernetwork.social.data.api.BlockApi
import eu.peernetwork.social.remote.mock.BlockMock
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import social.social.eu.peernetwork.social.remote.ListBlockedUsersQuery
import social.social.eu.peernetwork.social.remote.ToggleBlockUserStatusMutation
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class BlockApiDelegateTest {
    private val client = mockk<ApolloClient>()
    private val url = "http://localhost"
    private lateinit var api: BlockApi

    @Before
    fun setup() {
        api = BlockApiDelegate(url, object : RequestClient {
            override fun invoke(): ApolloClient = client
        })
    }

    @Test
    fun `test block user success`(): Unit = runBlocking {
        val id = "<test-id>"
        val content = BlockMock.block().copy(ResponseCode = "11105")
        val mockData = mockk<ToggleBlockUserStatusMutation.Data>()
        val operation = mockk<Operation<ToggleBlockUserStatusMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.toggleBlockUserStatus } returns content
        coEvery { client.mutation(any<ToggleBlockUserStatusMutation>()).execute() } returns mockResponse

        val result = api.block(id)
        assertEquals(true, result)
        verify { client.mutation(ToggleBlockUserStatusMutation(id)) }
    }

    @Test
    fun `test block user error`(): Unit = runBlocking {
        val id = "<test-id>"
        val content = BlockMock.block().copy(status = Status.ERROR.value)
        val mockData = mockk<ToggleBlockUserStatusMutation.Data>()
        val operation = mockk<Operation<ToggleBlockUserStatusMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.toggleBlockUserStatus } returns content
        coEvery { client.mutation(any<ToggleBlockUserStatusMutation>()).execute() } returns mockResponse

        val result = try {
            api.block(id)
        } catch (_: Throwable) {
            null
        }
        assertNull(result)
    }

    @Test
    fun `test block user list`(): Unit = runBlocking {
        val page = Pageable(0,1)
        val id = "<test-id>"
        val content = BlockMock.get()
        val mockData = mockk<ListBlockedUsersQuery.Data>()
        val operation = mockk<Operation<ListBlockedUsersQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.listBlockedUsers } returns content
        coEvery { client.query(any<ListBlockedUsersQuery>()).execute() } returns mockResponse

        val result = api.get(id, page)

        assertNotNull(result.items.first())
        assertEquals(result.items.first().userId, content.affectedRows?.iBlocked?.first()?.userid)

        verify { client.query(ListBlockedUsersQuery(
            offset = Optional.present(page.offset),
            limit = Optional.present(page.limit)
        )) }
    }

    @Test
    fun `test block user list error`(): Unit = runBlocking {
        val page = Pageable(0,1)
        val id = "<test-id>"
        val content = BlockMock.get().copy(status = Status.ERROR.value)
        val mockData = mockk<ListBlockedUsersQuery.Data>()
        val operation = mockk<Operation<ListBlockedUsersQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.listBlockedUsers } returns content
        coEvery { client.query(any<ListBlockedUsersQuery>()).execute() } returns mockResponse

        val result = try {
            api.get(id, page)
        } catch (_: Throwable) {
            null
        }
        assertNull(result)
    }
}