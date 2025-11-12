package eu.peernetwork.ads.ui.analytics

import android.content.Context
import eu.peernetwork.ads.ui.provider.AdsProvider
import eu.peernetwork.core.ui.component.UiComponent

interface Analytics : AdsProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Analytics::class ],
        modules = [ AnalyticsModule::class ]
    )
    interface Component : Analytics

    class Builder(private val dependency: Analytics) : UiComponent.DefaultBuilder<Analytics, Component>() {
        override fun build(context: Context): Component {
            return DaggerAnalytics_Component.builder().analytics(dependency).build()
        }
    }
}
