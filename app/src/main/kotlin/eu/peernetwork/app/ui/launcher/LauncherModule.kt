package eu.peernetwork.app.ui.launcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.app.ui.home.Home
import eu.peernetwork.app.ui.setup.Setup
import eu.peernetwork.app.ui.welcome.Welcome
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.core.ui.factory.UiViewModelFactory
import javax.inject.Provider

@Module
object LauncherModule {
    @Provides
    @Launcher.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Provides
    @Launcher.Scope
    fun provideViewModelFactory(
        classToViewModel:
        @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @Launcher.Scope
    @UiBuilder(Home.Builder::class)
    fun provideHomeBuilder(component: Launcher.Component): UiComponent.Builder {
        return Home.Builder(component)
    }

    @Provides
    @IntoMap
    @Launcher.Scope
    @UiBuilder(Setup.Builder::class)
    fun provideSetupBuilder(component: Launcher.Component): UiComponent.Builder {
        return Setup.Builder(component)
    }

    @Provides
    @IntoMap
    @Launcher.Scope
    @UiBuilder(Welcome.Builder::class)
    fun provideWelcomeBuilder(component: Launcher.Component): UiComponent.Builder {
        return Welcome.Builder(component)
    }
}
