package eu.peernetwork.app.ui.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.core.ui.annotation.UiController
import eu.peernetwork.core.ui.factory.UiControllerFactory

@Module
object SetupModule {
    @Provides
    @Setup.Scope
    fun provideViewModelFactory(factory: UiControllerFactory): ViewModelProvider.Factory = factory

    @Provides
    @IntoMap
    @Setup.Scope
    @UiController(SetupViewModel::class)
    fun viewModel(viewModel: SetupViewModel): ViewModel = viewModel
}
