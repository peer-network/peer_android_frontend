package eu.peernetwork.social.remote.mapper

import eu.peernetwork.social.domain.model.Member
import eu.peernetwork.social.domain.model.Post
import social.social.eu.peernetwork.social.remote.GetallpostsQuery

fun GetallpostsQuery.AffectedRow.mapToDomain(url: String): Post {
    return Post(
        id = id,
        title = title,
        type = contenttype,
        description = mediadescription,
        author = Member(
            id = user.id,
            slug = user.slug.toString(),
            username = user.username!!,
            imageUrl = "$url${user.img}"
        )
    )
}
