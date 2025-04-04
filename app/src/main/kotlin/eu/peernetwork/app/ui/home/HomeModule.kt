package eu.peernetwork.app.ui.home

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
import eu.peernetwork.app.ui.feed.Feed
import eu.peernetwork.app.ui.profile.core.Profile
import eu.peernetwork.blog.ui.point.BlogPoint
import javax.inject.Provider

@Module
object HomeModule {
    @Provides
    @Home.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Provides
    @Home.Scope
    fun provideViewModelFactory(
        classToViewModel:
        @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @Home.Scope
    @UiViewModel(HomeViewModel::class)
    fun viewModel(viewModel: HomeViewModel): ViewModel = viewModel

    @Home.Scope
    @Provides
    @IntoMap
    @UiBuilder(Feed.Builder::class)
    fun provideFeedBuilder(component: Home.Component): UiComponent.Builder {
        return Feed.Builder(component)
    }

    @Home.Scope
    @Provides
    @IntoMap
    @UiBuilder(Profile.Builder::class)
    fun provideProfileBuilder(component: Home.Component): UiComponent.Builder {
        return Profile.Builder(component)
    }

    @Home.Scope
    @Provides
    @IntoMap
    @UiBuilder(BlogPoint.Builder::class)
    fun provideCouponBuilder(component: Home.Component): UiComponent.Builder {
        return BlogPoint.Builder(component)
    }
}
