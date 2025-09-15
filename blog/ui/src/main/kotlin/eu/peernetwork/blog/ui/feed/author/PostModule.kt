package eu.peernetwork.blog.ui.feed.author

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.blog.ui.content.overlay.Overlay
import eu.peernetwork.blog.ui.content.timeline.Timeline
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.core.ui.factory.UiViewModelFactory
import javax.inject.Provider

@Module
class PostModule(private val context: Context) {
    @Provides
    @Post.Scope
    fun provideContext(): Context = context

    @Provides
    @Post.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Provides
    @Post.Scope
    fun provideViewModelFactory(
        classToViewModel:
        @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @Post.Scope
    @UiViewModel(PostViewModel::class)
    fun viewModel(viewModel: PostViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @Post.Scope
    @UiBuilder(Overlay.Builder::class)
    fun provideOverlayBuilder(post: Post.Component): UiComponent.Builder {
        return Overlay.Builder(post)
    }

    @Provides
    @IntoMap
    @Post.Scope
    @UiBuilder(Timeline.Builder::class)
    fun provideTimelineBuilder(post: Post.Component): UiComponent.Builder {
        return Timeline.Builder(post)
    }
}
