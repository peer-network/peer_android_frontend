package eu.peernetwork.social.remote.mock

import eu.peernetwork.core.remote.model.Status
import social.social.eu.peernetwork.social.remote.ReferralQuery

object ReferralMock {
    fun get(): ReferralQuery.ReferralList {
        return ReferralQuery.ReferralList(
            status = Status.SUCCESS.value,
            counter = 1,
            ResponseCode = null.toString(),
            affectedRows = ReferralQuery.AffectedRows(listOf(
                ReferralQuery.IInvited(
                    id = "<test-id>",
                    username = "<test-username>",
                    slug = 1,
                    img = "<test-image>",
                    isfollowed = false,
                    isfollowing = false
                )
            )),
        )
    }
}