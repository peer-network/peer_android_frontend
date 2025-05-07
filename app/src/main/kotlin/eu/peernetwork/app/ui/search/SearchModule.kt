package eu.peernetwork.app.ui.search

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.app.ui.profile.Profile
import eu.peernetwork.blog.ui.preview.photo.Photo
import eu.peernetwork.blog.ui.preview.video.Video
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.social.ui.search.member.Member
import eu.peernetwork.social.ui.search.tag.Tag
import eu.peernetwork.social.ui.search.title.Title

@Module
object SearchModule {
    @Provides
    @Search.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Search.Scope
    @Provides
    @IntoMap
    @UiBuilder(Profile.Builder::class)
    fun provideProfileBuilder(component: Search.Component): UiComponent.Builder {
        return Profile.Builder(component)
    }

    @Search.Scope
    @Provides
    @IntoMap
    @UiBuilder(Member.Builder::class)
    fun provideMemberBuilder(component: Search.Component): UiComponent.Builder {
        return Member.Builder(component)
    }

    @Search.Scope
    @Provides
    @IntoMap
    @UiBuilder(Tag.Builder::class)
    fun provideTagBuilder(component: Search.Component): UiComponent.Builder {
        return Tag.Builder(component)
    }

    @Search.Scope
    @Provides
    @IntoMap
    @UiBuilder(Title.Builder::class)
    fun provideTitleBuilder(component: Search.Component): UiComponent.Builder {
        return Title.Builder(component)
    }

    @Search.Scope
    @Provides
    @IntoMap
    @UiBuilder(Photo.Builder::class)
    fun providePhotoBuilder(component: Search.Component): UiComponent.Builder {
        return Photo.Builder(component)
    }

    @Search.Scope
    @Provides
    @IntoMap
    @UiBuilder(Video.Builder::class)
    fun provideVideoBuilder(component: Search.Component): UiComponent.Builder {
        return Video.Builder(component)
    }
}
