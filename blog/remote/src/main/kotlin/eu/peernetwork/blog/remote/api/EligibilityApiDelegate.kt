package eu.peernetwork.blog.remote.api

import eu.peernetwork.blog.data.api.EligibilityApi
import eu.peernetwork.blog.domain.model.Eligibility
import eu.peernetwork.blog.remote.content.PostEligibilityQuery
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.core.remote.extension.executeOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import javax.inject.Inject

class EligibilityApiDelegate @Inject constructor(
    private val client: RequestClient
) : EligibilityApi {
    override suspend fun fetch(): Eligibility {
        val response = client().query(PostEligibilityQuery()).executeOrThrow()
        val eligibilityData = response.getOrThrow().postEligibility
        return Eligibility(token = eligibilityData.eligibilityToken)
    }
}