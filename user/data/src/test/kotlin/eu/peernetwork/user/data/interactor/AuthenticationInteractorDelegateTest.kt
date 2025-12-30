package eu.peernetwork.user.data.interactor

import com.google.gson.Gson
import eu.peernetwork.persistence.domain.observable.ObservableString
import eu.peernetwork.persistence.domain.publishable.PublishableString
import eu.peernetwork.persistence.domain.retrievable.RetrievableString
import eu.peernetwork.user.domain.interactor.AuthenticationInteractor
import eu.peernetwork.user.domain.model.Account
import eu.peernetwork.user.domain.model.Overview
import eu.peernetwork.user.domain.repository.AccountRepository
import eu.peernetwork.user.domain.repository.AuthenticationRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

internal class AuthenticationInteractorDelegateTest {

    private val gson = Gson()

    private val observable = mockk<ObservableString>(relaxed = true)

    private val retrievable = mockk<RetrievableString>(relaxed = true)

    private val publisher = mockk<PublishableString>(relaxed = true)

    private val accountRepository = mockk<AccountRepository>()

    private val authenticationRepository = mockk<AuthenticationRepository>()

    private lateinit var interactor: AuthenticationInteractor

    @Before
    fun setup() {
        interactor = AuthenticationInteractorDelegate(
            gson,
            publisher,
            observable,
            retrievable,
            accountRepository,
            authenticationRepository
        )
    }

    @Test
    fun `test get authenticated user id`(): Unit = runBlocking {
        val user = "<test-user>"
        every { retrievable(any()) } returns null
        coEvery { authenticationRepository.authenticated() } returns user
        val result = interactor.get()
        assertEquals(result, user)
    }

    @Test
    fun `test get cached authenticated user id`(): Unit = runBlocking {
        val user = "<test-user>"
        every { retrievable(any()) } returns user
        val result = interactor.get()
        assertEquals(result, user)
    }

    @Test
    fun `test get authenticated account`(): Unit = runBlocking {
        val mockData = Account(
            id = "<test-id>",
            slug = 0,
            username = "<test-username>",
            bio = "<test-bio>",
            imageUrl = "<test-image-url>",
            followed = false,
            following = false,
            overview = Overview(
                posts = 0,
                peers = 0,
                followed = 0,
                followers = 0
            )
        )
        coEvery { accountRepository.get(any(), true) } returns mockData
        coEvery { authenticationRepository.authenticated() } returns mockData.id

        val result = interactor.getCurrentAccount(true)

        assertEquals(result, mockData)
        coVerify { publisher(any(), any()) }
    }

    @Test
    fun `test observe authenticated user`(): Unit = runBlocking {
        val mockData = Account(
            id = "<test-id>",
            slug = 0,
            username = "<test-username>",
            bio = "<test-bio>",
            imageUrl = "<test-image-url>",
            followed = false,
            following = false,
            overview = Overview(
                posts = 0,
                peers = 0,
                followed = 0,
                followers = 0
            )
        )
        every { observable(any()) } returns flowOf(gson.toJson(mockData))
        coEvery { accountRepository.get(any()) } returns mockData
        coEvery { authenticationRepository.authenticated() } returns mockData.id

        val result = interactor.observeAccount().first()

        assertEquals(result, mockData)
    }

    @Test
    fun `test logout user`(): Unit = runBlocking {
        coEvery { authenticationRepository.logout() } returns Unit

        interactor.logout()

        coVerify { publisher(any(), null) }
        coVerify { authenticationRepository.logout() }
    }
}
