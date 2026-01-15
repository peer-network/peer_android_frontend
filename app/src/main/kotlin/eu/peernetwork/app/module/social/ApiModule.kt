package eu.peernetwork.app.module.social

import dagger.Binds
import dagger.Module
import eu.peernetwork.app.ui.renderer.ConnectionRenderer
import eu.peernetwork.blog.ui.post.PostUserConnection
import eu.peernetwork.social.data.api.ModerationApi
import eu.peernetwork.social.data.api.FollowApi
import eu.peernetwork.social.data.api.InviteApi
import eu.peernetwork.social.data.api.ReferralApi
import eu.peernetwork.social.data.api.SearchApi
import eu.peernetwork.social.remote.api.ModerationApiDelegate
import eu.peernetwork.social.remote.api.FollowApiDelegate
import eu.peernetwork.social.remote.api.InviteApiDelegate
import eu.peernetwork.social.remote.api.ReferralApiDelegate
import eu.peernetwork.social.remote.api.SearchApiDelegate

@Module
interface ApiModule {
    @Binds
    fun bindsFollowApi(delegate: FollowApiDelegate): FollowApi

    @Binds
    fun bindsSearchApi(delegate: SearchApiDelegate): SearchApi

    @Binds
    fun bindsReferralApi(delegate: ReferralApiDelegate): ReferralApi

    @Binds
    fun bindsModerationApi(delegate: ModerationApiDelegate): ModerationApi

    @Binds
    fun bindsInviteApi(delegate: InviteApiDelegate): InviteApi

    @Binds
    fun bindsPostUserFollow(renderer: ConnectionRenderer): PostUserConnection
}
