package eu.peernetwork.blog.ui.engagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.blog.ui.interaction.overview.Overview
import eu.peernetwork.blog.ui.comment.Comment
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.core.ui.factory.UiViewModelFactory
import javax.inject.Provider

@Module
object EngagementModule {
    @Provides
    @Engagement.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Provides
    @Engagement.Scope
    fun provideViewModelFactory(
        providers: @JvmSuppressWildcards Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(providers)
    }

    @Provides
    @IntoMap
    @Engagement.Scope
    @UiViewModel(EngagementViewModel::class)
    fun viewModel(viewModel: EngagementViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @Engagement.Scope
    @UiBuilder(Comment.Builder::class)
    fun provideCommentBuilder(component: Engagement.Component): UiComponent.Builder {
        return Comment.Builder(component)
    }

    @Provides
    @IntoMap
    @Engagement.Scope
    @UiBuilder(Overview.Builder::class)
    fun provideOverviewBuilder(component: Engagement.Component): UiComponent.Builder {
        return Overview.Builder(component)
    }
}
