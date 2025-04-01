package eu.peernetwork.blog.remote.mock

import eu.peernetwork.blog.remote.content.CreatePostMutation
import eu.peernetwork.blog.remote.content.GetallpostsQuery
import eu.peernetwork.core.remote.model.Status

object ContentMocker {
    fun content(): CreatePostMutation.CreatePost {
        return CreatePostMutation.CreatePost(
            status = Status.SUCCESS.value,
            ResponseCode = null,
            affectedRows = CreatePostMutation.AffectedRows(
                id = "<test-id>",
                title = "<test-title>",
                mediadescription = "<test-mediadescription>",
                isliked = true,
                isviewed = false,
                isdisliked = false,
                amountlikes = 100,
                amountdislikes = 5,
                amountviews = 1000,
                amountcomments = 50,
                media = "<test-media>",
                cover = "<test-cover>",
                contenttype = "<test-contenttype>",
                createdat = "<test-createdat>"
            )
        )
    }

    fun contents(): GetallpostsQuery.Getallposts {
        return GetallpostsQuery.Getallposts(
            status = Status.SUCCESS.value,
            ResponseCode = null,
            counter = 1,
            affectedRows = listOf(
                GetallpostsQuery.AffectedRow(
                    id = "<test-id>",
                    title = "<test-title>",
                    mediadescription = "<test-mediadescription>",
                    isliked = true,
                    isviewed = false,
                    isdisliked = false,
                    amountlikes = 100,
                    amountdislikes = 5,
                    amountviews = 1000,
                    amountcomments = 50,
                    media = "<test-media>",
                    cover = "<test-cover>",
                    contenttype = "<test-contenttype>",
                    createdat = "<test-createdat>"
                )
            )
        )
    }
}
