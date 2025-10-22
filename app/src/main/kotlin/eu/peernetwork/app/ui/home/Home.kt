package eu.peernetwork.app.ui.home

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.app.provider.ApplicationProvider
import eu.peernetwork.app.ui.composer.Composer
import eu.peernetwork.app.ui.content.Content
import eu.peernetwork.app.ui.profile.Profile
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.app.ui.feed.Feed
import eu.peernetwork.app.ui.messaging.Messaging
import eu.peernetwork.app.ui.search.Search
import eu.peernetwork.app.ui.settings.SettingsEvent
import eu.peernetwork.app.ui.wallet.Wallet
import eu.peernetwork.messaging.ui.chat.Chat
import eu.peernetwork.social.ui.feedback.Feedback
import eu.peernetwork.wallet.ui.confirmation.Confirmation
import eu.peernetwork.wallet.ui.reward.Reward

interface Home : ApplicationProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Home::class ],
        modules = [ HomeModule::class ]
    )
    interface Component : Home,
        Feed,
        Profile,
        Messaging,
        UiComponentProvider,
        Reward,
        Composer,
        Content,
        Wallet,
        Search,
        Chat,
        Confirmation,
        Feedback {
        @dagger.Component.Builder
        interface Builder {
            fun home(home: Home): Builder

            @dagger.BindsInstance
            fun event(event: SettingsEvent): Builder

            fun build(): Component
        }

        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Home) : UiComponent.ParameterizedBuilder<SettingsEvent, Home, Component>() {
        override fun build(context: Context, param: SettingsEvent): Component {
            return DaggerHome_Component.builder()
                .home(dependency)
                .event(param)
                .build()
        }
    }
}
