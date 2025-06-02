package eu.peernetwork.user.ui.password.request

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.user.ui.password.reset.PasswordReset
import eu.peernetwork.user.ui.provider.UserProvider

interface PasswordRequest : UserProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ PasswordRequest::class ],
        modules = [ PasswordRequestModule::class ]
    )
    interface Component : PasswordRequest, PasswordReset, UiComponentProvider {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: PasswordRequest) : UiComponent.DefaultBuilder<PasswordRequest, Component>() {
        override fun build(context: Context): Component {
            return DaggerPasswordRequest_Component.builder().passwordRequest(dependency).build()
        }
    }
}
