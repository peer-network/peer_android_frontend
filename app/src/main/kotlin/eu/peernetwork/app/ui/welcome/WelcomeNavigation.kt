package eu.peernetwork.app.ui.welcome

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignNavigation
import eu.peernetwork.core.ui.extension.route
import eu.peernetwork.user.ui.v2.login.LoginScreen
import eu.peernetwork.user.ui.v2.password.request.RequestScreen
import eu.peernetwork.user.ui.v2.password.reset.ResetScreen
import eu.peernetwork.user.ui.v2.password.verification.VerificationScreen
import eu.peernetwork.user.ui.v2.referral.ReferralScreen
import eu.peernetwork.user.ui.v2.registration.RegistrationScreen
import eu.peernetwork.user.ui.v2.registration.RegistrationSuccess

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
                    onReset = { controller.navigate("verification?email=$it") }
                )
            } else {
                VerificationScreen(
                    email = email,
                    provider = provider,
                    viewModelStoreOwner = backStackEntry,
                    onVerification = { controller.navigate("reset") }
                )
            }
        }
        composable("reset?email={email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            ResetScreen(
                email = email,
                provider = provider,
                viewModelStoreOwner = backStackEntry,
            )
        }
    }
}
