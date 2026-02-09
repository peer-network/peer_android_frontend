package eu.peernetwork.user.data.repository

import eu.peernetwork.user.data.api.PreferenceApi
import eu.peernetwork.user.domain.model.Preference
import eu.peernetwork.user.domain.repository.PreferenceRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

internal class PreferenceRepositoryDelegateTest {
    private val api = mockk<PreferenceApi>()

    private lateinit var repository: PreferenceRepository

    @Before
    fun setup() {
        repository = PreferenceRepositoryDelegate(api)
    }

    @Test
    fun `test register user`(): Unit = runBlocking {
        val mockResponse = mockk<Preference>()
        coEvery { api.get() } returns mockResponse

        val result = repository.get()

        coVerify { api.get() }
        assertEquals(mockResponse, result)
    }
}
