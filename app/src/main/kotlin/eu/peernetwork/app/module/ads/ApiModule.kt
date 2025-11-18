package eu.peernetwork.app.module.ads

import dagger.Binds
import dagger.Module
import eu.peernetwork.ads.data.api.AdvertiserApi
import eu.peernetwork.ads.data.api.ContentApi
import eu.peernetwork.ads.remote.api.AdvertiserApiDelegate
import eu.peernetwork.ads.remote.api.ContentApiDelegate

@Module
interface ApiModule {
    @Binds
    fun bindAdvertiserApi(delegate: AdvertiserApiDelegate): AdvertiserApi

    @Binds
    fun bindContentApi(delegate: ContentApiDelegate): ContentApi
}
