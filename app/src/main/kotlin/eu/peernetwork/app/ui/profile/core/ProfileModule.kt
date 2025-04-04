package eu.peernetwork.app.ui.profile.core

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.app.ui.profile.preview.ProfilePreview
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.user.ui.settings.UserSettings

@Module
object ProfileModule {
    @Provides
    @Profile.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Profile.Scope
    @Provides
    @IntoMap
    @UiBuilder(ProfilePreview.Builder::class)
    fun provideProfileDetailBuilder(component: Profile.Component): UiComponent.Builder {
        return ProfilePreview.Builder(component)
    }

    @Profile.Scope
    @Provides
    @IntoMap
    @UiBuilder(UserSettings.Builder::class)
    fun provideUserSettingsBuilder(component: Profile.Component): UiComponent.Builder {
        return UserSettings.Builder(component)
    }
}
