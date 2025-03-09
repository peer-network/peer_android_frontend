package eu.peernetwork.user.remote.provider

import eu.peernetwork.core.common.exception.BusinessException
import eu.peernetwork.user.data.api.SettingsApi
import eu.peernetwork.user.data.provider.SettingsProvider
import javax.inject.Inject

class SettingsProviderDelegate @Inject constructor(
    private val settings: Map<String, @JvmSuppressWildcards SettingsApi<*>>
) : SettingsProvider {
    override fun get(name: String): SettingsApi<*> {
        return settings[name] ?: throw BusinessException("settings", "No Settings found for: $name")
    }
}
