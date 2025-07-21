package eu.peernetwork.blog.ui.explore

import android.content.Context
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
class ExploreModule(private val context: Context){
    @Provides
    @Explore.Scope
    fun provideContext(): Context = context

    @Provides
    @Explore.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Provides
    @Explore.Scope
    fun providerViewModelFactory(
        classToViewModel: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @Explore.Scope
    @UiViewModel(ExploreViewModel::class)
    fun viewModel(viewModel: ExploreViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @Explore.Scope
    @UiBuilder(Engagement.Builder::class)
    fun provideEngagementBuilder(component: Explore.Component): UiComponent.Builder {
        return Engagement.Builder(component)
    }

    @Provides
    @IntoMap
    @Explore.Scope
    @UiBuilder(Moderation.Builder::class)
    fun provideModerationBuilder(component: Explore.Component): UiComponent.Builder {
        return Moderation.Builder(component)
    }
}