package eu.peernetwork.social.ui.referral

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.core.ui.annotation.UiViewModel
import javax.inject.Provider
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.core.ui.factory.UiViewModelFactory

@Module
object ReferralModule {
    @Provides
    @Referral.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

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
}