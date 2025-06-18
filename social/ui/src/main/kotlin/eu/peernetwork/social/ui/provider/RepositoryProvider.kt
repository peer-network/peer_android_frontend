package eu.peernetwork.social.ui.provider

import eu.peernetwork.core.common.provider.CoreProvider
import eu.peernetwork.social.domain.repository.FollowRepository
import eu.peernetwork.social.domain.repository.ReferralRepository
import eu.peernetwork.social.domain.repository.SearchRepository

interface RepositoryProvider : CoreProvider {
    fun followRepository(): FollowRepository

    fun socialSearchRepository(): SearchRepository

    fun referralRepository(): ReferralRepository
}
