package eu.peernetwork.blog.ui.content.overlay

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.blog.ui.engagement.Engagement
import eu.peernetwork.blog.ui.engagement.EngagementDialog
import eu.peernetwork.blog.ui.moderation.Moderation
import eu.peernetwork.blog.ui.provider.BlogProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider

interface Overlay : BlogProvider {
    fun engagementConfirmation(): EngagementDialog

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [Overlay::class],
        modules = [OverlayModule::class]
    )
    interface Component : Overlay, UiComponentProvider, Engagement, Moderation {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Overlay) : UiComponent.DefaultBuilder<Overlay, Component>() {
        override fun build(context: Context): Component {
            return DaggerOverlay_Component.builder()
                .overlay(dependency)
                .build()
        }
    }
}
