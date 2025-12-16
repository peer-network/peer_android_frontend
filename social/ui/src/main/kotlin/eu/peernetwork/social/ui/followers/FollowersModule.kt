package eu.peernetwork.social.ui.followers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.core.ui.annotation.UiViewModel
import javax.inject.Provider
import eu.peernetwork.core.ui.factory.UiViewModelFactory

@Module
object FollowersModule {
    @Provides
    @Followers.Scope
    fun provideViewModelFactory(
        providers: @JvmSuppressWildcards Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(providers)
    }

    @Provides
    @IntoMap
    @Followers.Scope
    @UiViewModel(FollowersViewModel::class)
    fun viewModel(viewModel: FollowersViewModel): ViewModel = viewModel
}