package eu.peernetwork.app.ui.profile

import android.content.Context
import eu.peernetwork.app.provider.ApplicationProvider
import eu.peernetwork.blog.ui.post.music.Music
import eu.peernetwork.blog.ui.post.photo.Photo
import eu.peernetwork.blog.ui.post.video.Video
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.social.ui.member.Member
import eu.peernetwork.user.ui.account.Account
import eu.peernetwork.user.ui.settings.Settings
import eu.peernetwork.user.ui.user.User

interface Profile : ApplicationProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Profile::class ],
        modules = [ ProfileModule::class ]
    )
    interface Component : Profile, UiComponentProvider, Settings, Member, User, Photo, Video, Music, Account

    class Builder(private val dependency: Profile) : UiComponent.DefaultBuilder<Profile, Component>() {
        override fun build(context: Context): Component {
            return DaggerProfile_Component.builder().profile(dependency).build()
        }
    }
}
