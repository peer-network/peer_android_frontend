package eu.peernetwork.user.ui.password.verification

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.user.ui.provider.UserProvider

interface Verification : UserProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Verification::class ],
        modules = [ VerificationModule::class ]
    )
    interface Component : Verification {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Verification) : UiComponent.DefaultBuilder<Verification, Component>() {
        override fun build(context: Context): Component {
            return DaggerVerification_Component.builder()
                .verification(dependency)
                .build()
        }
    }
}
