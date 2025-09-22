package eu.peernetwork.blog.ui.content.timeline

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
object TimelineModule {
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
    @Timeline.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Provides
    @IntoMap
    @Timeline.Scope
    @UiBuilder(Engagement.Builder::class)
    fun provideEngagementBuilder(component: Timeline.Component): UiComponent.Builder {
        return Engagement.Builder(component)
    }

    @Provides
    @IntoMap
    @Timeline.Scope
    @UiBuilder(Moderation.Builder::class)
    fun provideModerationBuilder(component: Timeline.Component): UiComponent.Builder {
        return Moderation.Builder(component)
    }
}
