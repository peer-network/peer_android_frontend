package eu.peernetwork.app.module.core

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.app.Peer
import eu.peernetwork.app.ui.main.Main
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import javax.inject.Singleton

@Module
object UiModule {
    @Provides
    fun provideFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Singleton
    @Provides
    @IntoMap
    @UiBuilder(Main.Builder::class)
    fun provideMainBuilder(component: Peer.Component): UiComponent.Builder {
        return Main.Builder(component)
    }
}
