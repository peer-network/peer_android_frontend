package eu.peernetwork.blog.data.api

import eu.peernetwork.blog.domain.model.Eligibility

interface EligibilityApi {
    suspend fun fetch(): Eligibility
}