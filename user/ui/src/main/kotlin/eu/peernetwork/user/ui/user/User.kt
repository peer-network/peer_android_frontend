package eu.peernetwork.user.ui.user

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.media.core.provider.MediaProvider
import eu.peernetwork.user.ui.option.Option
import eu.peernetwork.user.ui.provider.UserProvider

interface User : UserProvider, MediaProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ User::class ],
        modules = [ UserModule::class ]
    )
    interface Component : User, Option, UiComponentProvider {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: User) : UiComponent.DefaultBuilder<User, Component>() {
        override fun build(context: Context): Component {
            return DaggerUser_Component.builder().user(dependency).build()
        }
    }
}
