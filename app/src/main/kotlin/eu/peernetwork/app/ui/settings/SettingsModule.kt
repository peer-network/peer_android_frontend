package eu.peernetwork.app.ui.settings

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.user.ui.password.update.PasswordUpdate
import eu.peernetwork.user.ui.settings.account.Account
import eu.peernetwork.user.ui.settings.address.Address

@Module
object SettingsModule {
    @Provides
    @Settings.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Settings.Scope
    @Provides
    @IntoMap
    @UiBuilder(Account.Builder::class)
    fun provideAccountBuilder(component: Settings.Component): UiComponent.Builder {
        return Account.Builder(component)
    }

    @Settings.Scope
    @Provides
    @IntoMap
    @UiBuilder(Address.Builder::class)
    fun provideAddressBuilder(component: Settings.Component): UiComponent.Builder {
        return Address.Builder(component)
    }

    @Settings.Scope
    @Provides
    @IntoMap
    @UiBuilder(PasswordUpdate.Builder::class)
    fun providePasswordUpdateBuilder(component: Settings.Component): UiComponent.Builder {
        return PasswordUpdate.Builder(component)
    }
}
