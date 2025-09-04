package eu.peernetwork.blog.ui.engagement

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.blog.ui.interactions.overview.Overview
import eu.peernetwork.blog.ui.comment.Comment
import eu.peernetwork.blog.ui.provider.BlogProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider

interface Engagement : BlogProvider {
    fun engagementConfirmation(): EngagementDialog

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [Engagement::class],
        modules = [EngagementModule::class]
    )
    interface Component : Engagement, UiComponentProvider, Comment, Overview {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Engagement) : UiComponent.DefaultBuilder<Engagement, Component>() {
        override fun build(context: Context): Component {
            return DaggerEngagement_Component.builder().engagement(dependency).build()
        }
    }
}