package eu.peernetwork.social.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.api.Optional
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.core.remote.model.Status
import eu.peernetwork.social.data.api.ReferralApi
import eu.peernetwork.social.remote.mock.ReferralMock
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import social.social.eu.peernetwork.social.remote.ReferralQuery
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class ReferralApiDelegateTest {
    private val client = mockk<ApolloClient>()

    private val url = "http://localhost"

    private lateinit var api: ReferralApi

    @Before
    fun setup() {
        api = ReferralApiDelegate(url, object : RequestClient {
            override fun invoke(): ApolloClient = client
        })
    }

    @Test
    fun `test referral list success`(): Unit = runBlocking {
        val page = Pageable(0,1)
        val id = "<test-id>"
        val content = ReferralMock.get()
        val mockData = mockk<ReferralQuery.Data>()
        val operation = mockk<Operation<ReferralQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.referralList } returns content
        coEvery { client.query(any<ReferralQuery>()).execute() } returns mockResponse

        val result = api.get(id, page)

        assertNotNull(result.items.first())
        assertEquals(result.items.first().id, content.affectedRows.iInvited.first().id)

        verify { client.query(ReferralQuery(
            limit = Optional.present(page.limit),
            offset = Optional.present(page.offset)
        )) }
    }

    @Test
    fun `test referral list error`(): Unit = runBlocking {
        val page = Pageable(0, 1)
        val id = "<test-id>"
        val content = ReferralMock.get().copy(status = Status.ERROR.value)
        val mockData = mockk<ReferralQuery.Data>()
        val operation = mockk<Operation<ReferralQuery.Data>>(relaxed = true)
        val mockResponse = ApolloResponse.Builder(
            operation,
            UUID.randomUUID(),
            mockData
        ).build()

        every { mockData.referralList } returns content
        coEvery { client.query(any<ReferralQuery>()).execute() } returns mockResponse

        val result = try {
            api.get(id, page)
        } catch (_: Throwable) {
            null
        }
        assertNull(result)
    }
}