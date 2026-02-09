package eu.peernetwork.ads.data.repository

import eu.peernetwork.ads.data.api.ContentApi
import eu.peernetwork.ads.domain.model.Content
import eu.peernetwork.ads.domain.repository.ContentRepository
import javax.inject.Inject

class ContentRepositoryDelegate @Inject constructor(
    private val api: ContentApi
) : ContentRepository {
    override suspend fun get(id: String): Content {
        return api.get(id)
    }
}
