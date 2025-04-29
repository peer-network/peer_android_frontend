package eu.peernetwork.social.remote.api

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.data.api.SearchApi
import eu.peernetwork.social.domain.model.Member
import javax.inject.Inject

class SearchApiDelegate @Inject constructor() : SearchApi {
    override suspend fun friends(id: String, pageable: Pageable): Page<Member> {
        TODO("Not yet implemented")
    }
}
