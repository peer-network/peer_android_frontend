package eu.peernetwork.user.ui.user.coupon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.factory.UiViewModelFactory
import javax.inject.Provider

@Module
object UserCouponModule {
    @Provides
    @UserCoupon.Scope
    fun provideViewModelFactory(
        classToViewModel:
        @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @UserCoupon.Scope
    @UiViewModel(UserCouponViewModel::class)
    fun provideViewModel(viewModel: UserCouponViewModel): ViewModel = viewModel
}
