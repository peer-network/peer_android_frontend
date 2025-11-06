package eu.peernetwork.blog.ui.post

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.blog.ui.engagement.Engagement
import eu.peernetwork.blog.ui.moderation.Moderation
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory

@Module
object PostModule {
    @Provides
    @Post.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Provides
    @IntoMap
    @Post.Scope
    @UiBuilder(Engagement.Builder::class)
    fun provideEngagementBuilder(component: Post.Component): UiComponent.Builder {
        return Engagement.Builder(component)
    }

    @Provides
    @IntoMap
    @Post.Scope
    @UiBuilder(Moderation.Builder::class)
    fun provideModerationBuilder(component: Post.Component): UiComponent.Builder {
        return Moderation.Builder(component)
    }
}
