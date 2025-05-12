package eu.peernetwork.social.ui.connection

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.social.ui.provider.SocialProvider

interface Connection : SocialProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Connection::class ],
        modules = [ ConnectionModule::class ]
    )
    interface Component : Connection {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Connection) : UiComponent.DefaultBuilder<Connection, Component>() {
        override fun build(context: Context): Component {
            return DaggerConnection_Component.builder().connection(dependency).build()
        }
    }
}
