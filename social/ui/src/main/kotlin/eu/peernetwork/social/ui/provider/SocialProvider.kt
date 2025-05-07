package eu.peernetwork.social.ui.provider

import eu.peernetwork.core.common.provider.CoreProvider
import eu.peernetwork.social.domain.interactor.SearchInteractor

interface SocialProvider : CoreProvider, RepositoryProvider {
    fun searchInteractor(): SearchInteractor
}
