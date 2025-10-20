package eu.peernetwork.user.ui.v2.registration

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.user.ui.provider.UserProvider

interface Registration : UserProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Registration::class ],
        modules = [ RegistrationModule::class ]
    )
    interface Component : Registration {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Registration) : UiComponent.DefaultBuilder<Registration, Component>() {
        override fun build(context: Context): Component {
            return DaggerRegistration_Component.builder()
                .registration(dependency)
                .build()
        }
    }
}
