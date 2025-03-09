package eu.peernetwork.user.remote.provider

import eu.peernetwork.core.common.exception.BusinessException
import eu.peernetwork.user.data.api.SettingsApi
import eu.peernetwork.user.data.provider.SettingsProvider
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

internal class SettingsProviderDelegateTest {
    private lateinit var provider: SettingsProvider

    private val usernameApi = mockk<SettingsApi<*>>(relaxed = true)
    private val emailApi = mockk<SettingsApi<*>>(relaxed = true)

    private val settingsMap = mapOf(
        "<test-username>" to usernameApi,
        "<test-email>" to emailApi
    )

    @Before
    fun setup() {
        provider = SettingsProviderDelegate(settingsMap)
    }

    @Test
    fun `should return correct SettingsApi when name exists`() {
        val result = provider.get("<test-username>")
        assertEquals(usernameApi, result)

        val result2 = provider.get("<test-email>")
        assertEquals(emailApi, result2)
    }

    @Test
    fun `should throw BusinessException when name does not exist`() {
        val exception = assertThrows(BusinessException::class.java) {
            provider.get("unknown")
        }
        assertEquals("No Settings found for: unknown", exception.message)
    }
}
