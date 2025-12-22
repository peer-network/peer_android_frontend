package eu.peernetwork.app.ui.profile

import android.content.Context
import eu.peernetwork.ads.ui.boost.Boost
import eu.peernetwork.ads.ui.checkout.Checkout
import eu.peernetwork.ads.ui.dashboard.Dashboard
import eu.peernetwork.app.provider.ApplicationProvider
import eu.peernetwork.app.ui.feed.Feed
import eu.peernetwork.app.ui.search.Search
import eu.peernetwork.app.ui.settings.Settings
import eu.peernetwork.app.ui.settings.SettingsEvent
import eu.peernetwork.app.ui.screen.Screen
import eu.peernetwork.blog.ui.article.Article
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.social.ui.connection.Connection
import eu.peernetwork.social.ui.followers.Followers
import eu.peernetwork.social.ui.followings.Followings
import eu.peernetwork.social.ui.peers.Peers
import eu.peernetwork.user.ui.user.User
import eu.peernetwork.wallet.ui.balance.Balance
import eu.peernetwork.wallet.ui.confirmation.Confirmation
import eu.peernetwork.wallet.ui.service.Service

interface Profile : ApplicationProvider {
    fun settingsEvent(): SettingsEvent

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Profile::class ],
        modules = [ ProfileModule::class ]
    )
    interface Component : Profile,
        UiComponentProvider,
        User,
        Article,
        Search,
        Settings,
        Followers,
        Followings,
        Peers,
        Connection,
        Confirmation,
        Dashboard,
        Boost,
        Balance,
        Service,
        Checkout,
        Feed,
        Screen

    class Builder(private val dependency: Profile) : UiComponent.DefaultBuilder<Profile, Component>() {
        override fun build(context: Context): Component {
            return DaggerProfile_Component.builder().profile(dependency).build()
        }
    }
}
