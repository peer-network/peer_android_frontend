package eu.peernetwork.blog.ui.post

import android.content.Context
import eu.peernetwork.blog.ui.engagement.Engagement
import eu.peernetwork.blog.ui.engagement.EngagementDialog
import eu.peernetwork.blog.ui.moderation.Moderation
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
    interface Component : Post, UiComponentProvider, Engagement, Moderation

    class Builder(private val dependency: Post) : UiComponent.DefaultBuilder<Post, Component>() {
        override fun build(context: Context): Component {
            return DaggerPost_Component.builder()
                .post(dependency)
                .build()
        }
    }
}
