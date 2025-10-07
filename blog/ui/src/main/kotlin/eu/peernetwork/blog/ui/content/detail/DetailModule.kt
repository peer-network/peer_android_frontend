package eu.peernetwork.blog.ui.content.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.blog.ui.content.overlay.Overlay
import eu.peernetwork.blog.ui.engagement.Engagement
import eu.peernetwork.blog.ui.moderation.Moderation
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.core.ui.factory.UiViewModelFactory
import javax.inject.Provider

@Module
object DetailModule {
    @Provides
    @Detail.Scope
    fun provideViewModelFactory(
        classToViewModel:
        @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @Detail.Scope
    @UiViewModel(DetailViewModel::class)
    fun viewModel(viewModel: DetailViewModel): ViewModel = viewModel

    @Provides
    @Detail.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Provides
    @IntoMap
    @Detail.Scope
    @UiBuilder(Engagement.Builder::class)
    fun provideEngagementBuilder(component: Detail.Component): UiComponent.Builder {
        return Engagement.Builder(component)
    }

    @Provides
    @IntoMap
    @Detail.Scope
    @UiBuilder(Moderation.Builder::class)
    fun provideModerationBuilder(component: Detail.Component): UiComponent.Builder {
        return Moderation.Builder(component)
    }

    @Provides
    @IntoMap
    @Detail.Scope
    @UiBuilder(Overlay.Builder::class)
    fun provideOverlayBuilder(post: Detail.Component): UiComponent.Builder {
        return Overlay.Builder(post)
    }
}
