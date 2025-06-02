package eu.peernetwork.user.ui.password.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.factory.UiViewModelFactory
import javax.inject.Provider

@Module
object PasswordUpdateModule {
    @Provides
    @PasswordUpdate.Scope
    fun provideViewModelFactory(
        classToViewModel:
        @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @PasswordUpdate.Scope
    @UiViewModel(PasswordUpdateViewModel::class)
    fun provideViewModel(viewModel: PasswordUpdateViewModel): ViewModel = viewModel
}
