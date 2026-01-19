package eu.peernetwork.app.interactor

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import eu.peernetwork.ads.ui.article.ArticleNavigator
import eu.peernetwork.blog.ui.post.PostNavigator
import eu.peernetwork.user.ui.user.UserNavigator
import java.net.URLEncoder

class NavigationInteractor(
    private val context: Context,
    private val controller: NavHostController
) : PostNavigator, ArticleNavigator, UserNavigator {
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

    override fun navigate(route: ArticleNavigator.Route) {
        when(route) {
            is ArticleNavigator.Route.Profile -> {
                controller.navigate("profile/${route.id}") {
                    launchSingleTop = true
                }
            }
            is ArticleNavigator.Route.Search -> {
                val encoded = URLEncoder.encode(route.query, "UTF-8")
                controller.navigate("search/${route.type}/$encoded") {
                    launchSingleTop = true
                }
            }
            is ArticleNavigator.Route.Link -> {
                val url = if (!route.url.startsWith("http")) {
                    "https://${route.url}"
                } else {
                    route.url
                }
                context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
            }
        }
    }

    override fun navigate(route: UserNavigator.Route) {
        when(route) {
            is UserNavigator.Route.Profile -> {
                controller.navigate("profile/${route.id}") {
                    launchSingleTop = true
                }
            }
            is UserNavigator.Route.Search -> {
                val encoded = URLEncoder.encode(route.query, "UTF-8")
                controller.navigate("search/${route.type}/$encoded") {
                    launchSingleTop = true
                }
            }
            is UserNavigator.Route.Link -> {
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
