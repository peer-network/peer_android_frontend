package eu.peernetwork.app.ui.member

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.blog.ui.post.photo.Photo
import eu.peernetwork.blog.ui.post.video.Video
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import javax.inject.Provider
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.social.ui.connection.Connection
import eu.peernetwork.social.ui.followers.Followers
import eu.peernetwork.social.ui.followings.Followings
import eu.peernetwork.social.ui.peers.Peers
import eu.peernetwork.user.ui.user.User

@Module
object MemberModule {
    @Provides
    @Member.Scope
    fun provideBuilderFactory(factory: Map<Class<out UiComponent.Builder>,
            @JvmSuppressWildcards Provider<UiComponent.Builder>>): UiComponentProvider.Factory {
        return UiBuilderFactory(factory)
    }

    @Member.Scope
    @Provides
    @IntoMap
    @UiBuilder(User.Builder::class)
    fun provideUserBuilder(component: Member.Component): UiComponent.Builder {
        return User.Builder(component)
    }

    @Member.Scope
    @Provides
    @IntoMap
    @UiBuilder(Photo.Builder::class)
    fun providePhotoBuilder(component: Member.Component): UiComponent.Builder {
        return Photo.Builder(component)
    }

    @Member.Scope
    @Provides
    @IntoMap
    @UiBuilder(Video.Builder::class)
    fun provideVideoBuilder(component: Member.Component): UiComponent.Builder {
        return Video.Builder(component)
    }

    @Provides
    @IntoMap
    @Member.Scope
    @UiBuilder(Connection.Builder::class)
    fun provideConnectionBuilder(member: Member.Component): UiComponent.Builder {
        return Connection.Builder(member)
    }

    @Provides
    @IntoMap
    @Member.Scope
    @UiBuilder(Followers.Builder::class)
    fun provideFollowersBuilder(member: Member.Component): UiComponent.Builder {
        return Followers.Builder(member)
    }

    @Provides
    @IntoMap
    @Member.Scope
    @UiBuilder(Followings.Builder::class)
    fun provideFollowingsBuilder(member: Member.Component): UiComponent.Builder {
        return Followings.Builder(member)
    }

    @Provides
    @IntoMap
    @Member.Scope
    @UiBuilder(Peers.Builder::class)
    fun providePeersBuilder(member: Member.Component): UiComponent.Builder {
        return Peers.Builder(member)
    }
}