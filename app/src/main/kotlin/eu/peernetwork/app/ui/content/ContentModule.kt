package eu.peernetwork.app.ui.content

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.ads.ui.boost.Boost
import eu.peernetwork.ads.ui.checkout.Checkout
import eu.peernetwork.ads.ui.checkout.CheckoutBalance
import eu.peernetwork.app.ui.feed.Feed
import eu.peernetwork.app.ui.profile.Profile
import eu.peernetwork.app.ui.renderer.BalanceRenderer
import eu.peernetwork.app.ui.renderer.EngagementRenderer
import eu.peernetwork.app.ui.search.Search
import eu.peernetwork.app.ui.screen.Screen
import eu.peernetwork.blog.ui.detail.Detail
import eu.peernetwork.blog.ui.engagement.EngagementDialog
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.social.ui.connection.Connection
import eu.peernetwork.wallet.ui.balance.Balance
import eu.peernetwork.wallet.ui.confirmation.Confirmation
import eu.peernetwork.wallet.ui.service.Service

@Module
object ContentModule {
    @Provides
    @Content.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Content.Scope
    @Provides
    @IntoMap
    @UiBuilder(Profile.Builder::class)
    fun provideProfileBuilder(component: Content.Component): UiComponent.Builder {
        return Profile.Builder(component)
    }

    @Content.Scope
    @Provides
    @IntoMap
    @UiBuilder(Search.Builder::class)
    fun provideSearchBuilder(component: Content.Component): UiComponent.Builder {
        return Search.Builder(component)
    }

    @Content.Scope
    @Provides
    @IntoMap
    @UiBuilder(Connection.Builder::class)
    fun provideConnectionBuilder(component: Content.Component): UiComponent.Builder {
        return Connection.Builder(component)
    }

    @Content.Scope
    @Provides
    @IntoMap
    @UiBuilder(Confirmation.Builder::class)
    fun provideConfirmationBuilder(component: Content.Component): UiComponent.Builder {
        return Confirmation.Builder(component)
    }

    @Content.Scope
    @Provides
    @IntoMap
    @UiBuilder(Detail.Builder::class)
    fun provideDetailBuilder(component: Content.Component): UiComponent.Builder {
        return Detail.Builder(component)
    }

    @Content.Scope
    @Provides
    fun provideEngagementRenderer(component: Content.Component): EngagementDialog {
        return EngagementRenderer(component)
    }

    @Content.Scope
    @Provides
    @IntoMap
    @UiBuilder(Screen.Builder::class)
    fun provideWindowBuilder(component: Content.Component): UiComponent.Builder {
        return Screen.Builder(component)
    }

    @Content.Scope
    @Provides
    @IntoMap
    @UiBuilder(Checkout.Builder::class)
    fun provideCheckoutBuilder(component: Content.Component): UiComponent.Builder {
        return Checkout.Builder(component)
    }

    @Content.Scope
    @Provides
    @IntoMap
    @UiBuilder(Balance.Builder::class)
    fun provideBalanceBuilder(component: Content.Component): UiComponent.Builder {
        return Balance.Builder(component)
    }

    @Content.Scope
    @Provides
    @IntoMap
    @UiBuilder(Service.Builder::class)
    fun provideServiceBuilder(component: Content.Component): UiComponent.Builder {
        return Service.Builder(component)
    }

    @Provides
    @Content.Scope
    fun provideCheckoutBalance(component: Content.Component): CheckoutBalance {
        return BalanceRenderer(component)
    }

    @Content.Scope
    @Provides
    @IntoMap
    @UiBuilder(Boost.Builder::class)
    fun provideBoostBuilder(component: Content.Component): UiComponent.Builder {
        return Boost.Builder(component)
    }
}
