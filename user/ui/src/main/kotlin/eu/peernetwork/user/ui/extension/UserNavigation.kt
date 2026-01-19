package eu.peernetwork.user.ui.extension

import eu.peernetwork.core.ui.design.luna.DesignRichText
import eu.peernetwork.user.ui.user.UserNavigator

fun DesignRichText.route(value: String): UserNavigator.Route {
    return when(this) {
        is DesignRichText.Link -> UserNavigator.Route.Link(value)
        is DesignRichText.Tag -> UserNavigator.Route.Search("tag", value)
        is DesignRichText.Mention -> UserNavigator.Route.Search("username", value)
    }
}
