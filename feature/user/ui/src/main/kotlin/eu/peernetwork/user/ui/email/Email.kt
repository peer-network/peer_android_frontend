package eu.peernetwork.user.ui.email

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.user.ui.provider.UserProvider

interface Email : UserProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Email::class ],
        modules = [ EmailModule::class ]
    )
    interface Component : Email {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Email) : UiComponent.DefaultBuilder<Email, Component>() {
        override fun build(context: Context): Component {
            return DaggerEmail_Component.builder().email(dependency).build()
        }
    }
}
