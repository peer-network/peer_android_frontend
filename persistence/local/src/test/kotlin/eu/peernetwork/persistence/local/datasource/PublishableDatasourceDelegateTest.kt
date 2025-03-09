package eu.peernetwork.persistence.local.datasource

import android.content.SharedPreferences
import eu.peernetwork.persistence.data.datasource.PublishableDatasource
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

internal class PublishableDatasourceDelegateTest {
    private val editor = mockk<SharedPreferences.Editor>(relaxed = true)

    private val preference = mockk<SharedPreferences>(relaxed = true)

    private lateinit var publishable: PublishableDatasource

    @Before
    fun setup() {
        every { preference.edit() } returns editor
        publishable = PublishableDatasourceDelegate(preference)
    }

    @Test
    fun `putLong stores value in SharedPreferences`() = runTest {
        publishable.putLong("test_key", 123L)

        verify { editor.putLong("test_key", 123L) }
        verify { editor.commit() }
    }

    @Test
    fun `putBoolean stores value in SharedPreferences`() = runTest {
        publishable.putBoolean("test_key", true)

        verify { editor.putBoolean("test_key", true) }
        verify { editor.commit() }
    }

    @Test
    fun `putInteger stores value in SharedPreferences`() = runTest {
        publishable.putInteger("test_key", 42)

        verify { editor.putInt("test_key", 42) }
        verify { editor.commit() }
    }

    @Test
    fun `putString stores value in SharedPreferences`() = runTest {
        publishable.putString("test_key", "Hello World")

        verify { editor.putString("test_key", "Hello World") }
        verify { editor.commit() }
    }

    @Test
    fun `remove deletes key from SharedPreferences`() = runTest {
        publishable.remove("test_key")

        verify { editor.remove("test_key") }
        verify { editor.commit() }
    }
}
