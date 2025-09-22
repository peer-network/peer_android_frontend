package eu.peernetwork.blog.ui.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.blog.ui.interaction.listing.Listing
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.core.ui.factory.UiViewModelFactory
import javax.inject.Provider

@Module
object CommentModule {
    @Provides
    @Comment.Scope
    fun provideViewModelFactory(
        classToViewModel:
        @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ) : ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @Comment.Scope
    @UiViewModel(CommentViewModel::class)
    fun viewModel(viewModel: CommentViewModel): ViewModel = viewModel

    @Provides
    @Comment.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Provides
    @IntoMap
    @Comment.Scope
    @UiBuilder(Listing.Builder::class)
    fun provideListingBuilder(component: Comment.Component): UiComponent.Builder {
        return Listing.Builder(component)
    }
}