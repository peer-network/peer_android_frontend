package eu.peernetwork.app.provider

import eu.peernetwork.app.service.LaunchService
import eu.peernetwork.persistence.domain.provider.PreferenceProvider

interface ResourceProvider : PreferenceProvider {
    fun resourceLoader(): LaunchService
}
