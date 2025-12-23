package eu.peernetwork.blog.ui.detail

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.blog.ui.engagement.EngagementDialog
import eu.peernetwork.blog.ui.post.Post
import eu.peernetwork.blog.ui.provider.BlogProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider

interface Detail : BlogProvider {
    fun context(): Context

    fun engagementConfirmation(): EngagementDialog

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [Detail::class],
        modules = [DetailModule::class]
    )
    interface Component : Detail, UiComponentProvider, Post {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Detail) : UiComponent.DefaultBuilder<Detail, Component>() {
        override fun build(context: Context): Component {
            return DaggerDetail_Component.builder()
                .detail(dependency)
                .build()
        }
    }
}
