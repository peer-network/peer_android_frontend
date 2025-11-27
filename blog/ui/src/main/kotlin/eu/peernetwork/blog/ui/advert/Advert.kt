package eu.peernetwork.blog.ui.advert

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.blog.ui.engagement.EngagementDialog
import eu.peernetwork.blog.ui.post.Post
import eu.peernetwork.blog.ui.provider.BlogProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider

interface Advert : BlogProvider {
    fun engagementConfirmation(): EngagementDialog

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [Advert::class ],
        modules = [ AdvertModule::class ]
    )
    interface Component : Advert, Post, UiComponentProvider {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Advert) : UiComponent.DefaultBuilder<Advert, Component>() {
        override fun build(context: Context): Component {
            return DaggerAdvert_Component.builder()
                .advert(dependency)
                .advertModule(AdvertModule(context))
                .build()
        }
    }
}
