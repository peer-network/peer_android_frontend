package eu.peernetwork.app.ui.window

import android.content.Context
import eu.peernetwork.app.provider.ApplicationProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.wallet.ui.reward.Reward

interface Window : ApplicationProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Window::class ],
        modules = [ WindowModule::class ]
    )
    interface Component : Window, Reward, UiComponentProvider

    class Builder(private val dependency: Window) : UiComponent.DefaultBuilder<Window, Component>() {
        override fun build(context: Context): Component {
            return DaggerWindow_Component.builder().window(dependency).build()
        }
    }
}
