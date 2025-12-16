package eu.peernetwork.social.ui.followings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.core.ui.annotation.UiViewModel
import javax.inject.Provider
import eu.peernetwork.core.ui.factory.UiViewModelFactory

@Module
object FollowingsModule {
    @Provides
    @Followings.Scope
    fun provideViewModelFactory(
        providers: @JvmSuppressWildcards Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(providers)
    }

    @Provides
    @IntoMap
    @Followings.Scope
    @UiViewModel(FollowingsViewModel::class)
    fun viewModel(viewModel: FollowingsViewModel): ViewModel = viewModel
}