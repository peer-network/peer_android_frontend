package eu.peernetwork.user.ui.password.request

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.core.ui.factory.UiViewModelFactory
import eu.peernetwork.user.ui.password.reset.PasswordReset
import eu.peernetwork.user.ui.v2.password.request.RequestViewModel
import javax.inject.Provider

@Module
object PasswordRequestModule {
    @Provides
    @PasswordRequest.Scope
    fun provideBuilderFactory(factory: Map<Class<out UiComponent.Builder>,
            @JvmSuppressWildcards Provider<UiComponent.Builder>>): UiComponentProvider.Factory {
        return UiBuilderFactory(factory)
    }

    @Provides
    @PasswordRequest.Scope
    fun provideViewModelFactory(
        classToViewModel:
        @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @PasswordRequest.Scope
    @UiViewModel(RequestViewModel::class)
    fun provideViewModel(viewModel: RequestViewModel): ViewModel = viewModel

    @PasswordRequest.Scope
    @Provides
    @IntoMap
    @UiBuilder(PasswordReset.Builder::class)
    fun providePasswordResetBuilder(component: PasswordRequest.Component): UiComponent.Builder {
        return PasswordReset.Builder(component)
    }
}
