package eu.peernetwork.wallet.ui.transfer

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
import eu.peernetwork.wallet.ui.balance.Balance
import javax.inject.Provider

@Module
object TransferModule {
    @Provides
    @Transfer.Scope
    fun provideBuilderFactory(factory: Map<Class<out UiComponent.Builder>,
            @JvmSuppressWildcards Provider<UiComponent.Builder>>): UiComponentProvider.Factory {
        return UiBuilderFactory(factory)
    }

    @Provides
    @Transfer.Scope
    fun provideViewModelFactory(
        classToViewModel: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @Transfer.Scope
    @UiViewModel(TransferViewModel::class)
    fun v2ViewModel(viewModel: TransferViewModel): ViewModel = viewModel

    @Provides
    @Transfer.Scope
    @IntoMap
    @UiBuilder(Balance.Builder::class)
    fun provideBalanceBuilder(component: Transfer.Component): UiComponent.Builder {
        return Balance.Builder(component)
    }
}
