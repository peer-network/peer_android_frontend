package eu.peernetwork.ads.ui.extension

import eu.peernetwork.ads.ui.article.ArticleNavigator
import eu.peernetwork.core.ui.design.luna.DesignRichText

fun DesignRichText.route(value: String): ArticleNavigator.Route {
    return when(this) {
        is DesignRichText.Link -> ArticleNavigator.Route.Link(value)
        is DesignRichText.Tag -> ArticleNavigator.Route.Search("tag", value)
        is DesignRichText.Mention -> ArticleNavigator.Route.Search("username", value)
    }
}
