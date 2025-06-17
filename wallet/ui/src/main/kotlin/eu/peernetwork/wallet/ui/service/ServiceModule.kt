package eu.peernetwork.wallet.ui.service

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
object ServiceModule {
    @Provides
    @Service.Scope
    fun provideBuilderFactory(factory: Map<Class<out UiComponent.Builder>,
            @JvmSuppressWildcards Provider<UiComponent.Builder>>): UiComponentProvider.Factory {
        return UiBuilderFactory(factory)
    }

    @Provides
    @Service.Scope
    fun provideViewModelFactory(
        classToViewModel: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @Service.Scope
    @UiViewModel(ServiceViewModel::class)
    fun viewModel(viewModel: ServiceViewModel): ViewModel = viewModel

    @Provides
    @Service.Scope
    @IntoMap
    @UiBuilder(Transfer.Builder::class)
    fun provideTransferBuilder(component: Service.Component): UiComponent.Builder {
        return Transfer.Builder(component)
    }
}
