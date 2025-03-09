package eu.peernetwork.persistence.local.datasource

import android.content.SharedPreferences
import app.cash.turbine.test
import eu.peernetwork.persistence.data.datasource.ObservableDatasource
import io.mockk.CapturingSlot
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class ObservableDatasourceDelegateTest {
    private val preference = mockk<SharedPreferences>(relaxed = true)

    private val slot: CapturingSlot<SharedPreferences.OnSharedPreferenceChangeListener> = slot()

    private lateinit var observable: ObservableDatasource

    @Before
    fun setup() {
        every { preference.registerOnSharedPreferenceChangeListener(capture(slot)) } just Runs
        observable = ObservableDatasourceDelegate(preference)
    }

    @After
    fun teardown() {
        every { preference.unregisterOnSharedPreferenceChangeListener(slot.captured) } just Runs
    }

    @Test
    fun `observeBoolean emits current and updated values`() = runTest {
        val key = "<test-key>"
        every { preference.contains(key) } returns true
        every { preference.getBoolean(key, false) } returns false

        observable.observeBoolean(key).test {
            assertEquals(false, awaitItem())

            every { preference.getBoolean(key, false) } returns true
            slot.captured.onSharedPreferenceChanged(preference, key)

            assertEquals(true, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `observeLong emits current and updated values`() = runTest {
        val key = "<test-key>"
        every { preference.contains(key) } returns true
        every { preference.getLong(key, 0L) } returns 1234L

        observable.observeLong(key).test {
            assertEquals(1234L, awaitItem())

            every { preference.getLong(key, 0L) } returns 5678L
            slot.captured.onSharedPreferenceChanged(preference, key)

            assertEquals(5678L, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `observeString emits null when key is absent`() = runTest {
        val key = "username"
        every { preference.contains(key) } returns false

        observable.observeString(key).test {
            assertEquals(null, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
