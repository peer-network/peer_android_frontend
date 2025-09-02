package eu.peernetwork.app.ui.profile

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.app.ui.renderer.EngagementRenderer
import eu.peernetwork.app.ui.search.Search
import eu.peernetwork.app.ui.settings.Settings
import eu.peernetwork.app.ui.window.Window
import eu.peernetwork.blog.ui.engagement.EngagementDialog
import eu.peernetwork.blog.ui.post.photo.Photo
import eu.peernetwork.blog.ui.post.video.Video
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.social.ui.connection.Connection
import eu.peernetwork.social.ui.followers.Followers
import eu.peernetwork.social.ui.followings.Followings
import eu.peernetwork.social.ui.peers.Peers
import eu.peernetwork.user.ui.user.User
import eu.peernetwork.wallet.ui.confirmation.Confirmation

@Module
object ProfileModule {
    @Provides
    @Profile.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Profile.Scope
    @Provides
    @IntoMap
    @UiBuilder(User.Builder::class)
    fun provideUserBuilder(component: Profile.Component): UiComponent.Builder {
        return User.Builder(component)
    }

    @Profile.Scope
    @Provides
    @IntoMap
    @UiBuilder(Photo.Builder::class)
    fun providePhotoBuilder(component: Profile.Component): UiComponent.Builder {
        return Photo.Builder(component)
    }

    @Profile.Scope
    @Provides
    @IntoMap
    @UiBuilder(Video.Builder::class)
    fun provideVideoBuilder(component: Profile.Component): UiComponent.Builder {
        return Video.Builder(component)
    }

    @Profile.Scope
    @Provides
    @IntoMap
    @UiBuilder(Search.Builder::class)
    fun provideSearchBuilder(component: Profile.Component): UiComponent.Builder {
        return Search.Builder(component)
    }

    @Profile.Scope
    @Provides
    @IntoMap
    @UiBuilder(Settings.Builder::class)
    fun provideSettingsBuilder(component: Profile.Component): UiComponent.Builder {
        return Settings.Builder(component)
    }

    @Provides
    @IntoMap
    @Profile.Scope
    @UiBuilder(Connection.Builder::class)
    fun provideConnectionBuilder(component: Profile.Component): UiComponent.Builder {
        return Connection.Builder(component)
    }

    @Provides
    @IntoMap
    @Profile.Scope
    @UiBuilder(Followers.Builder::class)
    fun provideFollowersBuilder(component: Profile.Component): UiComponent.Builder {
        return Followers.Builder(component)
    }

    @Provides
    @IntoMap
    @Profile.Scope
    @UiBuilder(Followings.Builder::class)
    fun provideFollowingsBuilder(component: Profile.Component): UiComponent.Builder {
        return Followings.Builder(component)
    }

    @Provides
    @IntoMap
    @Profile.Scope
    @UiBuilder(Peers.Builder::class)
    fun providePeersBuilder(component: Profile.Component): UiComponent.Builder {
        return Peers.Builder(component)
    }

    @Profile.Scope
    @Provides
    @IntoMap
    @UiBuilder(Confirmation.Builder::class)
    fun provideConfirmationBuilder(component: Profile.Component): UiComponent.Builder {
        return Confirmation.Builder(component)
    }

    @Profile.Scope
    @Provides
    fun provideEngagementRenderer(component: Profile.Component): EngagementDialog {
        return EngagementRenderer(component)
    }

    @Profile.Scope
    @Provides
    @IntoMap
    @UiBuilder(Window.Builder::class)
    fun provideWindowBuilder(component: Profile.Component): UiComponent.Builder {
        return Window.Builder(component)
    }
}
