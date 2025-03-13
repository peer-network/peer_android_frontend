package eu.peernetwork.user.ui.registeration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.factory.UiViewModelFactory

@Module
interface RegistrationModule {
    @Binds
    @Registration.Scope
    fun bindViewModelFactory(factory: UiViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @Registration.Scope
    @UiViewModel(RegistrationViewModel::class)
    fun bindViewModel(viewModel: RegistrationViewModel): ViewModel
}
