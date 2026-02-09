package eu.peernetwork.user.data.repository

import com.google.gson.Gson
import eu.peernetwork.persistence.domain.observable.ObservableString
import eu.peernetwork.persistence.domain.publishable.PublishableString
import eu.peernetwork.persistence.domain.retrievable.RetrievableString
import eu.peernetwork.user.data.api.AuthenticationApi
import eu.peernetwork.user.data.api.TokenApi
import eu.peernetwork.user.data.mock.TokenMock
import eu.peernetwork.user.data.repository.TokenRepositoryDelegate.Companion.TAG
import eu.peernetwork.user.domain.repository.TokenRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class TokenRepositoryDelegateTest {
    private val gson = Gson()

    private val publisher = mockk<PublishableString>()

    private val api = mockk<TokenApi>()

    private val observer = mockk<ObservableString>()

    private val retrievable = mockk<RetrievableString>()

    private lateinit var repository: TokenRepository

    private lateinit var listener: AuthenticationApi.Listener

    @Before
    fun setup() {
        TokenRepositoryDelegate(gson, api, publisher, observer, retrievable).also {
            repository = it
            listener = it
        }
    }

    @Test
    fun `test get token`() = runBlocking {
        val token = TokenMock.token()
        coEvery { retrievable(any()) } returns gson.toJson(token)
        val result = repository.get()
        assertEquals(result, token)
        coVerify { retrievable(any()) }
    }

    @Test
    fun `test on authentication changed`() = runBlocking {
        val token = TokenMock.token()

        coEvery { publisher(any(), any()) } returns Unit

        listener.onAuthenticationChanged(token)

        coVerify { publisher(any(), gson.toJson(token)) }
    }

    @Test
    fun `test refresh token`() = runBlocking {
        val token = TokenMock.token()
        coEvery { api.refresh(any()) } returns token
        coEvery { publisher(any(), any()) } returns Unit

        repository.refresh("old_token")

        coVerify { api.refresh("old_token") }
        coVerify { publisher(TAG, gson.toJson(token)) }
    }

    @Test
    fun `test observe emits token updates`() = runBlocking {
        val token = TokenMock.token()

        every { observer(any()) } returns flowOf(gson.toJson(token))

        val flow = repository.observe()
        val emittedToken = flow.firstOrNull()

        assertEquals(token, emittedToken)
    }
}
