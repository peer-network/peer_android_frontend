package eu.peernetwork.app.ui.profile.preview

import android.content.Context
import eu.peernetwork.core.common.provider.CoreProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.persistence.domain.provider.PreferenceProvider
import eu.peernetwork.social.ui.content.music.Music
import eu.peernetwork.social.ui.content.photo.Photo
import eu.peernetwork.social.ui.content.video.Video
import eu.peernetwork.user.domain.provider.AccountProvider
import eu.peernetwork.user.domain.provider.AuthenticationProvider
import eu.peernetwork.user.ui.user.core.User

interface ProfilePreview : CoreProvider, AccountProvider, AuthenticationProvider, PreferenceProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ ProfilePreview::class ],
        modules = [ProfilePreviewModule::class ]
    )
    interface Component : ProfilePreview, UiComponentProvider, User, Photo, Video, Music

    class Builder(private val dependency: ProfilePreview) : UiComponent.DefaultBuilder<ProfilePreview, Component>() {
        override fun build(context: Context): Component {
            return DaggerProfilePreview_Component.builder().profilePreview(dependency).build()
        }
    }
}
