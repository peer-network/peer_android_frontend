package eu.peernetwork.blog.ui.moderation

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
import javax.inject.Provider

@Module
object ModerationModule {
    @Provides
    @Moderation.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Provides
    @Moderation.Scope
    fun provideViewModelFactory(
        providers: @JvmSuppressWildcards Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(providers)
    }

    @Provides
    @IntoMap
    @Moderation.Scope
    @UiViewModel(ModerationViewModel::class)
    fun viewModel(viewModel: ModerationViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @Moderation.Scope
    @UiBuilder(Moderation.Builder::class)
    fun provideModerationBuilder(component: Moderation.Component): UiComponent.Builder {
        return Moderation.Builder(component)
    }
}