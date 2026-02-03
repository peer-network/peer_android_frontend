package eu.peernetwork.wallet.ui.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.core.ui.factory.UiViewModelFactory
import eu.peernetwork.wallet.ui.rate.Rate
import javax.inject.Provider

@Module
object TransactionsModule {
    @Provides
    @Transactions.Scope
    fun provideBuilderFactory(factory: Map<Class<out UiComponent.Builder>,
            @JvmSuppressWildcards Provider<UiComponent.Builder>>): UiComponentProvider.Factory {
        return UiBuilderFactory(factory)
    }

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

    @Provides
    @Transactions.Scope
    @IntoMap
    @UiBuilder(Rate.Builder::class)
    fun provideRateBuilder(component: Transactions.Component): UiComponent.Builder {
        return Rate.Builder(component)
    }
}
