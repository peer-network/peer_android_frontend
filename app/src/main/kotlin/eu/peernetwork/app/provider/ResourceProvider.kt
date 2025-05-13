package eu.peernetwork.app.provider

import eu.peernetwork.app.service.ResourceLoader
import eu.peernetwork.persistence.domain.provider.PreferenceProvider

interface ResourceProvider : PreferenceProvider {
    fun resourceLoader(): ResourceLoader
}
