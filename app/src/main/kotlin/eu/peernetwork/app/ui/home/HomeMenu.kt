package eu.peernetwork.app.ui.home

import eu.peernetwork.app.R

sealed class HomeMenu(
    val icon: Int,
    val active: Int,
    val label: Int
) {
    val path: String = this::class.java.simpleName
    data object Home: HomeMenu(
        icon = R.drawable.ic_home_outline,
        active = R.drawable.ic_home,
        label = eu.peernetwork.feature.blog.ui.R.string.feed_label,
    )
    data object Search: HomeMenu(
        icon = R.drawable.ic_search_outline,
        active = R.drawable.ic_search,
        label = R.string.search_label
    )
    data object Add: HomeMenu(
        icon = R.drawable.ic_add_outline,
        active = R.drawable.ic_add,
        label = R.string.add_label
    )
    data object Wallet: HomeMenu(
        icon = R.drawable.ic_wallet_outline,
        active = R.drawable.ic_wallet,
        label = R.string.wallet_label
    )
    data object Profile: HomeMenu(
        icon = R.drawable.ic_profile_outline,
        active = R.drawable.ic_profile,
        label = R.string.profile_label
    )
    data object Explore: HomeMenu(
        icon = R.drawable.ic_trend_outline,
        active = R.drawable.ic_trend,
        label = R.string.trend_label
    )
    companion object {
        val MENU = arrayOf(Home, Search, Add, Wallet, Profile)
        fun get(index: Int): HomeMenu = MENU[index]
    }
}
