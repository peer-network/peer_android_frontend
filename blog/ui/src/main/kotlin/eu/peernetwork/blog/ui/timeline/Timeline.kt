package eu.peernetwork.blog.ui.timeline

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.blog.ui.engagement.EngagementModal
import eu.peernetwork.blog.ui.post.Post
import eu.peernetwork.blog.ui.provider.BlogProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider

interface Timeline : BlogProvider {
    fun engagementConfirmation(): EngagementModal

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [Timeline::class],
        modules = [TimelineModule::class]
    )
    interface Component : Timeline, UiComponentProvider, Post {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Timeline) : UiComponent.DefaultBuilder<Timeline, Component>() {
        override fun build(context: Context): Component {
            return DaggerTimeline_Component.builder()
                .timeline(dependency)
                .timelineModule(TimelineModule(context))
                .build()
        }
    }
}
