package eu.peernetwork.social.remote.api

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.data.api.PeerApi
import eu.peernetwork.social.domain.model.Member

class PeerApiDelegate : PeerApi {
    override suspend fun friends(id: String, pageable: Pageable): Page<Member> {
        TODO("Not yet implemented")
    }
}
