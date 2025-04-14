package eu.peernetwork.app.module.blog

import dagger.Binds
import dagger.Module
import eu.peernetwork.blog.data.api.CommentApi
import eu.peernetwork.blog.data.api.ContentApi
import eu.peernetwork.blog.data.api.EngagementApi
import eu.peernetwork.blog.remote.api.CommentApiDelegate
import eu.peernetwork.blog.remote.api.ContentApiDelegate
import eu.peernetwork.blog.remote.api.EngagementApiDelegate

@Module
interface BlogApiModule {
    @Binds
    fun bindContentApiApi(delegate: ContentApiDelegate): ContentApi

    @Binds
    fun bindCommentApi(delegate: CommentApiDelegate): CommentApi

    @Binds
    fun bindEngagementApi(delegate: EngagementApiDelegate): EngagementApi
}
