package eu.peernetwork.persistence.data.repository

import app.cash.turbine.test
import eu.peernetwork.persistence.data.datasource.ObservableDatasource
import eu.peernetwork.persistence.data.datasource.PublishableDatasource
import eu.peernetwork.persistence.data.datasource.RetrievableDatasource
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

    private val retrievable = mockk<RetrievableDatasource>(relaxed = true)

    private lateinit var persistence: PreferenceRepository

    @Before
    fun setup() {
        persistence = PreferenceRepositoryDelegate(observable, publishable, retrievable)
    }

    @Test
    fun `get string value from SharedPreferences`() = runTest {
        val key = "<test-key>"
        val value = "<test-value>"
        every { retrievable.getString(key) } returns value

        val result = persistence.get(key, String::class.java)

        assertEquals(value, result)
    }

    @Test
    fun `get long value from SharedPreferences`() = runTest {
        val key = "<test-key>"
        val value = System.currentTimeMillis()
        every { retrievable.getLong(key) } returns value

        val result = persistence.get(key, Long::class.java)

        assertEquals(value, result)
    }

    @Test
    fun `get int value from SharedPreferences`() = runTest {
        val key = "<test-key>"
        val value = System.currentTimeMillis().toInt()
        every { retrievable.getInteger(key) } returns value

        val result = persistence.get(key, Int::class.java)

        assertEquals(value, result)
    }

    @Test
    fun `get boolean value from SharedPreferences`() = runTest {
        val key = "<test-key>"
        val value = true
        every { retrievable.getBoolean(key) } returns value

        val result = persistence.get(key, Boolean::class.java)

        assertEquals(value, result)
    }

    @Test
    fun `observe string value from SharedPreferences`() = runTest {
        val key = "<test-key>"
        val value = "<test-value>"
        every { observable.observeString(key) } returns flowOf(value)

        persistence.observe(key, String::class.java).test {
            assertEquals(value, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `observe int value from SharedPreferences`() = runTest {
        val key = "<test-key>"
        val value = System.currentTimeMillis().toInt()
        every { observable.observeInteger(key) } returns flowOf(value)

        persistence.observe(key, Int::class.java).test {
            assertEquals(value, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `observe boolean value from SharedPreferences`() = runTest {
        val key = "<test-key>"
        every { observable.observeBoolean(key) } returns flowOf(true)

        persistence.observe(key, Boolean::class.java).test {
            assertEquals(true, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `observe long value from SharedPreferences`() = runTest {
        val key = "<test-key>"
        val value = System.currentTimeMillis()
        every { observable.observeLong(key) } returns flowOf(value)

        persistence.observe(key, Long::class.java).test {
            assertEquals(value, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `observe unsupported type throws UnsupportedTypeException`() {
        assertFailsWith<UnsupportedTypeException> {
            persistence.observe("<test-key>", Float::class.java)
        }
    }

    @Test
    fun `set string value in SharedPreferences`() = runTest {
        val key = "<test-key>"
        val value = "<test-value>"
        persistence.set(key, value)

        coVerify { publishable.putString(key, value) }
    }

    @Test
    fun `set int value in SharedPreferences`() = runTest {
        val key = "<test-key>"
        val value = 100
        persistence.set(key, value)

        coVerify { publishable.putInteger(key, value) }
    }

    @Test
    fun `set boolean value in SharedPreferences`() = runTest {
        val key = "<test-key>"
        persistence.set(key, false)

        coVerify { publishable.putBoolean(key, false) }
    }

    @Test
    fun `set long value in SharedPreferences`() = runTest {
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
