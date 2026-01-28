package eu.peernetwork.wallet.ui.transactions

import androidx.compose.runtime.staticCompositionLocalOf

interface TransactionsNavigator {
    fun navigate(route: Route)

    sealed interface Route {
        data class Search(
            val type: String,
            val query: String
        ): Route
        data class Link(val url: String): Route
        data class Profile(val id: String): Route
    }

    companion object Companion {
        val LocalTransactionsNavigator = staticCompositionLocalOf<TransactionsNavigator> {
            error("${TransactionsNavigator::class.java.name} not provided")
        }
    }
}
