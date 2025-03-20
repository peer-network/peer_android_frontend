package eu.peernetwork.app.ui.setup

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.compose.DesignContainer
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.toInt
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.login.LoginScreen
import eu.peernetwork.user.ui.registeration.RegistrationScreen

@Composable
fun SetupScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    isRegistration: Boolean,
    onOptionChange: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    var isRegistrationState = remember { mutableStateOf(isRegistration) }
    val component = remember {
        provider.builder(Setup.Builder::class.java).build(context)
    }
    DesignContainer {
        SetupScaffold(
            header = {
                SetupHeader(
                    state = isRegistrationState
                )
            },
            footer = { SetupFooter(onPrivacy = {}) }
        ) {
            SetupContent(
                page = isRegistrationState,
                register = { RegistrationScreen(
                    component,
                    viewModelStoreOwner,
                    onRegistrationSuccess = { isRegistrationState.value = false }
                ) },
                login = { LoginScreen(component, viewModelStoreOwner) },
                onOptionChange = onOptionChange
            )
        }
    }
}

@Composable
fun SetupContent(
    page: MutableState<Boolean>,
    register: @Composable () -> Unit,
    login: @Composable () -> Unit,
    onOptionChange: (Boolean) -> Unit,
) {
    val state = rememberPagerState(pageCount = { 2 }, initialPage = page.value.toInt())
    HorizontalPager(
        state = state,
        verticalAlignment = Alignment.Top,
        userScrollEnabled = false
    ) { page ->
        Crossfade(targetState = page) { targetPage ->
            when (targetPage) {
                0 -> login()
                1 -> register()
            }
        }
    }
    LaunchedEffect(page.value) {
        onOptionChange(page.value)
        state.scrollToPage(page.value.toInt())
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewSetupScreen() {
    val isRegistration = remember { mutableStateOf(false) }
    PeerTheme {
        SetupScaffold(
            header = {
                SetupHeader(state = rememberSaveable { mutableStateOf(false) })
            },
            footer = { SetupFooter(onPrivacy = {}) },
        ) {
            SetupContent(
                page = isRegistration,
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
                onOptionChange = {}
            )
        }
    }
}
