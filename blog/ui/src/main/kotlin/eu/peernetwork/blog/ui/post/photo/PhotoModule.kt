package eu.peernetwork.blog.ui.post.photo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.blog.ui.comment.Comment
import eu.peernetwork.blog.ui.engagement.Engagement
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.core.ui.factory.UiViewModelFactory
import javax.inject.Provider

@Module
object PhotoModule {
    @Provides
    @Photo.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Provides
    @Photo.Scope
    fun provideViewModelFactory(
        classToViewModel:
        @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @Photo.Scope
    @UiViewModel(PhotoViewModel::class)
    fun viewModel(viewModel: PhotoViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @Photo.Scope
    @UiBuilder(Engagement.Builder::class)
    fun provideEngagementBuilder(photo: Photo.Component): UiComponent.Builder {
        return Engagement.Builder(photo)
    }

    @Provides
    @IntoMap
    @Photo.Scope
    @UiBuilder(Comment.Builder::class)
    fun provideCommentBuilder(photo: Photo.Component): UiComponent.Builder {
        return Comment.Builder(photo)
    }
}
