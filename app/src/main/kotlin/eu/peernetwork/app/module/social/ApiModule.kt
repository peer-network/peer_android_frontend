package eu.peernetwork.app.module.social

import dagger.Binds
import dagger.Module
import eu.peernetwork.social.data.api.FollowApi
import eu.peernetwork.social.remote.api.FollowApiDelegate

@Module
interface ApiModule {
    @Binds
    fun bindsFollowApi(delegate: FollowApiDelegate): FollowApi
}
