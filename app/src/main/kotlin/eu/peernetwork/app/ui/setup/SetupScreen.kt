package eu.peernetwork.app.ui.setup

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.login.LoginScreen
import eu.peernetwork.user.ui.registeration.RegistrationScreen

@Composable
fun SetupScreen(
    referral: String? = null,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Setup.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = SetupViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val page = remember(referral) { referral?.let { 1 } ?: state.page }
    val contentState = remember { mutableIntStateOf(page) }
    SetupNavigation(component) { controller ->
        SetupScaffold(
            header = { SetupHeader(state = contentState, {
                viewModel.lastVisited(it)
            }) },
            footer = {
                SetupFooter(onPrivacy = {
                controller.navigateIfNecessary("privacy")
            }) { controller.navigateIfNecessary("about") } }
        ) {
            SetupScreen(
                state = contentState,
                register = { RegistrationScreen(
                    referral,
                    component,
                    viewModelStoreOwner,
                    onRegistrationSuccess = { contentState.intValue = 0 }
                ) },
                login = { LoginScreen(component, viewModelStoreOwner) {
                    controller.navigateIfNecessary("passwordRequest/$it")
                } },
            )
        }
    }
}

@Composable
fun SetupScreen(
    state: MutableIntState,
    register: @Composable () -> Unit,
    login: @Composable () -> Unit,
) {
    val updatedLogin by rememberUpdatedState(login)
    val updatedRegister by rememberUpdatedState(register)
    Crossfade(targetState = state.intValue) { targetPage ->
        when (targetPage) {
            0 -> updatedLogin()
            1 -> updatedRegister()
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewSetupScreen() {
    val state = remember { mutableIntStateOf(1) }
    PeerTheme {
        SetupScaffold(
            header = { SetupHeader(state = state, {}) },
            footer = { SetupFooter({}) {} },
        ) {
            SetupScreen(
                state = state,
                register = {
                    Text(
                        text = "Register",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                login = {
                    Text(
                        text = "login",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
            )
        }
    }
}
