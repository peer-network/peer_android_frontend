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
class PhotoModule(private val context: Context) {
    @Provides
    @Photo.Scope
    fun provideContext(): Context = context

    @Provides
    @Photo.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Provides
    @Photo.Scope
    fun provideViewModelFactory(
        classToViewModel:
        @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @Photo.Scope
    @UiViewModel(PhotoViewModel::class)
    fun viewModel(viewModel: PhotoViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @Photo.Scope
    @UiBuilder(Overlay.Builder::class)
    fun provideOverlayBuilder(photo: Photo.Component): UiComponent.Builder {
        return Overlay.Builder(photo)
    }

    @Provides
    @IntoMap
    @Photo.Scope
    @UiBuilder(Timeline.Builder::class)
    fun provideTimelineBuilder(photo: Photo.Component): UiComponent.Builder {
        return Timeline.Builder(photo)
    }
}
