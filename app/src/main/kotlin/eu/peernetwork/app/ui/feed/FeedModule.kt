package eu.peernetwork.app.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.ads.ui.boost.Boost
import eu.peernetwork.ads.ui.checkout.Checkout
import eu.peernetwork.ads.ui.checkout.CheckoutBalance
import eu.peernetwork.app.ui.profile.Profile
import eu.peernetwork.app.ui.renderer.BalanceRenderer
import eu.peernetwork.app.ui.renderer.ConnectionRenderer
import eu.peernetwork.app.ui.renderer.EngagementRenderer
import eu.peernetwork.app.ui.search.Search
import eu.peernetwork.app.ui.screen.Screen
import eu.peernetwork.blog.ui.engagement.EngagementDialog
import eu.peernetwork.blog.ui.post.PostUserConnection
import eu.peernetwork.blog.ui.timeline.Timeline
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.core.ui.factory.UiViewModelFactory
import eu.peernetwork.social.ui.connection.Connection
import eu.peernetwork.wallet.ui.balance.Balance
import eu.peernetwork.wallet.ui.confirmation.Confirmation
import javax.inject.Provider

@Module
object FeedModule {
    @Provides
    @Feed.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Provides
    @Feed.Scope
    fun provideViewModelFactory(
        classToViewModel:
        @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @Feed.Scope
    @UiViewModel(FeedViewModel::class)
    fun viewModel(viewModel: FeedViewModel): ViewModel = viewModel

    @Feed.Scope
    @Provides
    @IntoMap
    @UiBuilder(Feed.Builder::class)
    fun provideFeedBuilder(component: Feed.Component): UiComponent.Builder {
        return Feed.Builder(component)
    }

    @Feed.Scope
    @Provides
    @IntoMap
    @UiBuilder(Profile.Builder::class)
    fun provideProfileBuilder(component: Feed.Component): UiComponent.Builder {
        return Profile.Builder(component)
    }

    @Feed.Scope
    @Provides
    @IntoMap
    @UiBuilder(Search.Builder::class)
    fun provideSearchBuilder(component: Feed.Component): UiComponent.Builder {
        return Search.Builder(component)
    }

    @Feed.Scope
    @Provides
    @IntoMap
    @UiBuilder(Connection.Builder::class)
    fun provideConnectionBuilder(component: Feed.Component): UiComponent.Builder {
        return Connection.Builder(component)
    }

    @Feed.Scope
    @Provides
    @IntoMap
    @UiBuilder(Confirmation.Builder::class)
    fun provideConfirmationBuilder(component: Feed.Component): UiComponent.Builder {
        return Confirmation.Builder(component)
    }

    @Feed.Scope
    @Provides
    @IntoMap
    @UiBuilder(Timeline.Builder::class)
    fun provideTimelineBuilder(component: Feed.Component): UiComponent.Builder {
        return Timeline.Builder(component)
    }

    @Feed.Scope
    @Provides
    fun provideEngagementRenderer(component: Feed.Component): EngagementDialog {
        return EngagementRenderer(component)
    }

    @Feed.Scope
    @Provides
    fun providePostUserFollow(): PostUserConnection {
        return ConnectionRenderer()
    }

    @Feed.Scope
    @Provides
    @IntoMap
    @UiBuilder(Screen.Builder::class)
    fun provideScreenBuilder(component: Feed.Component): UiComponent.Builder {
        return Screen.Builder(component)
    }

    @Feed.Scope
    @Provides
    @IntoMap
    @UiBuilder(Checkout.Builder::class)
    fun provideCheckoutBuilder(component: Feed.Component): UiComponent.Builder {
        return Checkout.Builder(component)
    }

    @Feed.Scope
    @Provides
    @IntoMap
    @UiBuilder(Balance.Builder::class)
    fun provideBalanceBuilder(component: Feed.Component): UiComponent.Builder {
        return Balance.Builder(component)
    }

    @Provides
    @Feed.Scope
    fun provideCheckoutBalance(component: Feed.Component): CheckoutBalance {
        return BalanceRenderer(component)
    }

    @Feed.Scope
    @Provides
    @IntoMap
    @UiBuilder(Boost.Builder::class)
    fun provideBoostBuilder(component: Feed.Component): UiComponent.Builder {
        return Boost.Builder(component)
    }
}
