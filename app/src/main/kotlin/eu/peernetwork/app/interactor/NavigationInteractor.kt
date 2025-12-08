package eu.peernetwork.app.interactor

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import eu.peernetwork.blog.ui.post.PostNavigator
import java.net.URLEncoder

class NavigationInteractor(
    private val context: Context,
    private val controller: NavHostController
) : PostNavigator {
    override fun navigate(route: PostNavigator.Route) {
        when(route) {
            is PostNavigator.Route.Profile -> {
                controller.navigate("profile/${route.id}") {
                    launchSingleTop = true
                }
            }
            is PostNavigator.Route.Search -> {
                val encoded = URLEncoder.encode(route.query, "UTF-8")
                controller.navigate("search/${route.type}/$encoded") {
                    launchSingleTop = true
                }
            }
            is PostNavigator.Route.Link -> {
                val url = if (!route.url.startsWith("http")) {
                    "https://${route.url}"
                } else {
                    route.url
                }
                context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
            }
        }
    }
}
