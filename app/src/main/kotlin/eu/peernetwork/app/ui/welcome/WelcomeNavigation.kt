package eu.peernetwork.app.ui.welcome

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignNavigation
import eu.peernetwork.core.ui.extension.route
import eu.peernetwork.user.ui.login.LoginScreen
import eu.peernetwork.user.ui.password.request.RequestScreen
import eu.peernetwork.user.ui.password.reset.ResetScreen
import eu.peernetwork.user.ui.password.verification.VerificationScreen
import eu.peernetwork.user.ui.referral.ReferralScreen
import eu.peernetwork.user.ui.registration.RegistrationScreen
import eu.peernetwork.user.ui.registration.RegistrationSuccess

@Composable
fun WelcomeNavigation(
    referral: String? = null,
    provider: UiComponentProvider,
    onBrowse: (String) -> Unit,
) {
    val controller = rememberNavController()
    val startDestination = if (referral != null) {
        "referral?code=$referral"
    } else {
        "login"
    }
    val handleOnBrowse by rememberUpdatedState(onBrowse)
    DesignNavigation(
        navController = controller,
        startDestination = startDestination,
        enterTransition = {
            fadeIn(animationSpec = tween(200)) +
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(300),
                        initialOffset = { it / 4 }
                    )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(200)) +
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(300),
                        targetOffset = { -it / 4 }
                    )
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(200)) +
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(300),
                        initialOffset = { it / 4 }
                    )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(200)) +
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(300),
                        targetOffset = { -it / 4 }
                    )
        }
    ) {
        composable("login?email={email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            LoginScreen(
                email = email,
                provider = provider,
                viewModelStoreOwner = backStackEntry,
                onRegister = { controller.navigate("referral?code=$referral") },
                onPasswordReset = { controller.navigate("recovery?email=$it") },
                onPrivacy = { handleOnBrowse(BuildConfig.PRIVACY) }
            )
        }
        composable("register?code={code}") { backStackEntry ->
            val code = backStackEntry.arguments?.getString("code")
            if (code == null) {
                ReferralScreen(
                    referral = referral,
                    provider = provider,
                    viewModelStoreOwner = backStackEntry,
                    onRegister = { controller.navigate("register") }
                )
            } else {
                RegistrationScreen(
                    referral = code,
                    provider = provider,
                    viewModelStoreOwner = backStackEntry,
                    onLogin = { controller.route("login") },
                    onRegistered = { controller.route("registered?email=$it") },
                    onPrivacy = { handleOnBrowse(BuildConfig.PRIVACY) },
                    onLicence = { handleOnBrowse(BuildConfig.LICENCE) }
                )
            }
        }
        composable("registered?email={email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            RegistrationSuccess {
                controller.route("login?email=$email")
            }
        }
        composable("referral?code={code}") { backStackEntry ->
            val code = backStackEntry.arguments?.getString("code")
            ReferralScreen(
                referral = code,
                provider = provider,
                viewModelStoreOwner = backStackEntry,
                onRegister = { controller.navigate("register?code=$it") }
            )
        }
        composable("recovery?email={email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            RequestScreen(
                email = email,
                provider = provider,
                viewModelStoreOwner = backStackEntry,
                onVerify = {},
                onReset = { controller.navigate("verification?email=$it") }
            )
        }
        composable("verification?email={email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            if (email == null) {
                RequestScreen(
                    email = email,
                    provider = provider,
                    viewModelStoreOwner = backStackEntry,
                    onReset = { controller.navigate("verification?email=$it") },
                    onVerify = {}
                )
            } else {
                VerificationScreen(
                    email = email,
                    provider = provider,
                    viewModelStoreOwner = backStackEntry,
                    onVerification = { controller.navigate("reset?token=$it") }
                )
            }
        }
        composable("reset?token={token}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token")
            ResetScreen(
                token = token ?: "",
                provider = provider,
                viewModelStoreOwner = backStackEntry,
            ) { controller.route("login") }
        }
    }
}
