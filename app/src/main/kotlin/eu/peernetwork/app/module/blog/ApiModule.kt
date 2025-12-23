package eu.peernetwork.app.module.blog

import dagger.Binds
import dagger.Module
import eu.peernetwork.app.provider.CacheProviderDelegate
import eu.peernetwork.blog.data.api.CommentApi
import eu.peernetwork.blog.data.api.ContentApi
import eu.peernetwork.blog.data.api.EligibilityApi
import eu.peernetwork.blog.data.api.EngagementApi
import eu.peernetwork.blog.data.api.MultipartApi
import eu.peernetwork.blog.data.provider.CacheProvider
import eu.peernetwork.blog.remote.api.CommentApiDelegate
import eu.peernetwork.blog.remote.api.ContentApiDelegate
import eu.peernetwork.blog.remote.api.EligibilityApiDelegate
import eu.peernetwork.blog.remote.api.EngagementApiDelegate
import eu.peernetwork.blog.remote.api.MultipartApiDelegate
import eu.peernetwork.blog.remote.helper.RequestHelper

@Module
interface ApiModule {
    @Binds
    fun bindContentApiApi(delegate: ContentApiDelegate): ContentApi

    @Binds
    fun bindCommentApi(delegate: CommentApiDelegate): CommentApi

    @Binds
    fun bindEngagementApi(delegate: EngagementApiDelegate): EngagementApi

    @Binds
    fun bindMultipartApi(delegate: MultipartApiDelegate): MultipartApi

    @Binds
    fun bindEligibilityApi(delegate: EligibilityApiDelegate): EligibilityApi

    @Binds
    fun bindRequestHelper(delegate: RequestHelper.Delegate): RequestHelper

    @Binds
    fun bindCacheProvider(delegate: CacheProviderDelegate): CacheProvider
}
