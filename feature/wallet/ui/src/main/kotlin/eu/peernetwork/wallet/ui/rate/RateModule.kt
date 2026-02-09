package eu.peernetwork.wallet.ui.rate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.factory.UiViewModelFactory
import javax.inject.Provider

@Module
object RateModule {
    @Provides
    @Rate.Scope
    fun provideViewModelFactory(
        classToViewModel: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @Rate.Scope
    @UiViewModel(RateViewModel::class)
    fun viewModel(viewModel: RateViewModel): ViewModel = viewModel
}
