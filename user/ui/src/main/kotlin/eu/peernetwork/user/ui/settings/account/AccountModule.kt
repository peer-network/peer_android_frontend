package eu.peernetwork.user.ui.settings.account

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.factory.UiViewModelFactory
import javax.inject.Provider

@Module
class AccountModule(private val context: Context) {
    @Provides
    @Account.Scope
    fun provideContext(): Context = context

    @Provides
    @Account.Scope
    fun provideViewModelFactory(
        classToViewModel:
        @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @Account.Scope
    @UiViewModel(AccountViewModel::class)
    fun provideViewModel(viewModel: AccountViewModel): ViewModel = viewModel
}
