package eu.peernetwork.ads.ui.dashboard

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.ads.ui.adverts.Adverts
import eu.peernetwork.ads.ui.analytics.Analytics
import eu.peernetwork.ads.ui.overview.Overview
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import javax.inject.Provider

@Module
object DashboardModule {
    @Provides
    @Dashboard.Scope
    fun provideBuilderFactory(factory: Map<Class<out UiComponent.Builder>,
            @JvmSuppressWildcards Provider<UiComponent.Builder>>): UiComponentProvider.Factory {
        return UiBuilderFactory(factory)
    }

    @Dashboard.Scope
    @Provides
    @IntoMap
    @UiBuilder(Overview.Builder::class)
    fun provideOverviewBuilder(component: Dashboard.Component): UiComponent.Builder {
        return Overview.Builder(component)
    }

    @Dashboard.Scope
    @Provides
    @IntoMap
    @UiBuilder(Adverts.Builder::class)
    fun provideAdvertsBuilder(component: Dashboard.Component): UiComponent.Builder {
        return Adverts.Builder(component)
    }

    @Dashboard.Scope
    @Provides
    @IntoMap
    @UiBuilder(Analytics.Builder::class)
    fun provideAnalyticsBuilder(component: Dashboard.Component): UiComponent.Builder {
        return Analytics.Builder(component)
    }
}
