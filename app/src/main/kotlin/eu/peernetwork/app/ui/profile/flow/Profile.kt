package eu.peernetwork.app.ui.profile.flow

import android.content.Context
import eu.peernetwork.app.ui.profile.preview.ProfilePreview
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.persistence.domain.provider.PreferenceProvider
import eu.peernetwork.user.ui.provider.UserProvider
import eu.peernetwork.user.ui.user.settings.UserSettings

interface Profile : UserProvider, PreferenceProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Profile::class ],
        modules = [ ProfileModule::class ]
    )
    interface Component : Profile, UiComponentProvider, ProfilePreview, UserSettings

    class Builder(private val dependency: Profile) : UiComponent.DefaultBuilder<Profile, Component>() {
        override fun build(context: Context): Component {
            return DaggerProfile_Component.builder().profile(dependency).build()
        }
    }
}
