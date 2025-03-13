package eu.peernetwork.app.ui.setup

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.user.ui.login.Login
import eu.peernetwork.user.ui.registeration.Registration

@Module
object SetupModule {
    @Provides
    @Setup.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Setup.Scope
    @Provides
    @IntoMap
    @UiBuilder(Login.Builder::class)
    fun provideLoginBuilder(component: Setup.Component): UiComponent.Builder {
        return Login.Builder(component)
    }

    @Setup.Scope
    @Provides
    @IntoMap
    @UiBuilder(Registration.Builder::class)
    fun provideRegistrationBuilder(component: Setup.Component): UiComponent.Builder {
        return Registration.Builder(component)
    }
}
