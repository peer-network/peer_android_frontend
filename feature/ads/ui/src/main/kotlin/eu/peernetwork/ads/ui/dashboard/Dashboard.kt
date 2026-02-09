package eu.peernetwork.ads.ui.dashboard

import android.content.Context
import eu.peernetwork.ads.ui.adverts.Adverts
import eu.peernetwork.ads.ui.analytics.Analytics
import eu.peernetwork.ads.ui.overview.Overview
import eu.peernetwork.ads.ui.provider.AdsProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider

interface Dashboard : AdsProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Dashboard::class ],
        modules = [ DashboardModule::class ]
    )
    interface Component : Dashboard, Overview, Adverts, Analytics, UiComponentProvider


    class Builder(private val dependency: Dashboard) : UiComponent.DefaultBuilder<Dashboard, Component>() {
        override fun build(context: Context): Component {
            return DaggerDashboard_Component.builder().dashboard(dependency).build()
        }
    }
}
