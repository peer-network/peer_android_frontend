package eu.peernetwork.blog.data.repository

import eu.peernetwork.blog.data.api.EligibilityApi
import eu.peernetwork.blog.domain.repository.EligibilityRepository
import javax.inject.Inject

class EligibilityRepositoryDelegate @Inject constructor(
    private val api: EligibilityApi
) : EligibilityRepository {
    override suspend fun get(): String {
        return api.fetch().token
    }
}
