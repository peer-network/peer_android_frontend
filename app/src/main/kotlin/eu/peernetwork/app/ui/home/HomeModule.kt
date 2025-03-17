package eu.peernetwork.app.ui.home

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.social.ui.feed.Feed

@Module
object HomeModule {
    @Provides
    @Home.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Home.Scope
    @Provides
    @IntoMap
    @UiBuilder(Feed.Builder::class)
    fun provideFeedBuilder(component: Home.Component): UiComponent.Builder {
        return Feed.Builder(component)
    }
}
