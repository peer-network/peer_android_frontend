package eu.peernetwork.blog.ui.interaction.overview

import android.content.Context
import eu.peernetwork.blog.ui.interaction.user.User
import eu.peernetwork.blog.ui.provider.BlogProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider

interface Overview : BlogProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [Overview::class],
        modules = [OverviewModule::class]
    )
    interface Component : Overview, UiComponentProvider, User

    class Builder(private val dependency: Overview) : UiComponent.DefaultBuilder<Overview, Component>() {
        override fun build(context: Context): Component {
            return DaggerOverview_Component.builder().overview(dependency).build()
        }
    }
}
