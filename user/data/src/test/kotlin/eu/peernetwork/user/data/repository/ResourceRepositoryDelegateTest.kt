package eu.peernetwork.user.data.repository

import eu.peernetwork.user.data.api.ResourceApi
import eu.peernetwork.user.domain.repository.ResourceRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ResourceRepositoryDelegateTest {
    private val api = mockk<ResourceApi>()

    private lateinit var repository: ResourceRepository

    @Before
    fun setup() {
        repository = ResourceRepositoryDelegate(api)
    }

    @Test
    fun `test get string resource`(): Unit = runBlocking {
        val path = "<test-path>"
        val mockData = "<test-data>"
        coEvery { api.string(any()) } returns mockData
        val result = repository.string(path)
        assertEquals(result, mockData)
    }
}
