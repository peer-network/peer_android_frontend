package eu.peernetwork.app.ui.splash

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.app.provider.ApplicationProvider
import eu.peernetwork.core.ui.component.UiComponent

interface Splash : ApplicationProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Splash::class ],
        modules = [ SplashModule::class ]
    )
    interface Component : Splash {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Splash) : UiComponent.DefaultBuilder<Splash, Component>() {
        override fun build(context: Context): Component {
            return DaggerSplash_Component.builder().splash(dependency).build()
        }
    }
}
