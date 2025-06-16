package eu.peernetwork.app.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.app.ui.profile.Profile
import eu.peernetwork.app.ui.search.Search
import eu.peernetwork.blog.ui.timeline.photo.Photo
import eu.peernetwork.blog.ui.timeline.video.Video
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.core.ui.factory.UiViewModelFactory
import eu.peernetwork.social.ui.connection.Connection
import eu.peernetwork.app.ui.member.Member
import javax.inject.Provider

@Module
object FeedModule {
    @Provides
    @Feed.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Provides
    @Feed.Scope
    fun provideViewModelFactory(
        classToViewModel:
        @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return UiViewModelFactory(classToViewModel)
    }

    @Provides
    @IntoMap
    @Feed.Scope
    @UiViewModel(FeedViewModel::class)
    fun viewModel(viewModel: FeedViewModel): ViewModel = viewModel

    @Feed.Scope
    @Provides
    @IntoMap
    @UiBuilder(Profile.Builder::class)
    fun provideProfileBuilder(component: Feed.Component): UiComponent.Builder {
        return Profile.Builder(component)
    }

    @Feed.Scope
    @Provides
    @IntoMap
    @UiBuilder(Photo.Builder::class)
    fun providePhotoBuilder(component: Feed.Component): UiComponent.Builder {
        return Photo.Builder(component)
    }

    @Feed.Scope
    @Provides
    @IntoMap
    @UiBuilder(Video.Builder::class)
    fun provideVideoBuilder(component: Feed.Component): UiComponent.Builder {
        return Video.Builder(component)
    }

    @Feed.Scope
    @Provides
    @IntoMap
    @UiBuilder(Member.Builder::class)
    fun provideMemberBuilder(component: Feed.Component): UiComponent.Builder {
        return Member.Builder(component)
    }

    @Feed.Scope
    @Provides
    @IntoMap
    @UiBuilder(Search.Builder::class)
    fun provideSearchBuilder(component: Feed.Component): UiComponent.Builder {
        return Search.Builder(component)
    }

    @Feed.Scope
    @Provides
    @IntoMap
    @UiBuilder(Connection.Builder::class)
    fun provideConnectionBuilder(component: Feed.Component): UiComponent.Builder {
        return Connection.Builder(component)
    }
}
