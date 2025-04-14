package eu.peernetwork.blog.ui.engagement

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.blog.ui.provider.BlogProvider
import eu.peernetwork.core.ui.component.UiComponent

interface Engagements : BlogProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [Engagements::class],
        modules = [EngagementsModule::class]
    )
    interface Component : Engagements {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Engagements) : UiComponent.DefaultBuilder<Engagements, Component>() {
        override fun build(context: Context): Component {
            return DaggerEngagements_Component.builder().engagements(dependency).build()
        }
    }
}