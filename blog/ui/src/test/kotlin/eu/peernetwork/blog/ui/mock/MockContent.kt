package eu.peernetwork.blog.ui.mock

import androidx.compose.ui.text.buildAnnotatedString
import eu.peernetwork.blog.ui.model.v2.UiAuthor
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.model.UiVideo
import io.mockk.mockk

object MockContent {
    fun post(): UiPost {
        return UiPost(
            id = "<test-id>",
            title = buildAnnotatedString { append("<test-title>") },
            media = mockk(),
            author = UiAuthor(
                id = "<test-id>",
                username = "<test-username>",
                slug = 0,
                imageUrl = "http://localhost",
                followed = false,
                following = false
            ),
            type = UiPost.Type.IMAGE,
            time = "<test-time>",
            createdAt = System.currentTimeMillis(),
            description = buildAnnotatedString { append("<test-description>") },
            likes = 0,
            dislikes = 0,
            isLiked = false,
            isDisliked = false,
            comment = 0,
            views = 0,
            isViewed = false,
            aspectRatio = 0.1f,
            url = "http://localhost",
        )
    }

    fun video(): UiVideo {
        return UiVideo(
            id = "<test-id>",
            title = buildAnnotatedString { append("<test-title>") },
            media = "<test-media>",
            author = UiAuthor(
                id = "<test-id>",
                username = "<test-username>",
                slug = 0,
                imageUrl = "http://localhost",
                followed = false,
                following = false
            ),
            time = "<test-time>",
            createdAt = System.currentTimeMillis(),
            description = buildAnnotatedString { append("<test-description>") },
            likes = 0,
            dislikes = 0,
            isLiked = false,
            isDisliked = false,
            comment = 0,
            views = 0,
            isViewed = false,
            aspectRatio = 0.1f,
            resolution = null,
            url = "http://localhost",
        )
    }
}