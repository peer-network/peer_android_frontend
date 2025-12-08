package eu.peernetwork.app.interactor

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import eu.peernetwork.blog.ui.post.PostNavigator

class PostNavigatorDelegate(
    private val context: Context,
    private val controller: NavHostController
) : PostNavigator {
    override fun navigate(route: PostNavigator.Route) {
        when(route) {
            is PostNavigator.Route.Profile -> {
                controller.navigate("profile/${route.id}")
            }
            is PostNavigator.Route.Search -> {
                controller.navigate("search/${route.type}/${route.query}")
            }
            is PostNavigator.Route.Link -> {
                val url = if (!route.url.startsWith("http")) {
                    "https://$route.url"
                } else {
                    route.url
                }
                context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
            }
        }
    }
}
