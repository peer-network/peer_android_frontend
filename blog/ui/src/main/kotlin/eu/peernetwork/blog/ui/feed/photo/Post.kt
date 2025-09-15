package eu.peernetwork.blog.ui.feed.photo

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.blog.ui.engagement.EngagementDialog
import eu.peernetwork.blog.ui.content.overlay.Overlay
import eu.peernetwork.blog.ui.content.timeline.Timeline
import eu.peernetwork.blog.ui.provider.BlogProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider

interface Post : BlogProvider {
    fun engagementConfirmation(): EngagementDialog

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [Post::class],
        modules = [PostModule::class]
    )
    interface Component : Post, UiComponentProvider, Overlay, Timeline {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Post) : UiComponent.DefaultBuilder<Post, Component>() {
        override fun build(context: Context): Component {
            return DaggerPost_Component.builder()
                .post(dependency)
                .postModule(PostModule(context))
                .build()
        }
    }
}
