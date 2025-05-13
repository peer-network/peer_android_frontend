package eu.peernetwork.blog.remote.mock

import eu.peernetwork.blog.remote.content.CreatePostMutation
import eu.peernetwork.blog.remote.content.GetallpostsQuery
import eu.peernetwork.core.remote.model.Status

object ContentMock {
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
                media = """[{"path":"\/video\/1fc44d5b-0b92-4e67-b403-a59396eb6afc.mp4","options":{"size":"14.58 MB","duration":"00:00:05","ratio":"16:9","resolution":"3840x2160"}}]""",
                cover = "<test-cover>",
                contenttype = "<test-contenttype>",
                createdat = "2025-03-24 00:05:09.334000",
                user = CreatePostMutation.User(
                    id = "<test-userid>",
                    username = "<test-username>",
                    img = "<test-avatar>",
                    slug = 0,
                    isfollowed = false,
                    isfollowing = false
                )
            )
        )
    }

    fun contents(): GetallpostsQuery.ListPosts {
        return GetallpostsQuery.ListPosts(
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
                    media = """[{"path":"\/video\/1fc44d5b-0b92-4e67-b403-a59396eb6afc.mp4","options":{"size":"14.58 MB","duration":"00:00:05","ratio":"16:9","resolution":"3840x2160"}}]""",
                    cover = "<test-cover>",
                    contenttype = "<test-contenttype>",
                    createdat = "2025-03-24 00:05:09.334000",
                    user = GetallpostsQuery.User(
                        id = "<test-userid>",
                        username = "<test-username>",
                        img = "<test-avatar>",
                        slug = 0,
                        isfollowed = false,
                        isfollowing = false
                    )
                )
            )
        )
    }
}
