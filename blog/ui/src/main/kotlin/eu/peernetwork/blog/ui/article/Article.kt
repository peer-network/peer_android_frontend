package eu.peernetwork.blog.ui.article

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.blog.ui.engagement.EngagementDialog
import eu.peernetwork.blog.ui.post.Post
import eu.peernetwork.blog.ui.provider.BlogProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider

interface Article : BlogProvider {
    fun engagementConfirmation(): EngagementDialog

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [Article::class ],
        modules = [ ArticleModule::class ]
    )
    interface Component : Article, UiComponentProvider, Post {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Article) : UiComponent.DefaultBuilder<Article, Component>() {
        override fun build(context: Context): Component {
            return DaggerArticle_Component.builder()
                .article(dependency)
                .articleModule(ArticleModule(context))
                .build()
        }
    }
}
