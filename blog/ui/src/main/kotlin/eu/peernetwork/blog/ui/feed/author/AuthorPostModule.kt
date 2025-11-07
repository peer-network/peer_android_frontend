package eu.peernetwork.blog.ui.feed.author

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.blog.ui.content.overlay.Overlay
import eu.peernetwork.blog.ui.content.timeline.Timeline
import eu.peernetwork.blog.ui.post.Post
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.core.ui.factory.UiViewModelFactory
import javax.inject.Provider

@Module
class AuthorPostModule(private val context: Context) {
    @Provides
    @AuthorPost.Scope
    fun provideContext(): Context = context

    @Provides
    @AuthorPost.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Provides
    @AuthorPost.Scope
    fun provideViewModelFactory(
        classToViewModel:
        @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @AuthorPost.Scope
    @UiViewModel(AuthorPostViewModel::class)
    fun viewModel(viewModel: AuthorPostViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @AuthorPost.Scope
    @UiBuilder(Overlay.Builder::class)
    fun provideOverlayBuilder(post: AuthorPost.Component): UiComponent.Builder {
        return Overlay.Builder(post)
    }

    @Provides
    @IntoMap
    @AuthorPost.Scope
    @UiBuilder(Timeline.Builder::class)
    fun provideTimelineBuilder(post: AuthorPost.Component): UiComponent.Builder {
        return Timeline.Builder(post)
    }

    @Provides
    @IntoMap
    @AuthorPost.Scope
    @UiBuilder(Post.Builder::class)
    fun providePostBuilder(post: AuthorPost.Component): UiComponent.Builder {
        return Post.Builder(post)
    }
}
