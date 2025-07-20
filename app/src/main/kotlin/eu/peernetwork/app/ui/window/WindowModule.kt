package eu.peernetwork.app.ui.window

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.wallet.ui.reward.Reward

@Module
object WindowModule {
    @Provides
    @Window.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Window.Scope
    @Provides
    @IntoMap
    @UiBuilder(Reward.Builder::class)
    fun provideRewardBuilder(component: Window.Component): UiComponent.Builder {
        return Reward.Builder(component)
    }
}
