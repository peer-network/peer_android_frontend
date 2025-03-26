package eu.peernetwork.app.ui.setup

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
import eu.peernetwork.user.ui.login.Login
import eu.peernetwork.user.ui.registeration.Registration
import javax.inject.Provider

@Module
object SetupModule {
    @Provides
    @Setup.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Provides
    @Setup.Scope
    fun provideViewModelFactory(
        classToViewModel:
        @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @Setup.Scope
    @UiViewModel(SetupViewModel::class)
    fun viewModel(viewModel: SetupViewModel): ViewModel = viewModel

    @Setup.Scope
    @Provides
    @IntoMap
    @UiBuilder(Login.Builder::class)
    fun provideLoginBuilder(component: Setup.Component): UiComponent.Builder {
        return Login.Builder(component)
    }

    @Setup.Scope
    @Provides
    @IntoMap
    @UiBuilder(Registration.Builder::class)
    fun provideRegistrationBuilder(component: Setup.Component): UiComponent.Builder {
        return Registration.Builder(component)
    }
}
