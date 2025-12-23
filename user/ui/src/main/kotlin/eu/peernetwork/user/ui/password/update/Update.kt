package eu.peernetwork.user.ui.password.update

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.user.ui.provider.UserProvider

interface Update : UserProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Update::class ],
        modules = [ UpdateModule::class ]
    )
    interface Component : Update {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Update) : UiComponent.DefaultBuilder<Update, Component>() {
        override fun build(context: Context): Component {
            return DaggerUpdate_Component.builder().update(dependency).build()
        }
    }
}
