package eu.peernetwork.social.ui.provider

import eu.peernetwork.core.common.provider.CoreProvider
import eu.peernetwork.social.domain.interactor.SearchInteractor
import eu.peernetwork.social.domain.repository.FollowRepository

interface SocialProvider : CoreProvider {
    fun followRepository(): FollowRepository

    fun searchInteractor(): SearchInteractor
}
