package eu.peernetwork.user.ui.password.update

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.user.ui.provider.UserProvider

interface PasswordUpdate : UserProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ PasswordUpdate::class ],
        modules = [ PasswordUpdateModule::class ]
    )
    interface Component : PasswordUpdate {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: PasswordUpdate) : UiComponent.DefaultBuilder<PasswordUpdate, Component>() {
        override fun build(context: Context): Component {
            return DaggerPasswordUpdate_Component.builder().passwordUpdate(dependency).build()
        }
    }
}
