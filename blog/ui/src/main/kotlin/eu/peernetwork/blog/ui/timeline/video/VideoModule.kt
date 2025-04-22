package eu.peernetwork.blog.ui.timeline.video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.blog.ui.engagement.Engagement
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.core.ui.factory.UiViewModelFactory
import javax.inject.Provider

@Module
object VideoModule {
    @Provides
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

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

    @Provides
    @IntoMap
    @Video.Scope
    @UiBuilder(Engagement.Builder::class)
    fun provideEngagementBuilder(component: Video.Component): UiComponent.Builder {
        return Engagement.Builder(component)
    }
}
