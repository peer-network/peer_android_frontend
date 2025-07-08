package eu.peernetwork.app.ui.search

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.app.ui.feed.Feed
import eu.peernetwork.app.ui.profile.Profile
import eu.peernetwork.blog.ui.explore.Explore
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
    @UiBuilder(Feed.Builder::class)
    fun provideFeedBuilder(component: Search.Component): UiComponent.Builder {
        return Feed.Builder(component)
    }

    @Search.Scope
    @Provides
    @IntoMap
    @UiBuilder(Explore.Builder::class)
    fun provideExploreBuilder(component: Search.Component): UiComponent.Builder {
        return Explore.Builder(component)
    }
}
