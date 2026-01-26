package eu.peernetwork.app.ui.search

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
import eu.peernetwork.app.ui.screen.Screen
import eu.peernetwork.blog.ui.engagement.EngagementModal
import eu.peernetwork.blog.ui.explore.Explore
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.social.ui.connection.Connection
import eu.peernetwork.social.ui.search.member.Member
import eu.peernetwork.social.ui.search.tag.Tag
import eu.peernetwork.social.ui.search.title.Title
import eu.peernetwork.wallet.ui.balance.Balance
import eu.peernetwork.wallet.ui.confirmation.Confirmation
import eu.peernetwork.wallet.ui.dashboard.Dashboard

@Module
object SearchModule {
    @Provides
    @Search.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Search.Scope
    @Provides
    @IntoMap
    @UiBuilder(Profile.Builder::class)
    fun provideProfileBuilder(component: Search.Component): UiComponent.Builder {
        return Profile.Builder(component)
    }

    @Search.Scope
    @Provides
    @IntoMap
    @UiBuilder(Member.Builder::class)
    fun provideMemberBuilder(component: Search.Component): UiComponent.Builder {
        return Member.Builder(component)
    }

    @Search.Scope
    @Provides
    @IntoMap
    @UiBuilder(Tag.Builder::class)
    fun provideTagBuilder(component: Search.Component): UiComponent.Builder {
        return Tag.Builder(component)
    }

    @Search.Scope
    @Provides
    @IntoMap
    @UiBuilder(Title.Builder::class)
    fun provideTitleBuilder(component: Search.Component): UiComponent.Builder {
        return Title.Builder(component)
    }

    @Search.Scope
    @Provides
    @IntoMap
    @UiBuilder(Feed.Builder::class)
    fun provideFeedBuilder(component: Search.Component): UiComponent.Builder {
        return Feed.Builder(component)
    }

    @Search.Scope
    @Provides
    @IntoMap
    @UiBuilder(Explore.Builder::class)
    fun provideExploreBuilder(component: Search.Component): UiComponent.Builder {
        return Explore.Builder(component)
    }

    @Search.Scope
    @Provides
    @IntoMap
    @UiBuilder(Connection.Builder::class)
    fun provideConnectionBuilder(component: Search.Component): UiComponent.Builder {
        return Connection.Builder(component)
    }

    @Search.Scope
    @Provides
    @IntoMap
    @UiBuilder(Confirmation.Builder::class)
    fun provideConfirmationBuilder(component: Search.Component): UiComponent.Builder {
        return Confirmation.Builder(component)
    }

    @Search.Scope
    @Provides
    fun provideEngagementRenderer(component: Search.Component): EngagementModal {
        return EngagementRenderer(component)
    }

    @Search.Scope
    @Provides
    @IntoMap
    @UiBuilder(Screen.Builder::class)
    fun provideWindowBuilder(component: Search.Component): UiComponent.Builder {
        return Screen.Builder(component)
    }

    @Search.Scope
    @Provides
    @IntoMap
    @UiBuilder(Search.Builder::class)
    fun provideSearchBuilder(component: Search.Component): UiComponent.Builder {
        return Search.Builder(component)
    }

    @Search.Scope
    @Provides
    @IntoMap
    @UiBuilder(Checkout.Builder::class)
    fun provideCheckoutBuilder(component: Search.Component): UiComponent.Builder {
        return Checkout.Builder(component)
    }

    @Search.Scope
    @Provides
    @IntoMap
    @UiBuilder(Balance.Builder::class)
    fun provideBalanceBuilder(component: Search.Component): UiComponent.Builder {
        return Balance.Builder(component)
    }

    @Search.Scope
    @Provides
    @IntoMap
    @UiBuilder(Dashboard.Builder::class)
    fun provideServiceBuilder(component: Search.Component): UiComponent.Builder {
        return Dashboard.Builder(component)
    }

    @Provides
    @Search.Scope
    fun provideCheckoutBalance(component: Search.Component): CheckoutBalance {
        return BalanceRenderer(component)
    }

    @Search.Scope
    @Provides
    @IntoMap
    @UiBuilder(Boost.Builder::class)
    fun provideBoostBuilder(component: Search.Component): UiComponent.Builder {
        return Boost.Builder(component)
    }
}
