package eu.peernetwork.blog.ui.creator

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.factory.UiViewModelFactory
import javax.inject.Provider

@Module
class CreatorModule(private val context: Context) {
    @Provides
    @Creator.Scope
    fun provideContext(): Context = context

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
}
