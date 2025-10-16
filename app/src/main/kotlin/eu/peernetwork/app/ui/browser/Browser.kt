package eu.peernetwork.app.ui.browser

import android.content.Context
import eu.peernetwork.core.ui.component.UiComponent

interface Browser {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Browser::class ],
        modules = [ BrowserModule::class ]
    )
    interface Component : Browser

    class Builder(private val dependency: Browser) : UiComponent.DefaultBuilder<Browser, Component>() {
        override fun build(context: Context): Component {
            return DaggerBrowser_Component.builder().browser(dependency).build()
        }
    }
}
