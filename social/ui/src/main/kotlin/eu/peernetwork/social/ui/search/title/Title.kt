package eu.peernetwork.social.ui.search.title

import android.content.Context
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.social.ui.provider.SocialProvider

interface Title : SocialProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Title::class ],
        modules = [ TitleModule::class ]
    )
    interface Component : Title

    class Builder(private val dependency: Title) : UiComponent.DefaultBuilder<Title, Component>() {
        override fun build(context: Context): Component {
            return DaggerTitle_Component.builder().title(dependency).build()
        }
    }
}
