package eu.peernetwork.user.ui.v2.password.reset

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.user.ui.provider.UserProvider

interface Reset : UserProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Reset::class ],
        modules = [ ResetModule::class ]
    )
    interface Component : Reset {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Reset) : UiComponent.DefaultBuilder<Reset, Component>() {
        override fun build(context: Context): Component {
            return DaggerReset_Component.builder()
                .reset(dependency)
                .build()
        }
    }
}
