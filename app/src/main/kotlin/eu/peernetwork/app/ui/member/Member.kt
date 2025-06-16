package eu.peernetwork.app.ui.member

import android.content.Context
import eu.peernetwork.app.provider.ApplicationProvider
import eu.peernetwork.blog.ui.post.photo.Photo
import eu.peernetwork.blog.ui.post.video.Video
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.social.ui.connection.Connection
import eu.peernetwork.social.ui.followers.Followers
import eu.peernetwork.social.ui.followings.Followings
import eu.peernetwork.social.ui.peers.Peers
import eu.peernetwork.user.ui.user.User

interface Member : ApplicationProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Member::class ],
        modules = [MemberModule::class]
    )
    interface Component : Member,
        User,
        Photo,
        Video,
        Followers,
        UiComponentProvider,
        Followings,
        Peers,
        Connection

    class Builder(private val dependency: Member) : UiComponent.DefaultBuilder<Member, Component>() {
        override fun build(context: Context): Component {
            return DaggerMember_Component.builder().member(dependency).build()
        }
    }
}
