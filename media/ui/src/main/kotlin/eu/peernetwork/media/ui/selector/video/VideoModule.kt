package eu.peernetwork.media.ui.selector.video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.core.ui.factory.UiViewModelFactory
import eu.peernetwork.media.ui.thumbnail.Thumbnail
import javax.inject.Provider

@Module
object VideoModule {
    @Provides
    @Video.Scope
    fun provideBuilderFactory(factory: Map<Class<out UiComponent.Builder>,
            @JvmSuppressWildcards Provider<UiComponent.Builder>>): UiComponentProvider.Factory {
        return UiBuilderFactory(factory)
    }

    @Provides
    @Video.Scope
    fun provideViewModelFactory(
        classToViewModel:
        @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @Video.Scope
    @UiViewModel(VideoViewModel::class)
    fun viewModel(viewModel: VideoViewModel): ViewModel = viewModel

    @Video.Scope
    @Provides
    @IntoMap
    @UiBuilder(Thumbnail.Builder::class)
    fun provideThumbnailBuilder(component: Video.Component): UiComponent.Builder {
        return Thumbnail.Builder(component)
    }
}
