package eu.peernetwork.app.ui.onboarding

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.app.provider.ApplicationProvider
import eu.peernetwork.core.ui.component.UiComponent

interface Onboarding : ApplicationProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Onboarding::class ],
        modules = [ OnboardingModule::class ]
    )
    interface Component : Onboarding {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Onboarding) : UiComponent.DefaultBuilder<Onboarding, Component>() {
        override fun build(context: Context): Component {
            return DaggerOnboarding_Component.builder().onboarding(dependency).build()
        }
    }
}