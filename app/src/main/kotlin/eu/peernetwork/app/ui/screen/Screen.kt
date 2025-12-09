package eu.peernetwork.app.ui.screen

import android.content.Context
import eu.peernetwork.app.provider.ApplicationProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.wallet.ui.reward.Reward

interface Screen : ApplicationProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Screen::class ],
        modules = [ ScreenModule::class ]
    )
    interface Component : Screen, Reward, UiComponentProvider

    class Builder(private val dependency: Screen) : UiComponent.DefaultBuilder<Screen, Component>() {
        override fun build(context: Context): Component {
            return DaggerScreen_Component.builder().screen(dependency).build()
        }
    }
}
