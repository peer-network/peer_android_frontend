package eu.peernetwork.app.ui.profile

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.app.ui.renderer.BlogRendererDelegate
import eu.peernetwork.app.ui.renderer.UserRendererDelegate
import eu.peernetwork.app.ui.search.Search
import eu.peernetwork.blog.ui.post.music.Music
import eu.peernetwork.blog.ui.post.photo.Photo
import eu.peernetwork.blog.ui.post.video.Video
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.social.ui.member.Member
import eu.peernetwork.social.ui.renderder.BlogRenderer
import eu.peernetwork.social.ui.renderder.UserRenderer
import eu.peernetwork.user.ui.account.Account
import eu.peernetwork.user.ui.settings.Settings
import eu.peernetwork.user.ui.user.User

@Module
object ProfileModule {
    @Provides
    @Profile.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Profile.Scope
    @Provides
    @IntoMap
    @UiBuilder(Member.Builder::class)
    fun provideMemberBuilder(component: Profile.Component): UiComponent.Builder {
        return Member.Builder(component)
    }

    @Profile.Scope
    @Provides
    @IntoMap
    @UiBuilder(Account.Builder::class)
    fun provideAccountBuilder(component: Profile.Component): UiComponent.Builder {
        return Account.Builder(component)
    }

    @Profile.Scope
    @Provides
    @IntoMap
    @UiBuilder(Settings.Builder::class)
    fun provideUserSettingsBuilder(component: Profile.Component): UiComponent.Builder {
        return Settings.Builder(component)
    }

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
    @UiBuilder(Music.Builder::class)
    fun provideMusicBuilder(component: Profile.Component): UiComponent.Builder {
        return Music.Builder(component)
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
    fun provideBlogRenderer(component: Profile.Component): BlogRenderer {
        return BlogRendererDelegate(component)
    }

    @Profile.Scope
    @Provides
    fun provideProfileDetail(component: Profile.Component): UserRenderer {
        return UserRendererDelegate(component)
    }
}
