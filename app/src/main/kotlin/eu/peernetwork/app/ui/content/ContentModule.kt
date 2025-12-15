package eu.peernetwork.app.ui.content

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.app.ui.profile.Profile
import eu.peernetwork.app.ui.renderer.EngagementRenderer
import eu.peernetwork.app.ui.search.Search
import eu.peernetwork.app.ui.screen.Screen
import eu.peernetwork.blog.ui.detail.Detail
import eu.peernetwork.blog.ui.engagement.EngagementDialog
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.social.ui.connection.Connection
import eu.peernetwork.wallet.ui.confirmation.Confirmation

@Module
object ContentModule {
    @Provides
    @Content.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Content.Scope
    @Provides
    @IntoMap
    @UiBuilder(Profile.Builder::class)
    fun provideProfileBuilder(component: Content.Component): UiComponent.Builder {
        return Profile.Builder(component)
    }

    @Content.Scope
    @Provides
    @IntoMap
    @UiBuilder(Search.Builder::class)
    fun provideSearchBuilder(component: Content.Component): UiComponent.Builder {
        return Search.Builder(component)
    }

    @Content.Scope
    @Provides
    @IntoMap
    @UiBuilder(Connection.Builder::class)
    fun provideConnectionBuilder(component: Content.Component): UiComponent.Builder {
        return Connection.Builder(component)
    }

    @Content.Scope
    @Provides
    @IntoMap
    @UiBuilder(Confirmation.Builder::class)
    fun provideConfirmationBuilder(component: Content.Component): UiComponent.Builder {
        return Confirmation.Builder(component)
    }

    @Content.Scope
    @Provides
    @IntoMap
    @UiBuilder(Detail.Builder::class)
    fun provideDetailBuilder(component: Content.Component): UiComponent.Builder {
        return Detail.Builder(component)
    }

    @Content.Scope
    @Provides
    fun provideEngagementRenderer(component: Content.Component): EngagementDialog {
        return EngagementRenderer(component)
    }

    @Content.Scope
    @Provides
    @IntoMap
    @UiBuilder(Screen.Builder::class)
    fun provideWindowBuilder(component: Content.Component): UiComponent.Builder {
        return Screen.Builder(component)
    }
}
