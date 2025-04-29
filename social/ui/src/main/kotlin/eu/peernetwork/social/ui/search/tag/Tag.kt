package eu.peernetwork.social.ui.search.tag

import android.content.Context
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.social.ui.search.title.TitleModule

interface Tag {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Tag::class ],
        modules = [ TitleModule::class ]
    )
    interface Component : Tag

    class Builder(private val dependency: Tag) : UiComponent.DefaultBuilder<Tag, Component>() {
        override fun build(context: Context): Component {
            return DaggerTag_Component.builder().tag(dependency).build()
        }
    }
}
