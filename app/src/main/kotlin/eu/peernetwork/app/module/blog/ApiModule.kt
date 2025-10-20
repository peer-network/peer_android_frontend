package eu.peernetwork.app.module.blog

import dagger.Binds
import dagger.Module
import eu.peernetwork.blog.data.api.CommentApi
import eu.peernetwork.blog.data.api.ContentApi
import eu.peernetwork.blog.data.api.ContentMultipartApi
import eu.peernetwork.blog.data.api.EngagementApi
import eu.peernetwork.blog.data.api.MultipartApi
import eu.peernetwork.blog.remote.api.CommentApiDelegate
import eu.peernetwork.blog.remote.api.ContentApiDelegate
import eu.peernetwork.blog.remote.api.ContentMultipartApiDelegate
import eu.peernetwork.blog.remote.api.EngagementApiDelegate
import eu.peernetwork.blog.remote.api.MultipartApiDelegate

@Module
interface ApiModule {
    @Binds
    fun bindContentApiApi(delegate: ContentApiDelegate): ContentApi

    @Binds
    fun bindCommentApi(delegate: CommentApiDelegate): CommentApi

    @Binds
    fun bindEngagementApi(delegate: EngagementApiDelegate): EngagementApi

    @Binds
    fun bindContentMultipartApi(delegate: ContentMultipartApiDelegate): ContentMultipartApi
}
