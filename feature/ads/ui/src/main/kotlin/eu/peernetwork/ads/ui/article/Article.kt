package eu.peernetwork.ads.ui.article

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.ads.ui.provider.AdsProvider
import eu.peernetwork.core.ui.component.UiComponent

interface Article : AdsProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Article::class ],
        modules = [ ArticleModule::class ]
    )
    interface Component : Article {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Article) : UiComponent.DefaultBuilder<Article, Component>() {
        override fun build(context: Context): Component {
            return DaggerArticle_Component.builder().article(dependency).build()
        }
    }
}
