package eu.peernetwork.blog.ui.timeline.video

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.blog.ui.engagement.Engagement
import eu.peernetwork.blog.ui.engagement.EngagementDialog
import eu.peernetwork.blog.ui.moderation.Moderation
import eu.peernetwork.blog.ui.provider.BlogProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider

interface Video : BlogProvider {
    fun engagementConfirmation(): EngagementDialog

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [Video::class],
        modules = [VideoModule::class]
    )
    interface Component : Video, Engagement, UiComponentProvider, Moderation {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Video) : UiComponent.DefaultBuilder<Video, Component>() {
        override fun build(context: Context): Component {
            return DaggerVideo_Component.builder()
                .video(dependency)
                .videoModule(VideoModule(context))
                .build()
        }
    }
}
