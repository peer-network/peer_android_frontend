package eu.peernetwork.social.ui.referral

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
import eu.peernetwork.social.ui.connection.Connection

@Module
object ReferralModule {
    @Provides
    @Referral.Scope
    fun provideBuilderFactory(factory: Map<Class<out UiComponent.Builder>,
            @JvmSuppressWildcards Provider<UiComponent.Builder>>): UiComponentProvider.Factory {
        return UiBuilderFactory(factory)
    }

    @Provides
    @Referral.Scope
    fun provideViewModelFactory(
        providers: @JvmSuppressWildcards Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(providers)
    }

    @Provides
    @IntoMap
    @Referral.Scope
    @UiViewModel(ReferralViewModel::class)
    fun viewmodel(viewModel: ReferralViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @Referral.Scope
    @UiBuilder(Connection.Builder::class)
    fun provideConnectionBuilder(component: Referral.Component): UiComponent.Builder {
        return Connection.Builder(component)
    }
}