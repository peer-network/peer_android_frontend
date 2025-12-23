package eu.peernetwork.blog.ui.explore

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.blog.ui.engagement.Engagement
import eu.peernetwork.blog.ui.engagement.EngagementDialog
import eu.peernetwork.blog.ui.moderation.Moderation
import eu.peernetwork.blog.ui.post.Post
import eu.peernetwork.blog.ui.provider.BlogProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider

interface Explore: BlogProvider {
    fun engagementConfirmation(): EngagementDialog

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Explore::class ],
        modules = [ ExploreModule::class ]
    )
    interface Component : Explore,
        UiComponentProvider,
        Engagement,
        Moderation,
        Post {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Explore) : UiComponent.DefaultBuilder<Explore, Component>() {
        override fun build(context: Context): Component {
            return DaggerExplore_Component.builder()
                .explore(dependency)
                .exploreModule(ExploreModule(context))
                .build()
        }
    }
}
