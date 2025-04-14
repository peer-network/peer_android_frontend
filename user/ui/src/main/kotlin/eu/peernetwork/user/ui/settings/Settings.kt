package eu.peernetwork.user.ui.settings

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.user.ui.provider.UserProvider

interface Settings : UserProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Settings::class ],
        modules = [ SettingsModule::class ]
    )
    interface Component : Settings {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Settings) : UiComponent.DefaultBuilder<Settings, Component>() {
        override fun build(context: Context): Component {
            return DaggerSettings_Component.builder()
                .settings(dependency)
                .settingsModule(SettingsModule(context))
                .build()
        }
    }
}
