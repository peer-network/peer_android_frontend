package eu.peernetwork.ads.ui.overview

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.ads.ui.provider.AdsProvider
import eu.peernetwork.core.ui.component.UiComponent

interface Overview : AdsProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Overview::class ],
        modules = [ OverviewModule::class ]
    )
    interface Component : Overview {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Overview) : UiComponent.DefaultBuilder<Overview, Component>() {
        override fun build(context: Context): Component {
            return DaggerOverview_Component.builder().overview(dependency).build()
        }
    }
}
