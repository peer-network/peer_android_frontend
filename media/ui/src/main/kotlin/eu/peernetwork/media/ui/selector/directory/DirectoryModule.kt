package eu.peernetwork.media.ui.selector.directory

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
object DirectoryModule {
    @Provides
    @Directory.Scope
    fun provideBuilderFactory(factory: Map<Class<out UiComponent.Builder>,
            @JvmSuppressWildcards Provider<UiComponent.Builder>>): UiComponentProvider.Factory {
        return UiBuilderFactory(factory)
    }

    @Provides
    @Directory.Scope
    fun provideViewModelFactory(
        classToViewModel:
        @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @Directory.Scope
    @UiViewModel(DirectoryViewModel::class)
    fun viewModel(viewModel: DirectoryViewModel): ViewModel = viewModel

    @Directory.Scope
    @Provides
    @IntoMap
    @UiBuilder(Thumbnail.Builder::class)
    fun provideThumbnailBuilder(component: Directory.Component): UiComponent.Builder {
        return Thumbnail.Builder(component)
    }
}
