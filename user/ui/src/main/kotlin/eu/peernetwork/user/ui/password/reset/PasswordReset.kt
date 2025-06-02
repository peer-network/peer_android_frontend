package eu.peernetwork.user.ui.password.reset

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.user.ui.provider.UserProvider

interface PasswordReset : UserProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ PasswordReset::class ],
        modules = [ PasswordResetModule::class ]
    )
    interface Component : PasswordReset {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: PasswordReset) : UiComponent.DefaultBuilder<PasswordReset, Component>() {
        override fun build(context: Context): Component {
            return DaggerPasswordReset_Component.builder().passwordReset(dependency).build()
        }
    }
}
