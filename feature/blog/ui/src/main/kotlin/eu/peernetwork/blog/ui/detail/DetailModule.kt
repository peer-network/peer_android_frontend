package eu.peernetwork.blog.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.blog.ui.post.Post
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
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

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
    @IntoMap
    @Detail.Scope
    @UiBuilder(Post.Builder::class)
    fun providePostBuilder(component: Detail.Component): UiComponent.Builder {
        return Post.Builder(component)
    }
}
