package eu.peernetwork.persistence.data.repository

import app.cash.turbine.test
import eu.peernetwork.persistence.data.datasource.ObservableDatasource
import eu.peernetwork.persistence.data.datasource.PublishableDatasource
import eu.peernetwork.persistence.domain.repository.PreferenceRepository
import eu.peernetwork.persistence.domain.exception.UnsupportedTypeException
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertFailsWith

internal class PreferenceRepositoryDelegateTest {
    private val observable = mockk<ObservableDatasource>(relaxed = true)

    private val publishable = mockk<PublishableDatasource>(relaxed = true)

    private lateinit var persistence: PreferenceRepository

    @Before
    fun setup() {
        persistence = PreferenceRepositoryDelegate(observable, publishable)
    }

    @Test
    fun `get String value from SharedPreferences`() = runTest {
        val key = "<test-key>"
        val value = "<test-key>"
        every { observable.observeString(key) } returns flowOf(value)

        persistence[key, String::class.java].test {
            assertEquals(value, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `get Int value from SharedPreferences`() = runTest {
        val key = "<test-key>"
        val value = System.currentTimeMillis().toInt()
        every { observable.observeInteger(key) } returns flowOf(value)

        persistence[key, Int::class.java].test {
            assertEquals(value, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `get Boolean value from SharedPreferences`() = runTest {
        val key = "<test-key>"
        every { observable.observeBoolean(key) } returns flowOf(true)

        persistence[key, Boolean::class.java].test {
            assertEquals(true, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `get Long value from SharedPreferences`() = runTest {
        val key = "<test-key>"
        val value = System.currentTimeMillis()
        every { observable.observeLong(key) } returns flowOf(value)

        persistence.get(key, Long::class.java).test {
            assertEquals(value, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `get unsupported type throws UnsupportedTypeException`() {
        assertFailsWith<UnsupportedTypeException> {
            persistence.get("<test-key>", Float::class.java)
        }
    }

    @Test
    fun `set String value in SharedPreferences`() = runTest {
        val key = "<test-key>"
        val value = "<test-key>"
        persistence.set(key, value)

        coVerify { publishable.putString(key, value) }
    }

    @Test
    fun `set Int value in SharedPreferences`() = runTest {
        val key = "<test-key>"
        val value = 100
        persistence.set(key, value)

        coVerify { publishable.putInteger(key, value) }
    }

    @Test
    fun `set Boolean value in SharedPreferences`() = runTest {
        val key = "<test-key>"
        persistence.set(key, false)

        coVerify { publishable.putBoolean(key, false) }
    }

    @Test
    fun `set Long value in SharedPreferences`() = runTest {
        val key = "<test-key>"
        val value = System.currentTimeMillis()
        persistence.set(key, value)

        coVerify { publishable.putLong(key, value) }
    }

    @Test
    fun `remove value from SharedPreferences when set to null`() = runTest {
        val key = "<test-key>"
        persistence.set(key, null)

        coVerify { publishable.remove(key) }
    }

    @Test
    fun `set unsupported type throws UnsupportedTypeException`() = runTest {
        assertFailsWith<UnsupportedTypeException> {
            persistence.set("test_key", 3.14)
        }
    }
}
