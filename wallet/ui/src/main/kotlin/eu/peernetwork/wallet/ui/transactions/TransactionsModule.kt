package eu.peernetwork.wallet.ui.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.factory.UiViewModelFactory
import javax.inject.Provider

@Module
object TransactionsModule {
    @Provides
    @Transactions.Scope
    fun provideViewModelFactory(
        classToViewModel: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @Transactions.Scope
    @UiViewModel(TransactionsViewModel::class)
    fun viewModel(viewModel: TransactionsViewModel): ViewModel = viewModel
}
