package eu.peernetwork.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.app.ui.home.Home
import eu.peernetwork.app.ui.setup.Setup
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.core.ui.factory.UiViewModelFactory
import javax.inject.Provider

@Module
object MainModule {
    @Provides
    @Main.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Provides
    @Main.Scope
    fun provideViewModelFactory(
        classToViewModel:
        @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @Main.Scope
    @UiViewModel(MainViewModel::class)
    fun viewModel(viewModel: MainViewModel): ViewModel = viewModel

    @Main.Scope
    @Provides
    @IntoMap
    @UiBuilder(Home.Builder::class)
    fun provideHomeBuilder(component: Main.Component): UiComponent.Builder {
        return Home.Builder(component)
    }

    @Main.Scope
    @Provides
    @IntoMap
    @UiBuilder(Setup.Builder::class)
    fun provideSetupBuilder(component: Main.Component): UiComponent.Builder {
        return Setup.Builder(component)
    }
}
