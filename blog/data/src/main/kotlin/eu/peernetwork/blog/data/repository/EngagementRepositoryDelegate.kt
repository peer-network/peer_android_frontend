package eu.peernetwork.blog.data.repository

import eu.peernetwork.blog.data.api.EngagementApi
import eu.peernetwork.blog.domain.model.Author
import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.domain.repository.EngagementRepository
import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import javax.inject.Inject

class EngagementRepositoryDelegate @Inject constructor(
    private val api: EngagementApi
) : EngagementRepository {
    override suspend fun post(id: String, engagement: Engagement.Content) {
        return api.post(id, engagement)
    }

    override suspend fun comment(id: String, engagement: Engagement.Comment) {
        return api.comment(id, engagement)
    }

    override suspend fun reactors(
        id: String,
        engagement: Engagement.Content,
        page: Pageable
    ): Page<Author> {
        return api.reactors(id, engagement, page)
    }
}
