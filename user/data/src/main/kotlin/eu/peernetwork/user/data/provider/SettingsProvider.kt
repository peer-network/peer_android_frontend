package eu.peernetwork.user.data.provider

import eu.peernetwork.user.data.api.SettingsApi

interface SettingsProvider {
    fun get(name: String): SettingsApi<*>
}
