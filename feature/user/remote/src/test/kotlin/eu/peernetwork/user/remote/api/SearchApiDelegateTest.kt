package eu.peernetwork.user.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import eu.peernetwork.core.common.interactor.SessionInteractor
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.remote.model.Status
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.user.data.api.SearchApi
import eu.peernetwork.user.remote.mock.SearchMock
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import `protected`.eu.peernetwork.user.remote.SearchuserQuery
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class SearchApiDelegateTest {
    private val url = "http://locahost"

    private val client = mockk<ApolloClient>()

    private val sessionInteractor = mockk<SessionInteractor>(relaxed = true)

    private lateinit var api: SearchApi

    @Before
    fun setup() {
        api = SearchApiDelegate(url, object : RequestClient {
            override fun invoke(): ApolloClient = client
        }, sessionInteractor)
    }

    @Test
    fun `test search user by username success`(): Unit = runBlocking {
        val mockModel = SearchMock.users()
        val mockData = mockk<SearchuserQuery.Data>()
        val operation = mockk<Operation<SearchuserQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.searchUser } returns mockModel
        coEvery { client.query(any<SearchuserQuery>()).execute() } returns mockResponse

        val result = api.findByUsername("<test-username>", Pageable(0, 1))

        assertEquals(result.items.first().id, mockModel.affectedRows?.first()?.id)
        assertEquals(result.items.first().slug, mockModel.affectedRows?.first()?.slug)
    }

    @Test
    fun `test search user by username error`(): Unit = runBlocking {
        val mockModel = SearchMock.users().copy(status = Status.ERROR.value)
        val mockData = mockk<SearchuserQuery.Data>()
        val operation = mockk<Operation<SearchuserQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.searchUser } returns mockModel
        coEvery { client.query(any<SearchuserQuery>()).execute() } returns mockResponse

        val result = try {
            api.findByUsername("<test-username>", Pageable(0, 1))
        } catch (_: Throwable) {
            null
        }
        assertNull(result)
    }
}
