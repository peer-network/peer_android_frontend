package eu.peernetwork.user.ui.login

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.user.ui.provider.UserProvider

interface Login : UserProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Login::class ],
        modules = [ LoginModule::class ]
    )
    interface Component : Login {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Login) : UiComponent.DefaultBuilder<Login, Component>() {
        override fun build(context: Context): Component {
            return DaggerLogin_Component.builder().login(dependency).build()
        }
    }
}
