package eu.peernetwork.blog.ui.timeline

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.blog.ui.advert.Advert
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
class TimelineModule(private val context: Context) {
    @Provides
    @Timeline.Scope
    fun provideContext(): Context = context

    @Provides
    @Timeline.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Provides
    @Timeline.Scope
    fun provideViewModelFactory(
        classToViewModel:
        @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @Timeline.Scope
    @UiViewModel(TimelineViewModel::class)
    fun viewModel(viewModel: TimelineViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @Timeline.Scope
    @UiBuilder(Post.Builder::class)
    fun providePostBuilder(component: Timeline.Component): UiComponent.Builder {
        return Post.Builder(component)
    }

    @Provides
    @IntoMap
    @Timeline.Scope
    @UiBuilder(Overlay.Builder::class)
    fun provideOverlayBuilder(component: Timeline.Component): UiComponent.Builder {
        return Overlay.Builder(component)
    }

    @Provides
    @IntoMap
    @Timeline.Scope
    @UiBuilder(Advert.Builder::class)
    fun provideAdsBuilder(component: Timeline.Component): UiComponent.Builder {
        return Advert.Builder(component)
    }
}
