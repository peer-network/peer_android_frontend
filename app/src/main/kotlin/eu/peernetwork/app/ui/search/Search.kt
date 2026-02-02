package eu.peernetwork.app.ui.search

import android.content.Context
import eu.peernetwork.ads.ui.boost.Boost
import eu.peernetwork.ads.ui.checkout.Checkout
import eu.peernetwork.app.provider.ApplicationProvider
import eu.peernetwork.app.ui.feed.Feed
import eu.peernetwork.app.ui.profile.Profile
import eu.peernetwork.app.ui.settings.SettingsEvent
import eu.peernetwork.app.ui.screen.Screen
import eu.peernetwork.blog.ui.explore.Explore
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.social.ui.connection.Connection
import eu.peernetwork.social.ui.search.member.Member
import eu.peernetwork.social.ui.search.tag.Tag
import eu.peernetwork.social.ui.search.title.Title
import eu.peernetwork.wallet.ui.balance.Balance
import eu.peernetwork.wallet.ui.confirmation.Confirmation
import eu.peernetwork.wallet.ui.dashboard.Dashboard

interface Search : ApplicationProvider {
    fun settingsEvent(): SettingsEvent

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Search::class ],
        modules = [ SearchModule::class ]
    )
    interface Component : UiComponentProvider,
        Search,
        Title,
        Member,
        Tag,
        Profile,
        Feed,
        Explore,
        Connection,
        Confirmation,
        Boost,
        Dashboard,
        Balance,
        Checkout,
        Screen

    class Builder(private val dependency: Search) : UiComponent.DefaultBuilder<Search, Component>() {
        override fun build(context: Context): Component {
            return DaggerSearch_Component.builder().search(dependency).build()
        }
    }
}
