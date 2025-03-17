package eu.peernetwork.app.ui.setup

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    showRegistration: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    var isRegistrationState by remember { mutableStateOf(isRegistration) }
    val component = remember {
        provider.builder(Setup.Builder::class.java).build(context)
    }
    DesignContainer {
        SetupScaffold(
            header = {
                SetupHeader(
                    onLogin = {
                        isRegistrationState = false
                        showRegistration(false) },
                    onRegister = {
                        isRegistrationState = true
                        showRegistration(true) },
                    isRegistration = isRegistrationState
                )
            },
            footer = { SetupFooter(onPrivacy = {}) }
        ) {
            SetupPager(
                page = isRegistrationState.toInt(),
                register = { RegistrationScreen(
                    component,
                    viewModelStoreOwner,
                    onRegistered = { isRegistrationState = false }
                ) },
                login = { LoginScreen(component, viewModelStoreOwner) }
            )
        }
    }
}

@Composable
private fun SetupPager(
    page: Int,
    register: @Composable () -> Unit,
    login: @Composable () -> Unit,
) {
    val pager = rememberPagerState(pageCount = { 2 }, initialPage = page)
    HorizontalPager(
        state = pager,
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
    LaunchedEffect(page) { pager.scrollToPage(page) }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewSetupScreen() {
    PeerTheme {
        SetupScaffold(
            header = {
                SetupHeader(
                    onLogin = {},
                    onRegister = {},
                    isRegistration = false
                )
            },
            footer = { SetupFooter(onPrivacy = {}) },
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                text = "content",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
