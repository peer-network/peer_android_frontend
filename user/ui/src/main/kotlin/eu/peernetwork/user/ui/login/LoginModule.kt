package eu.peernetwork.user.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.factory.UiViewModelFactory

@Module
interface LoginModule {
    @Binds
    @Login.Scope
    fun bindViewModelFactory(factory: UiViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @Login.Scope
    @UiViewModel(LoginViewModel::class)
    fun bindViewModel(viewModel: LoginViewModel): ViewModel
}
