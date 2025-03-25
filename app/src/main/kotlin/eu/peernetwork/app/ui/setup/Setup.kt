package eu.peernetwork.app.ui.setup

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.persistence.domain.provider.PreferenceProvider
import eu.peernetwork.user.domain.provider.AccountProvider
import eu.peernetwork.user.domain.provider.AuthenticationProvider
import eu.peernetwork.user.ui.login.Login
import eu.peernetwork.user.ui.registeration.Registration

interface Setup : AccountProvider, AuthenticationProvider, PreferenceProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Setup::class ],
        modules = [ SetupModule::class ]
    )
    interface Component : Setup, Login, Registration, UiComponentProvider {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Setup) : UiComponent.DefaultBuilder<Setup, Component>() {
        override fun build(context: Context): Component {
            return DaggerSetup_Component.builder().setup(dependency).build()
        }
    }
}
