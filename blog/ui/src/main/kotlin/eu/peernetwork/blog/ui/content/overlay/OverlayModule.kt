package eu.peernetwork.blog.ui.content.overlay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
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
object OverlayModule {
    @Provides
    @Overlay.Scope
    fun provideViewModelFactory(
        classToViewModel:
        @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @Overlay.Scope
    @UiViewModel(OverlayViewModel::class)
    fun viewModel(viewModel: OverlayViewModel): ViewModel = viewModel

    @Provides
    @Overlay.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Provides
    @IntoMap
    @Overlay.Scope
    @UiBuilder(Engagement.Builder::class)
    fun provideEngagementBuilder(component: Overlay.Component): UiComponent.Builder {
        return Engagement.Builder(component)
    }

    @Provides
    @IntoMap
    @Overlay.Scope
    @UiBuilder(Moderation.Builder::class)
    fun provideModerationBuilder(component: Overlay.Component): UiComponent.Builder {
        return Moderation.Builder(component)
    }
}
