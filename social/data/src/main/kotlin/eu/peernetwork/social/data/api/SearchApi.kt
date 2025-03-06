package eu.peernetwork.social.data.api

import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.domain.model.User
import kotlinx.coroutines.flow.SharedFlow

interface SearchApi {
    fun friends(id: String, pageable: Pageable): SharedFlow<List<User>>
}
