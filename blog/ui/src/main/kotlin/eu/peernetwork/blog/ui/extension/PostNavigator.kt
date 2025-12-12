package eu.peernetwork.blog.ui.extension

import eu.peernetwork.blog.ui.post.PostNavigator
import eu.peernetwork.core.ui.design.luna.DesignRichText

fun DesignRichText.route(value: String): PostNavigator.Route {
    return when(this) {
        is DesignRichText.Link -> PostNavigator.Route.Link(value)
        is DesignRichText.Tag -> PostNavigator.Route.Search("tag", value)
        is DesignRichText.Mention -> PostNavigator.Route.Search("username", value)
    }
}
