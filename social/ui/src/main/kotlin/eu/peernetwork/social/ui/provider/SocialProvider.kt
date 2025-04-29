package eu.peernetwork.social.ui.provider

import eu.peernetwork.social.domain.repository.FollowRepository

interface SocialProvider {
    fun followRepository(): FollowRepository
}
