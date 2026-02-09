package eu.peernetwork.user.ui.logout

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.user.ui.provider.UserProvider

interface Logout : UserProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Logout::class ],
        modules = [ LogoutModule::class ]
    )
    interface Component : Logout {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Logout) : UiComponent.DefaultBuilder<Logout, Component>() {
        override fun build(context: Context): Component {
            return DaggerLogout_Component.builder()
                .logout(dependency)
                .build()
        }
    }
}
