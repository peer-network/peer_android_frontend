package eu.peernetwork.blog.ui.mock

import androidx.compose.ui.text.buildAnnotatedString
import eu.peernetwork.blog.ui.model.UiAuthor
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.model.UiPostType
import eu.peernetwork.blog.ui.model.UiStatus
import eu.peernetwork.blog.ui.model.UiTimer
import io.mockk.mockk

object MockContent {
    fun post(): UiPost {
        return UiPost(
            id = "<test-id>",
            title = buildAnnotatedString { append("<test-title>") },
            author = UiAuthor(
                id = "<test-id>",
                username = "<test-username>",
                slug = 0,
                imageUrl = "http://localhost",
                followed = false,
                following = false,
                isAccessible = true,
                status = UiStatus.VISIBLE
            ),
            type = UiPostType.IMAGE,
            createdAt = System.currentTimeMillis(),
            description = buildAnnotatedString { append("<test-description>") },
            likes = 0,
            dislikes = 0,
            isLiked = false,
            isDisliked = false,
            comment = 0,
            views = 0,
            isViewed = false,
            asset = mockk(),
            url = "http://localhost/photo.jpg",
            time = UiTimer.Now,
            isAccessible = true,
            reported = false,
            status = UiStatus.HIDDEN
        )
    }
}