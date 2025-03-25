package eu.peernetwork.user.ui.user.core

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.common.provider.CoreProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.user.domain.provider.AccountProvider
import eu.peernetwork.user.domain.provider.AuthenticationProvider

interface User : CoreProvider, AccountProvider, AuthenticationProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ User::class ],
        modules = [ UserModule::class ]
    )
    interface Component : User {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: User) : UiComponent.DefaultBuilder<User, Component>() {
        override fun build(context: Context): Component {
            return DaggerUser_Component.builder().user(dependency).build()
        }
    }
}
