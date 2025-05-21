package eu.peernetwork.media.ui.selector.photo

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
object PhotoModule {
    @Provides
    @Photo.Scope
    fun provideBuilderFactory(factory: Map<Class<out UiComponent.Builder>,
            @JvmSuppressWildcards Provider<UiComponent.Builder>>): UiComponentProvider.Factory {
        return UiBuilderFactory(factory)
    }

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
    @UiBuilder(Thumbnail.Builder::class)
    fun provideThumbnailBuilder(component: Photo.Component): UiComponent.Builder {
        return Thumbnail.Builder(component)
    }
}
