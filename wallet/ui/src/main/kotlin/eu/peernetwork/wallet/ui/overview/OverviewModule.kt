package eu.peernetwork.wallet.ui.overview

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
import eu.peernetwork.wallet.ui.transfer.Transfer
import javax.inject.Provider

@Module
object OverviewModule {
    @Provides
    @Overview.Scope
    fun provideBuilderFactory(factory: Map<Class<out UiComponent.Builder>,
            @JvmSuppressWildcards Provider<UiComponent.Builder>>): UiComponentProvider.Factory {
        return UiBuilderFactory(factory)
    }

    @Provides
    @Overview.Scope
    fun provideViewModelFactory(
        classToViewModel:
        @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @Overview.Scope
    @UiViewModel(OverviewViewModel::class)
    fun viewModel(viewModel: OverviewViewModel): ViewModel = viewModel

    @Provides
    @Overview.Scope
    @IntoMap
    @UiBuilder(Transfer.Builder::class)
    fun provideTransferBuilder(component: Overview.Component): UiComponent.Builder {
        return Transfer.Builder(component)
    }
}
