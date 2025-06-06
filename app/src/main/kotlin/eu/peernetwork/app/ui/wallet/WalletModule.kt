package eu.peernetwork.app.ui.wallet

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.app.ui.profile.Profile
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.social.ui.search.member.Member
import eu.peernetwork.wallet.ui.overview.Overview
import eu.peernetwork.wallet.ui.transfer.Transfer
import javax.inject.Provider

@Module
object WalletModule {
    @Provides
    @Wallet.Scope
    fun provideBuilderFactory(factory: Map<Class<out UiComponent.Builder>,
            @JvmSuppressWildcards Provider<UiComponent.Builder>>): UiComponentProvider.Factory {
        return UiBuilderFactory(factory)
    }

    @Provides
    @Wallet.Scope
    @IntoMap
    @UiBuilder(Overview.Builder::class)
    fun provideOverviewBuilder(component: Wallet.Component): UiComponent.Builder {
        return Overview.Builder(component)
    }

    @Provides
    @Wallet.Scope
    @IntoMap
    @UiBuilder(Transfer.Builder::class)
    fun provideTransferBuilder(component: Wallet.Component): UiComponent.Builder {
        return Transfer.Builder(component)
    }

    @Wallet.Scope
    @Provides
    @IntoMap
    @UiBuilder(Member.Builder::class)
    fun provideMemberBuilder(component: Wallet.Component): UiComponent.Builder {
        return Member.Builder(component)
    }

    @Wallet.Scope
    @Provides
    @IntoMap
    @UiBuilder(Profile.Builder::class)
    fun provideProfileBuilder(component: Wallet.Component): UiComponent.Builder {
        return Profile.Builder(component)
    }
}
