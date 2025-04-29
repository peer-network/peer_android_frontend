package eu.peernetwork.social.data.repository

import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.social.data.api.SearchApi
import eu.peernetwork.social.domain.model.Member
import eu.peernetwork.social.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryDelegate @Inject constructor(
    private val api: SearchApi
) : SearchRepository {
    override suspend fun friends(
        id: String,
        pageable: Pageable
    ): Page<Member> {
        return api.friends(id, pageable)
    }
}
