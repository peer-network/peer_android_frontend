package eu.peernetwork.blog.ui.feed.author

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.blog.ui.engagement.EngagementDialog
import eu.peernetwork.blog.ui.content.overlay.Overlay
import eu.peernetwork.blog.ui.content.timeline.Timeline
import eu.peernetwork.blog.ui.post.Post
import eu.peernetwork.blog.ui.provider.BlogProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider

interface AuthorPost : BlogProvider {
    fun engagementConfirmation(): EngagementDialog

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [AuthorPost::class ],
        modules = [ AuthorPostModule::class ]
    )
    interface Component : AuthorPost, UiComponentProvider, Overlay, Timeline, Post {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: AuthorPost) : UiComponent.DefaultBuilder<AuthorPost, Component>() {
        override fun build(context: Context): Component {
            return DaggerAuthorPost_Component.builder()
                .authorPost(dependency)
                .authorPostModule(AuthorPostModule(context))
                .build()
        }
    }
}
