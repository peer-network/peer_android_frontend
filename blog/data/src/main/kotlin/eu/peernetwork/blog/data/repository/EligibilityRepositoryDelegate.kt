package eu.peernetwork.blog.data.repository

import com.google.gson.Gson
import eu.peernetwork.blog.data.api.EligibilityApi
import eu.peernetwork.blog.domain.repository.EligibilityRepository
import eu.peernetwork.persistence.domain.publishable.PublishableString
import eu.peernetwork.persistence.domain.retrievable.RetrievableString
import javax.inject.Inject

class EligibilityRepositoryDelegate @Inject constructor(
    private val gson: Gson,
    private val api: EligibilityApi,
    private val publisher: PublishableString,
    private val retrievableString: RetrievableString,
) : EligibilityRepository {

    override fun get(): String? = retrievableString(TAG)?.run {
        gson.fromJson(this, String::class.java)
    }

    private suspend fun onEligibilityChanged(token: String?) {
        publisher(TAG, token)
    }

    override suspend fun refresh(): String {
        val eligibility = api.fetch()
        onEligibilityChanged(eligibility.token)
        return eligibility.token
    }

    override suspend fun clear() {
        onEligibilityChanged(null)
    }

    internal companion object {
        val TAG: String = EligibilityRepositoryDelegate::class.java.name
    }
}