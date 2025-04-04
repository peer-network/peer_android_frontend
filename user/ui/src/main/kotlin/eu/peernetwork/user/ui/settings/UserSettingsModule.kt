package eu.peernetwork.user.ui.settings

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
class UserSettingsModule(private val context: Context) {
    @Provides
    @UserSettings.Scope
    fun provideContext(): Context = context

    @Provides
    @UserSettings.Scope
    fun provideViewModelFactory(
        classToViewModel:
        @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @UserSettings.Scope
    @UiViewModel(UserSettingsViewModel::class)
    fun provideViewModel(viewModel: UserSettingsViewModel): ViewModel = viewModel
}
