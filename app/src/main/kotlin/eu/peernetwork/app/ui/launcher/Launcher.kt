package eu.peernetwork.app.ui.launcher

import android.content.Context
import eu.peernetwork.app.provider.ApplicationProvider
import eu.peernetwork.app.ui.browser.Browser
import eu.peernetwork.app.ui.home.Home
import eu.peernetwork.app.ui.onboarding.Onboarding
import eu.peernetwork.app.ui.setup.Setup
import eu.peernetwork.app.ui.welcome.Welcome
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider

interface Launcher : ApplicationProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Launcher::class ],
        modules = [ LauncherModule::class ]
    )
    interface Component : Launcher,
        Onboarding,
        Home,
        Setup,
        Welcome,
        Browser,
        UiComponentProvider

    class Builder(private val dependency: Launcher) : UiComponent.DefaultBuilder<Launcher, Component>() {
        override fun build(context: Context): Component {
            return DaggerLauncher_Component.builder().launcher(dependency).build()
        }
    }
}
