package eu.peernetwork.social.remote.mock

import eu.peernetwork.core.remote.model.Status
import social.social.eu.peernetwork.social.remote.ListBlockedUsersQuery
import social.social.eu.peernetwork.social.remote.ToggleBlockUserStatusMutation

object BlockMock {
    fun block(): ToggleBlockUserStatusMutation.ToggleBlockUserStatus {
        return ToggleBlockUserStatusMutation.ToggleBlockUserStatus(
            status = Status.SUCCESS.value,
            ResponseCode = null
        )
    }

    fun get(): ListBlockedUsersQuery.ListBlockedUsers {
        return ListBlockedUsersQuery.ListBlockedUsers(
            status = Status.SUCCESS.value,
            ResponseCode = null,
            counter = 1,
            affectedRows = ListBlockedUsersQuery.AffectedRows(listOf(
                ListBlockedUsersQuery.IBlocked(
                    userid = "<test-id>",
                    img = "<test-image>",
                    username = "<test-username>",
                    slug = 1
                )
            ))
        )
    }
}