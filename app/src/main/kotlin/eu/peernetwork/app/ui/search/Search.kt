package eu.peernetwork.app.ui.search

import android.content.Context
import eu.peernetwork.app.ui.profile.Profile
import eu.peernetwork.blog.ui.provider.BlogProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.persistence.domain.provider.PreferenceProvider
import eu.peernetwork.social.ui.provider.SocialProvider
import eu.peernetwork.social.ui.search.member.Member
import eu.peernetwork.social.ui.search.tag.Tag
import eu.peernetwork.social.ui.search.title.Title
import eu.peernetwork.user.ui.provider.UserProvider

interface Search : PreferenceProvider, UserProvider, BlogProvider, SocialProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Search::class ],
        modules = [ SearchModule::class ]
    )
    interface Component : UiComponentProvider, Search, Title, Member, Tag, Profile

    class Builder(private val dependency: Search) : UiComponent.DefaultBuilder<Search, Component>() {
        override fun build(context: Context): Component {
            return DaggerSearch_Component.builder().search(dependency).build()
        }
    }
}
