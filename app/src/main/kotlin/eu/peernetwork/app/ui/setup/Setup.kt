package eu.peernetwork.app.ui.setup

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.app.provider.ApplicationProvider
import eu.peernetwork.app.ui.privacy.Privacy
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.user.ui.login.Login
import eu.peernetwork.user.ui.password.request.PasswordRequest
import eu.peernetwork.user.ui.registeration.Registration

interface Setup : ApplicationProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Setup::class ],
        modules = [ SetupModule::class ]
    )
    interface Component : Setup, Login, Registration, PasswordRequest, Privacy, UiComponentProvider {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Setup) : UiComponent.DefaultBuilder<Setup, Component>() {
        override fun build(context: Context): Component {
            return DaggerSetup_Component.builder().setup(dependency).build()
        }
    }
}
