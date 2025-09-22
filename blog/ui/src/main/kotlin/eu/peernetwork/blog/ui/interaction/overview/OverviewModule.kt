package eu.peernetwork.blog.ui.interaction.overview

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.blog.ui.interaction.listing.Listing
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory

@Module
object OverviewModule {
    @Provides
    @Overview.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Provides
    @IntoMap
    @Overview.Scope
    @UiBuilder(Listing.Builder::class)
    fun provideListingBuilder(component: Overview.Component): UiComponent.Builder {
        return Listing.Builder(component)
    }
}
