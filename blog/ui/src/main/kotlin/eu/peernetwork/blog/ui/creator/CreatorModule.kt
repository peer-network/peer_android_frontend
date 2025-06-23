package eu.peernetwork.blog.ui.creator

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.blog.ui.author.Author
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.core.ui.factory.UiViewModelFactory
import javax.inject.Provider

@Module
class CreatorModule(private val context: Context) {
    @Provides
    @Creator.Scope
    fun provideContext(): Context = context

    @Provides
    @Creator.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Provides
    @Creator.Scope
    fun provideViewModelFactory(
        classToViewModel:
        @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @Creator.Scope
    @UiViewModel(CreatorViewModel::class)
    fun viewModel(viewModel: CreatorViewModel): ViewModel = viewModel

    @Creator.Scope
    @Provides
    @IntoMap
    @UiBuilder(Author.Builder::class)
    fun provideBiographyBuilder(component: Creator.Component): UiComponent.Builder {
        return Author.Builder(component)
    }
}
