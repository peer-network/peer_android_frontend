package eu.peernetwork.blog.ui.advert

import android.content.Context
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
class AdvertModule(private val context: Context) {
    @Provides
    @Advert.Scope
    fun provideContext(): Context = context

    @Provides
    @Advert.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Provides
    @Advert.Scope
    fun provideViewModelFactory(
        classToViewModel:
        @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @Advert.Scope
    @UiViewModel(AdvertViewModel::class)
    fun viewModel(viewModel: AdvertViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @Advert.Scope
    @UiBuilder(Post.Builder::class)
    fun providePostBuilder(component: Advert.Component): UiComponent.Builder {
        return Post.Builder(component)
    }
}
