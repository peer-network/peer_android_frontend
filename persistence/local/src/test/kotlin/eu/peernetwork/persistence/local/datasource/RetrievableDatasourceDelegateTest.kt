package eu.peernetwork.persistence.local.datasource

import android.content.SharedPreferences
import eu.peernetwork.persistence.data.datasource.RetrievableDatasource
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

internal class RetrievableDatasourceDelegateTest {
    private val preference = mockk<SharedPreferences>(relaxed = true)

    private lateinit var datasource: RetrievableDatasource

    @Before
    fun setup() {
        datasource = RetrievableDatasourceDelegate(preference)
    }

    @Test
    fun `retrieve long stores value in SharedPreferences`() = runTest {
        val value = System.currentTimeMillis()
        every { preference.contains(any()) } returns true
        every { preference.getLong(any(), any()) } returns value

        val result = datasource.getLong("<test-key>")

        assertEquals(value, result)
    }

    @Test
    fun `retrieve boolean stores value in SharedPreferences`() = runTest {
        val value = true
        every { preference.contains(any()) } returns true
        every { preference.getBoolean(any(), any()) } returns value

        val result = datasource.getBoolean("<test-key>")

        assertEquals(value, result)
    }

    @Test
    fun `retrieve integer stores value in SharedPreferences`() = runTest {
        val value = 1
        every { preference.contains(any()) } returns true
        every { preference.getInt(any(), any()) } returns value

        val result = datasource.getInteger("<test-key>")

        assertEquals(value, result)
    }

    @Test
    fun `retrieve string stores value in SharedPreferences`() = runTest {
        val value = "<test-string>"
        every { preference.contains(any()) } returns true
        every { preference.getString(any(), any()) } returns value

        val result = datasource.getString("<test-key>")

        assertEquals(value, result)
    }

    @Test
    fun `retrieve non existing value in SharedPreferences`() = runTest {
        every { preference.contains(any()) } returns false
        every { preference.getLong(any(), any()) } returns 1

        val result = datasource.getLong("<test-key>")

        assertEquals(result, null)
    }
}
