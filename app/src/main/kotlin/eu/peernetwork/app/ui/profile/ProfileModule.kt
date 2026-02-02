package eu.peernetwork.app.ui.profile

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.ads.ui.boost.Boost
import eu.peernetwork.ads.ui.checkout.Checkout
import eu.peernetwork.ads.ui.checkout.CheckoutBalance
import eu.peernetwork.ads.ui.dashboard.Dashboard
import eu.peernetwork.app.ui.feed.Feed
import eu.peernetwork.app.ui.renderer.BalanceRenderer
import eu.peernetwork.app.ui.renderer.EngagementRenderer
import eu.peernetwork.app.ui.search.Search
import eu.peernetwork.app.ui.settings.Settings
import eu.peernetwork.app.ui.screen.Screen
import eu.peernetwork.blog.ui.article.Article
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.social.ui.connection.Connection
import eu.peernetwork.social.ui.followers.Followers
import eu.peernetwork.social.ui.followings.Followings
import eu.peernetwork.social.ui.peers.Peers
import eu.peernetwork.social.ui.report.Report
import eu.peernetwork.user.ui.user.User
import eu.peernetwork.wallet.ui.balance.Balance
import eu.peernetwork.wallet.ui.confirmation.Confirmation
import eu.peernetwork.wallet.ui.rate.Rate as WalletDashboard

@Module
object ProfileModule {
    @Provides
    @Profile.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Profile.Scope
    @Provides
    @IntoMap
    @UiBuilder(Profile.Builder::class)
    fun provideProfileBuilder(component: Profile.Component): UiComponent.Builder {
        return Profile.Builder(component)
    }

    @Profile.Scope
    @Provides
    @IntoMap
    @UiBuilder(Report.Builder::class)
    fun provideReportBuilder(component: Profile.Component): UiComponent.Builder {
        return Report.Builder(component)
    }

    @Profile.Scope
    @Provides
    @IntoMap
    @UiBuilder(Feed.Builder::class)
    fun provideFeedBuilder(component: Profile.Component): UiComponent.Builder {
        return Feed.Builder(component)
    }

    @Profile.Scope
    @Provides
    @IntoMap
    @UiBuilder(User.Builder::class)
    fun provideUserBuilder(component: Profile.Component): UiComponent.Builder {
        return User.Builder(component)
    }

    @Profile.Scope
    @Provides
    @IntoMap
    @UiBuilder(Article.Builder::class)
    fun providePhotoBuilder(component: Profile.Component): UiComponent.Builder {
        return Article.Builder(component)
    }

    @Profile.Scope
    @Provides
    @IntoMap
    @UiBuilder(Search.Builder::class)
    fun provideSearchBuilder(component: Profile.Component): UiComponent.Builder {
        return Search.Builder(component)
    }

    @Profile.Scope
    @Provides
    @IntoMap
    @UiBuilder(Settings.Builder::class)
    fun provideSettingsBuilder(component: Profile.Component): UiComponent.Builder {
        return Settings.Builder(component)
    }

    @Provides
    @IntoMap
    @Profile.Scope
    @UiBuilder(Connection.Builder::class)
    fun provideConnectionBuilder(component: Profile.Component): UiComponent.Builder {
        return Connection.Builder(component)
    }

    @Provides
    @IntoMap
    @Profile.Scope
    @UiBuilder(Followers.Builder::class)
    fun provideFollowersBuilder(component: Profile.Component): UiComponent.Builder {
        return Followers.Builder(component)
    }

    @Provides
    @IntoMap
    @Profile.Scope
    @UiBuilder(Followings.Builder::class)
    fun provideFollowingsBuilder(component: Profile.Component): UiComponent.Builder {
        return Followings.Builder(component)
    }

    @Provides
    @IntoMap
    @Profile.Scope
    @UiBuilder(Peers.Builder::class)
    fun providePeersBuilder(component: Profile.Component): UiComponent.Builder {
        return Peers.Builder(component)
    }

    @Profile.Scope
    @Provides
    @IntoMap
    @UiBuilder(Confirmation.Builder::class)
    fun provideConfirmationBuilder(component: Profile.Component): UiComponent.Builder {
        return Confirmation.Builder(component)
    }

    @Profile.Scope
    @Provides
    fun provideEngagementRenderer(component: Profile.Component): eu.peernetwork.blog.ui.engagement.EngagementModal {
        return EngagementRenderer(component)
    }

    @Profile.Scope
    @Provides
    @IntoMap
    @UiBuilder(Screen.Builder::class)
    fun provideWindowBuilder(component: Profile.Component): UiComponent.Builder {
        return Screen.Builder(component)
    }

    @Profile.Scope
    @Provides
    @IntoMap
    @UiBuilder(Dashboard.Builder::class)
    fun provideDashboardBuilder(component: Profile.Component): UiComponent.Builder {
        return Dashboard.Builder(component)
    }

    @Profile.Scope
    @Provides
    @IntoMap
    @UiBuilder(Boost.Builder::class)
    fun provideBoostBuilder(component: Profile.Component): UiComponent.Builder {
        return Boost.Builder(component)
    }

    @Profile.Scope
    @Provides
    @IntoMap
    @UiBuilder(Checkout.Builder::class)
    fun provideCheckoutBuilder(component: Profile.Component): UiComponent.Builder {
        return Checkout.Builder(component)
    }

    @Profile.Scope
    @Provides
    @IntoMap
    @UiBuilder(Balance.Builder::class)
    fun provideBalanceBuilder(component: Profile.Component): UiComponent.Builder {
        return Balance.Builder(component)
    }

    @Profile.Scope
    @Provides
    @IntoMap
    @UiBuilder(WalletDashboard.Builder::class)
    fun provideWalletDashboardBuilder(component: Profile.Component): UiComponent.Builder {
        return WalletDashboard.Builder(component)
    }

    @Provides
    @Profile.Scope
    fun provideCheckoutBalance(component: Profile.Component): CheckoutBalance {
        return BalanceRenderer(component)
    }
}
