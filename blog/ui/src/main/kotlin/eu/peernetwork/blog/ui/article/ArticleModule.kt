package eu.peernetwork.blog.ui.article

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.blog.ui.content.overlay.Overlay
import eu.peernetwork.blog.ui.post.Post
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.core.ui.factory.UiViewModelFactory
import javax.inject.Provider

@Module
class ArticleModule(private val context: Context) {
    @Provides
    @Article.Scope
    fun provideContext(): Context = context

    @Provides
    @Article.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Provides
    @Article.Scope
    fun provideViewModelFactory(
        classToViewModel:
        @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @Article.Scope
    @UiViewModel(ArticleViewModel::class)
    fun viewModel(viewModel: ArticleViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @Article.Scope
    @UiBuilder(Overlay.Builder::class)
    fun provideOverlayBuilder(post: Article.Component): UiComponent.Builder {
        return Overlay.Builder(post)
    }

    @Provides
    @IntoMap
    @Article.Scope
    @UiBuilder(Post.Builder::class)
    fun providePostBuilder(post: Article.Component): UiComponent.Builder {
        return Post.Builder(post)
    }
}
