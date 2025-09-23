package eu.peernetwork.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.app.ui.launcher.Launcher
import eu.peernetwork.app.ui.splash.Splash
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
    @UiBuilder(Splash.Builder::class)
    fun provideSplashBuilder(component: Main.Component): UiComponent.Builder {
        return Splash.Builder(component)
    }

    @Main.Scope
    @Provides
    @IntoMap
    @UiBuilder(Launcher.Builder::class)
    fun provideLauncherBuilder(component: Main.Component): UiComponent.Builder {
        return Launcher.Builder(component)
    }
}