package eu.peernetwork.user.ui.option

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.user.ui.provider.UserProvider

interface Option : UserProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Option::class ],
        modules = [ OptionModule::class ]
    )
    interface Component : Option {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Option) : UiComponent.DefaultBuilder<Option, Component>() {
        override fun build(context: Context): Component {
            return DaggerOption_Component.builder().option(dependency).build()
        }
    }
}
