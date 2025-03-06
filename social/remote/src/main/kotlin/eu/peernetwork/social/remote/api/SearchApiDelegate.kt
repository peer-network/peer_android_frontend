package eu.peernetwork.social.remote.api

import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.data.api.SearchApi
import eu.peernetwork.social.domain.model.User
import kotlinx.coroutines.flow.SharedFlow

class SearchApiDelegate : SearchApi {
    override fun friends(id: String, pageable: Pageable): SharedFlow<List<User>> {
        TODO("Not yet implemented")
    }
}
