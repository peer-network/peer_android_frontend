package eu.peernetwork.wallet.ui.extension

import eu.peernetwork.core.ui.design.luna.DesignRichText
import eu.peernetwork.wallet.ui.transactions.TransactionsNavigator

fun DesignRichText.route(value: String): TransactionsNavigator.Route {
    return when(this) {
        is DesignRichText.Link -> TransactionsNavigator.Route.Link(value)
        is DesignRichText.Tag -> TransactionsNavigator.Route.Search("tag", value)
        is DesignRichText.Mention -> TransactionsNavigator.Route.Search("username", value)
    }
}
