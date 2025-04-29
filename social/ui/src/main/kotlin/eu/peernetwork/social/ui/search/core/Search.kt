package eu.peernetwork.social.ui.search.core

import android.content.Context
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.social.ui.search.member.Member
import eu.peernetwork.social.ui.search.tag.Tag
import eu.peernetwork.social.ui.search.title.Title

interface Search {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Search::class ],
    )
    interface Component : Search, Title, Member, Tag

    class Builder(private val dependency: Search) : UiComponent.DefaultBuilder<Search, Component>() {
        override fun build(context: Context): Component {
            TODO("Not yet implemented")
        }
    }
}
