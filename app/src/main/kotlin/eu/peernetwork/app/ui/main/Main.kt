package eu.peernetwork.app.ui.main

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjector
import eu.peernetwork.app.ui.home.Home
import eu.peernetwork.app.ui.setup.Setup
import eu.peernetwork.app.ui.splash.Splash
import eu.peernetwork.core.common.provider.CoreProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.persistence.domain.provider.PreferenceProvider
import eu.peernetwork.user.domain.provider.AccountProvider
import eu.peernetwork.user.domain.provider.AuthenticationProvider

interface Main : CoreProvider, AccountProvider, AuthenticationProvider, PreferenceProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Main::class ],
        modules = [MainModule::class]
    )
    interface Component : Main,
        AndroidInjector<MainActivity>,
        Home,
        Setup,
        Splash,
        UiComponentProvider {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Main) : UiComponent.DefaultBuilder<Main, Component>() {
        override fun build(context: Context): Component {
            return DaggerMain_Component.builder().main(dependency).build()
        }
    }
}
