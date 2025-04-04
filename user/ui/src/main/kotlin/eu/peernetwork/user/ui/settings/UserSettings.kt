package eu.peernetwork.user.ui.settings

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.user.ui.provider.UserProvider

interface UserSettings : UserProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ UserSettings::class ],
        modules = [ UserSettingsModule::class ]
    )
    interface Component : UserSettings {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: UserSettings) : UiComponent.DefaultBuilder<UserSettings, Component>() {
        override fun build(context: Context): Component {
            return DaggerUserSettings_Component.builder()
                .userSettings(dependency)
                .userSettingsModule(UserSettingsModule(context))
                .build()
        }
    }
}
