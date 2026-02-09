package eu.peernetwork.social.ui.peers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.component.UiComponent
import javax.inject.Provider
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.core.ui.factory.UiViewModelFactory
import eu.peernetwork.social.ui.followers.Followers
import eu.peernetwork.social.ui.followers.FollowersViewModel
import eu.peernetwork.social.ui.followings.Followings
import eu.peernetwork.social.ui.followings.FollowingsViewModel

@Module
object PeersModule {
    @Provides
    @Peers.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Provides
    @Peers.Scope
    fun provideViewModelFactory(
        providers: @JvmSuppressWildcards Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(providers)
    }

    @Provides
    @IntoMap
    @Peers.Scope
    @UiViewModel(PeersViewModel::class)
    fun viewModel(viewModel: PeersViewModel): ViewModel = viewModel
}