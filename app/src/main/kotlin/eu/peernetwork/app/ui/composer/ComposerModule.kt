package eu.peernetwork.app.ui.composer

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.app.ui.renderer.EngagementRenderer
import eu.peernetwork.blog.ui.creator.Creator
import eu.peernetwork.blog.ui.engagement.EngagementDialog
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.media.ui.attachment.Attachment
import eu.peernetwork.media.ui.editor.video.Video
import eu.peernetwork.media.ui.selector.explorer.Explorer
import eu.peernetwork.media.ui.selector.photo.Photo
import eu.peernetwork.wallet.ui.confirmation.Confirmation
import javax.inject.Provider

@Module
object ComposerModule {
    @Provides
    @Composer.Scope
    fun provideBuilderFactory(factory: Map<Class<out UiComponent.Builder>,
            @JvmSuppressWildcards Provider<UiComponent.Builder>>): UiComponentProvider.Factory {
        return UiBuilderFactory(factory)
    }

    @Composer.Scope
    @Provides
    @IntoMap
    @UiBuilder(Creator.Builder::class)
    fun provideCreatorBuilder(component: Composer.Component): UiComponent.Builder {
        return Creator.Builder(component)
    }

    @Composer.Scope
    @Provides
    @IntoMap
    @UiBuilder(Attachment.Builder::class)
    fun provideAttachmentBuilder(component: Composer.Component): UiComponent.Builder {
        return Attachment.Builder(component)
    }

    @Composer.Scope
    @Provides
    @IntoMap
    @UiBuilder(Explorer.Builder::class)
    fun provideExplorerBuilder(component: Composer.Component): UiComponent.Builder {
        return Explorer.Builder(component)
    }

    @Composer.Scope
    @Provides
    @IntoMap
    @UiBuilder(Confirmation.Builder::class)
    fun provideConfirmationBuilder(component: Composer.Component): UiComponent.Builder {
        return Confirmation.Builder(component)
    }

    @Composer.Scope
    @Provides
    fun provideEngagementRenderer(component: Composer.Component): EngagementDialog {
        return EngagementRenderer(component)
    }

    @Composer.Scope
    @Provides
    @IntoMap
    @UiBuilder(Video.Builder::class)
    fun provideVideoBuilder(component: Composer.Component): UiComponent.Builder {
        return Video.Builder(component)
    }

    @Composer.Scope
    @Provides
    @IntoMap
    @UiBuilder(Photo.Builder::class)
    fun providePhotoBuilder(component: Composer.Component): UiComponent.Builder {
        return Photo.Builder(component)
    }
}
