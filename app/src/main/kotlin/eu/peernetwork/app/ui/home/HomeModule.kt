package eu.peernetwork.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.app.ui.composer.Composer
import eu.peernetwork.app.ui.content.Content
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.core.ui.factory.UiViewModelFactory
import eu.peernetwork.app.ui.feed.Feed
import eu.peernetwork.app.ui.messaging.Messaging
import eu.peernetwork.app.ui.profile.Profile
import eu.peernetwork.app.ui.renderer.EngagementRenderer
import eu.peernetwork.app.ui.search.Search
import eu.peernetwork.app.ui.wallet.Wallet
import eu.peernetwork.messaging.ui.chat.Chat
import eu.peernetwork.social.ui.feedback.Feedback
import eu.peernetwork.wallet.ui.confirmation.Confirmation
import eu.peernetwork.wallet.ui.reward.Reward
import javax.inject.Provider

@Module
object HomeModule {
    @Provides
    @Home.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Provides
    @Home.Scope
    fun provideViewModelFactory(
        classToViewModel:
        @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @Home.Scope
    @UiViewModel(HomeViewModel::class)
    fun viewModel(viewModel: HomeViewModel): ViewModel = viewModel

    @Home.Scope
    @Provides
    @IntoMap
    @UiBuilder(Feed.Builder::class)
    fun provideFeedBuilder(component: Home.Component): UiComponent.Builder {
        return Feed.Builder(component)
    }

    @Home.Scope
    @Provides
    @IntoMap
    @UiBuilder(Profile.Builder::class)
    fun provideProfileBuilder(component: Home.Component): UiComponent.Builder {
        return Profile.Builder(component)
    }

    @Home.Scope
    @Provides
    @IntoMap
    @UiBuilder(Reward.Builder::class)
    fun provideRewardBuilder(component: Home.Component): UiComponent.Builder {
        return Reward.Builder(component)
    }

    @Home.Scope
    @Provides
    @IntoMap
    @UiBuilder(Composer.Builder::class)
    fun provideComposerBuilder(component: Home.Component): UiComponent.Builder {
        return Composer.Builder(component)
    }

    @Home.Scope
    @Provides
    @IntoMap
    @UiBuilder(Wallet.Builder::class)
    fun provideWalletBuilder(component: Home.Component): UiComponent.Builder {
        return Wallet.Builder(component)
    }

    @Home.Scope
    @Provides
    @IntoMap
    @UiBuilder(Search.Builder::class)
    fun provideSearchBuilder(component: Home.Component): UiComponent.Builder {
        return Search.Builder(component)
    }

    @Home.Scope
    @Provides
    @IntoMap
    @UiBuilder(Chat.Builder::class)
    fun provideChatBuilder(component: Home.Component): UiComponent.Builder {
        return Chat.Builder(component)
    }

    @Home.Scope
    @Provides
    @IntoMap
    @UiBuilder(Messaging.Builder::class)
    fun provideMessagingBuilder(component: Home.Component): UiComponent.Builder {
        return Messaging.Builder(component)
    }

    @Home.Scope
    @Provides
    @IntoMap
    @UiBuilder(Feedback.Builder::class)
    fun provideFeedbackBuilder(component: Home.Component): UiComponent.Builder {
        return Feedback.Builder(component)
    }

    @Provides
    @IntoMap
    @Home.Scope
    @UiBuilder(Content.Builder::class)
    fun providePostBuilder(component: Home.Component): UiComponent.Builder {
        return Content.Builder(component)
    }

    @Provides
    @IntoMap
    @Home.Scope
    @UiBuilder(Confirmation.Builder::class)
    fun provideConfirmationBuilder(component: Home.Component): UiComponent.Builder {
        return Confirmation.Builder(component)
    }

    @Provides
    @Home.Scope
    fun provideEngagementRenderer(component: Home.Component): eu.peernetwork.blog.ui.engagement.EngagementModal {
        return EngagementRenderer(component)
    }
}
