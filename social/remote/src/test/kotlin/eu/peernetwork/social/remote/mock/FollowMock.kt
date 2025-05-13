package eu.peernetwork.social.remote.mock

import eu.peernetwork.core.remote.model.Status
import social.social.eu.peernetwork.social.remote.ListFollowRelationsQuery
import social.social.eu.peernetwork.social.remote.ListFollowingsRelationsQuery
import social.social.eu.peernetwork.social.remote.ListPeersQuery
import social.social.eu.peernetwork.social.remote.UserFollowMutation

object FollowMock {
    fun follow(): UserFollowMutation.ToggleUserFollowStatus {
        return UserFollowMutation.ToggleUserFollowStatus(
            status = Status.SUCCESS.value,
            ResponseCode = null,
            isfollowing = true
        )
    }

    fun followers(): ListFollowRelationsQuery.ListFollowRelations {
        return ListFollowRelationsQuery.ListFollowRelations(
            status = Status.SUCCESS.value,
            ResponseCode = null,
            counter = 1,
            affectedRows = ListFollowRelationsQuery.AffectedRows(listOf(
                ListFollowRelationsQuery.Follower(
                    id = "<test-id>",
                    username = "<test-username>",
                    slug = 1,
                    img = "<test-image>",
                    isfollowing = false,
                    isfollowed = false
                )
            ))
        )
    }

    fun following(): ListFollowingsRelationsQuery.ListFollowRelations {
        return ListFollowingsRelationsQuery.ListFollowRelations(
            status = Status.SUCCESS.value,
            ResponseCode = null,
            counter = 1,
            affectedRows = ListFollowingsRelationsQuery.AffectedRows(listOf(
                ListFollowingsRelationsQuery.Following(
                    id = "<test-id>",
                    username = "<test-username>",
                    slug = 1,
                    img = "<test-image>",
                    isfollowing = false,
                    isfollowed = false
                )
            ))
        )
    }

    fun friends(): ListPeersQuery.ListFriends {
        return ListPeersQuery.ListFriends(
            status = Status.SUCCESS.value,
            ResponseCode = null,
            counter = 1,
            affectedRows = listOf(
                ListPeersQuery.AffectedRow(
                    userid = "<test-id>",
                    username = "<test-username>",
                    slug = 1,
                    img = "<test-image>",
                    biography = "<test-biography>",
                    updatedat = "<test-updated-at>"
                )
            )
        )
    }
}
