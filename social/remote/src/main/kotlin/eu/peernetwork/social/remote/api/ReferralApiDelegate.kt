package eu.peernetwork.social.remote.api

import com.apollographql.apollo3.api.Optional
import eu.peernetwork.core.common.model.Page
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.core.remote.extension.assertOrThrow
import eu.peernetwork.core.remote.extension.getOrThrow
import eu.peernetwork.social.data.api.ReferralApi
import eu.peernetwork.social.domain.model.Referral
import social.social.eu.peernetwork.social.remote.ReferralQuery
import javax.inject.Inject
import javax.inject.Named

class ReferralApiDelegate @Inject constructor(
    @Named("mediaUrl") private val url: String,
    private val client: RequestClient
) : ReferralApi {

    override suspend fun get(userId: String, pageable: Pageable): Page<Referral> {
        val response = client().query(ReferralQuery(offset = Optional.present(pageable.offset), limit = Optional.present(pageable.limit))).execute()
        val data = response.getOrThrow().referralList
        response.assertOrThrow(data.status, data.ResponseCode)
        val referrals = data.affectedRows.iInvited.map {
            Referral(
                id = userId,
                username = it.username.toString(),
                slug = it.slug.toString(),
                img = "$url/${it.img!!}".removeSuffix("/")
            )
        }

        return Page(
            items = referrals,
            offset = pageable.offset,
            count = data.counter
        )
    }
}
