package eu.peernetwork.social.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.api.Optional
import eu.peernetwork.core.common.interactor.SessionInteractor
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.remote.model.Status
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.social.data.api.FollowApi
import eu.peernetwork.social.remote.mock.FollowMock
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import social.social.eu.peernetwork.social.remote.ListFollowRelationsQuery
import social.social.eu.peernetwork.social.remote.ListFollowingsRelationsQuery
import social.social.eu.peernetwork.social.remote.ListPeersQuery
import social.social.eu.peernetwork.social.remote.UserFollowMutation
import social.type.ContentFilterType
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class FollowApiDelegateTest {
    private val client = mockk<ApolloClient>()

    private val sessionInteractor = mockk<SessionInteractor>()

    private val url = "http://locahost"

    private lateinit var api: FollowApi

    @Before
    fun setup() {
        coEvery { sessionInteractor.mode() } returns ContentFilterType.MYGRANDMALIKES.name
        api = FollowApiDelegate(url, object : RequestClient {
            override fun invoke(): ApolloClient = client
        }, sessionInteractor)
    }

    @Test
    fun `test follow user success`(): Unit = runBlocking {
        val id = "<test-id>"
        val content = FollowMock.follow()
        val mockData = mockk<UserFollowMutation.Data>()
        val operation = mockk<Operation<UserFollowMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()
        every { mockData.toggleUserFollowStatus } returns content
        coEvery { client.mutation(any<UserFollowMutation>()).execute() } returns mockResponse
        val result = api.follow(id)
        assertEquals(result, content.isfollowing)
        verify { client.mutation(UserFollowMutation(id)) }
    }

    @Test
    fun `test follow user error`(): Unit = runBlocking {
        val id = "<test-id>"
        val content = FollowMock.follow().copy(status = Status.ERROR.value)
        val mockData = mockk<UserFollowMutation.Data>()
        val operation = mockk<Operation<UserFollowMutation.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()
        every { mockData.toggleUserFollowStatus } returns content
        coEvery { client.mutation(any<UserFollowMutation>()).execute() } returns mockResponse
        val result = try {
            api.follow(id)
        } catch (_: Throwable) {
            null
        }
        assertNull(result)
    }

    @Test
    fun `test get user followers`(): Unit = runBlocking {
        val page = Pageable(0, 1)
        val id = "<test-id>"
        val content = FollowMock.followers()
        val mockData = mockk<ListFollowRelationsQuery.Data>()
        val operation = mockk<Operation<ListFollowRelationsQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.listFollowRelations } returns content
        coEvery { client.query(any<ListFollowRelationsQuery>()).execute() } returns mockResponse

        val result = api.followers(id, page)

        assertNotNull(result.items.first())
        assertEquals(result.items.first().id, content.affectedRows?.followers?.first()?.id)

        verify { client.query(ListFollowRelationsQuery(
            userid = id,
            limit = page.limit,
            offset = page.offset,
            contentFilterBy = Optional.present(ContentFilterType.MYGRANDMALIKES)
        )) }
    }

    @Test
    fun `test get user followers error`(): Unit = runBlocking {
        val page = Pageable(0, 1)
        val id = "<test-id>"
        val content = FollowMock.followers().copy(status = Status.ERROR.value)
        val mockData = mockk<ListFollowRelationsQuery.Data>()
        val operation = mockk<Operation<ListFollowRelationsQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.listFollowRelations } returns content
        coEvery { client.query(any<ListFollowRelationsQuery>()).execute() } returns mockResponse

        val result = try {
            api.followers(id, page)
        } catch (_: Throwable) {
            null
        }
        assertNull(result)
    }

    @Test
    fun `test get user followings`(): Unit = runBlocking {
        val page = Pageable(0, 1)
        val id = "<test-id>"
        val content = FollowMock.following()
        val mockData = mockk<ListFollowingsRelationsQuery.Data>()
        val operation = mockk<Operation<ListFollowingsRelationsQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.listFollowRelations } returns content
        coEvery { client.query(any<ListFollowingsRelationsQuery>()).execute() } returns mockResponse

        val result = api.following(id, page)

        assertNotNull(result.items.first())
        assertEquals(result.items.first().id, content.affectedRows?.following?.first()?.id)

        verify { client.query(ListFollowingsRelationsQuery(
            userid = id,
            limit = page.limit,
            offset = page.offset,
            contentFilterBy = Optional.present(ContentFilterType.MYGRANDMALIKES)
        )) }
    }

    @Test
    fun `test get user followings error`(): Unit = runBlocking {
        val page = Pageable(0, 1)
        val id = "<test-id>"
        val content = FollowMock.following().copy(status = Status.ERROR.value)
        val mockData = mockk<ListFollowingsRelationsQuery.Data>()
        val operation = mockk<Operation<ListFollowingsRelationsQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.listFollowRelations } returns content
        coEvery { client.query(any<ListFollowingsRelationsQuery>()).execute() } returns mockResponse

        val result = try {
            api.following(id, page)
        } catch (_: Throwable) {
            null
        }
        assertNull(result)
    }

    @Test
    fun `test get user friends`(): Unit = runBlocking {
        val page = Pageable(0, 1)
        val content = FollowMock.friends()
        val mockData = mockk<ListPeersQuery.Data>()
        val operation = mockk<Operation<ListPeersQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.listFriends } returns content
        coEvery { client.query(any<ListPeersQuery>()).execute() } returns mockResponse

        val result = api.friends(page)

        assertNotNull(result.items.first())
        assertEquals(result.items.first().id, content.affectedRows?.first()?.userid)

        verify { client.query(ListPeersQuery(
            limit = page.limit,
            offset = page.offset,
            contentFilterBy = Optional.present(ContentFilterType.MYGRANDMALIKES)
        )) }
    }

    @Test
    fun `test get user friends error`(): Unit = runBlocking {
        val page = Pageable(0, 1)
        val content = FollowMock.friends().copy(status = Status.ERROR.value)
        val mockData = mockk<ListPeersQuery.Data>()
        val operation = mockk<Operation<ListPeersQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.listFriends } returns content
        coEvery { client.query(any<ListPeersQuery>()).execute() } returns mockResponse

        val result = try {
            api.friends(page)
        } catch (_: Throwable) {
            null
        }
        assertNull(result)
    }
}
