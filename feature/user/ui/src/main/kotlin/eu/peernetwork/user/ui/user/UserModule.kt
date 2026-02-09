package eu.peernetwork.user.ui.user

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
import eu.peernetwork.user.ui.option.Option
import javax.inject.Provider

@Module
object UserModule {
    @Provides
    @User.Scope
    fun provideBuilderFactory(factory: Map<Class<out UiComponent.Builder>,
            @JvmSuppressWildcards Provider<UiComponent.Builder>>): UiComponentProvider.Factory {
        return UiBuilderFactory(factory)
    }

    @Provides
    @User.Scope
    fun provideViewModelFactory(
        classToViewModel:
        @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @User.Scope
    @UiViewModel(UserViewModel::class)
    fun provideViewModel(viewModel: UserViewModel): ViewModel = viewModel

    @User.Scope
    @Provides
    @IntoMap
    @UiBuilder(Option.Builder::class)
    fun provideInvitationBuilder(component: User.Component): UiComponent.Builder {
        return Option.Builder(component)
    }
}
