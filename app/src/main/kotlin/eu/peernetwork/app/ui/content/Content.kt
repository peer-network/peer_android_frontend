package eu.peernetwork.app.ui.content

import android.content.Context
import eu.peernetwork.app.provider.ApplicationProvider
import eu.peernetwork.app.ui.profile.Profile
import eu.peernetwork.app.ui.search.Search
import eu.peernetwork.app.ui.settings.SettingsEvent
import eu.peernetwork.app.ui.window.Window
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.social.ui.connection.Connection
import eu.peernetwork.wallet.ui.confirmation.Confirmation

interface Content : ApplicationProvider {
    fun settingsEvent(): SettingsEvent

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Content::class ],
        modules = [ ContentModule::class ]
    )
    interface Component : Content,
        UiComponentProvider,
        Search,
        Profile,
        Connection,
        Confirmation,
        Window

    class Builder(private val dependency: Content) : UiComponent.DefaultBuilder<Content, Component>() {
        override fun build(context: Context): Component {
            return DaggerContent_Component.builder().content(dependency).build()
        }
    }
}
