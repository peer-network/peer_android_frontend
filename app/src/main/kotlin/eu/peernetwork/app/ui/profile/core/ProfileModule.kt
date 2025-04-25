package eu.peernetwork.app.ui.profile.core

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.app.ui.profile.preview.Preview
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.user.ui.settings.Settings

@Module
object ProfileModule {
    @Provides
    @Profile.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Profile.Scope
    @Provides
    @IntoMap
    @UiBuilder(Preview.Builder::class)
    fun provideProfileDetailBuilder(component: Profile.Component): UiComponent.Builder {
        return Preview.Builder(component)
    }

    @Profile.Scope
    @Provides
    @IntoMap
    @UiBuilder(Settings.Builder::class)
    fun provideUserSettingsBuilder(component: Profile.Component): UiComponent.Builder {
        return Settings.Builder(component)
    }
}
