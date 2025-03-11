package eu.peernetwork.persistence.domain.provider

import eu.peernetwork.persistence.domain.repository.PreferenceRepository

interface PreferenceProvider {
    fun preferenceRepository(): PreferenceRepository
}
