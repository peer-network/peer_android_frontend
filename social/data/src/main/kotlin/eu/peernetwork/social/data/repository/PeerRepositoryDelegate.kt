package eu.peernetwork.social.data.repository

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.domain.model.Member
import eu.peernetwork.social.domain.repository.PeerRepository

class PeerRepositoryDelegate : PeerRepository {
    override suspend fun friends(
        id: String,
        pageable: Pageable
    ): Page<Member> {
        TODO("Not yet implemented")
    }
}
