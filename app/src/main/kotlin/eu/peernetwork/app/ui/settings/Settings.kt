package eu.peernetwork.app.ui.settings

import android.content.Context
import eu.peernetwork.app.provider.ApplicationProvider
import eu.peernetwork.app.ui.about.About
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.user.ui.password.update.PasswordUpdate
import eu.peernetwork.user.ui.settings.account.Account
import eu.peernetwork.user.ui.settings.address.Address

interface Settings : ApplicationProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Settings::class ],
        modules = [ SettingsModule::class ]
    )
    interface Component : Settings, Account, PasswordUpdate, Address, About, UiComponentProvider

    class Builder(private val dependency: Settings) : UiComponent.DefaultBuilder<Settings, Component>() {
        override fun build(context: Context): Component {
            return DaggerSettings_Component.builder().settings(dependency).build()
        }
    }
}
