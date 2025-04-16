package eu.peernetwork.blog.ui.engagement

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.blog.ui.provider.BlogProvider
import eu.peernetwork.core.ui.component.UiComponent

interface Engagement : BlogProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [Engagement::class],
        modules = [EngagementModule::class]
    )
    interface Component : Engagement {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Engagement) : UiComponent.DefaultBuilder<Engagement, Component>() {
        override fun build(context: Context): Component {
            return DaggerEngagement_Component.builder().engagement(dependency).build()
        }
    }
}