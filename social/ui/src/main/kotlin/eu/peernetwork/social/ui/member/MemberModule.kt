package eu.peernetwork.social.ui.member

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
import eu.peernetwork.social.ui.followings.Followings
import eu.peernetwork.social.ui.peers.Peers

@Module
object MemberModule {
    @Provides
    @Member.Scope
    fun provideBuilderFactory(factory: Map<Class<out UiComponent.Builder>,
            @JvmSuppressWildcards Provider<UiComponent.Builder>>): UiComponentProvider.Factory {
        return UiBuilderFactory(factory)
    }

    @Provides
    @Member.Scope
    fun provideViewModelFactory(
        providers: @JvmSuppressWildcards Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(providers)
    }

    @Provides
    @IntoMap
    @Member.Scope
    @UiViewModel(MemberViewModel::class)
    fun viewModel(viewModel: MemberViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @Member.Scope
    @UiBuilder(Followers.Builder::class)
    fun provideFollowersBuilder(member: Member.Component): UiComponent.Builder {
        return Followers.Builder(member)
    }

    @Provides
    @IntoMap
    @Member.Scope
    @UiBuilder(Followings.Builder::class)
    fun provideFollowingsBuilder(member: Member.Component): UiComponent.Builder {
        return Followings.Builder(member)
    }

    @Provides
    @IntoMap
    @Member.Scope
    @UiBuilder(Peers.Builder::class)
    fun providePeersBuilder(member: Member.Component): UiComponent.Builder {
        return Peers.Builder(member)
    }
}