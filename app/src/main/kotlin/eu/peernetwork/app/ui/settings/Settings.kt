package eu.peernetwork.app.ui.settings

import android.content.Context
import eu.peernetwork.app.provider.ApplicationProvider
import eu.peernetwork.app.ui.about.About
import eu.peernetwork.app.ui.profile.Profile
import eu.peernetwork.app.ui.version.Version
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.social.ui.referral.Referral
import eu.peernetwork.user.ui.password.update.Update
import eu.peernetwork.user.ui.account.Account
import eu.peernetwork.user.ui.deactivate.Deactivate
import eu.peernetwork.user.ui.email.Email
import eu.peernetwork.user.ui.logout.Logout
import eu.peernetwork.user.ui.user.User

interface Settings : ApplicationProvider {
    fun settingsEvent(): SettingsEvent

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Settings::class ],
        modules = [ SettingsModule::class ]
    )
    interface Component : Settings,
        User,
        Account,
        Update,
        Email,
        About,
        UiComponentProvider,
        Referral,
        Profile,
        Version,
        Logout,
        Deactivate

    class Builder(private val dependency: Settings) : UiComponent.DefaultBuilder<Settings, Component>() {
        override fun build(context: Context): Component {
            return DaggerSettings_Component.builder().settings(dependency).build()
        }
    }
}
