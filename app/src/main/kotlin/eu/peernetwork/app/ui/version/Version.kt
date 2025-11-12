package eu.peernetwork.app.ui.version

import android.content.Context
import dagger.BindsInstance
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.provider.ApplicationProvider
import eu.peernetwork.core.ui.component.UiComponent

interface Version : ApplicationProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Version::class ]
    )
    interface Component : Version {
        @dagger.Component.Builder
        interface Builder {
            fun version(version: Version): Builder

            @BindsInstance
            fun link(link: VersionLink): Builder

            fun build(): Component
        }
        fun link(): VersionLink
    }

    class Builder(private val dependency: Version) : UiComponent.DefaultBuilder<Version, Component>() {
        override fun build(context: Context): Component {
            return DaggerVersion_Component.builder()
                .version(dependency)
                .link(
                    VersionLink(
                        app =BuildConfig.APP_WIKI,
                        wiki = BuildConfig.BACKEND_WIKI
                    )
                ).build()
        }
    }
}
