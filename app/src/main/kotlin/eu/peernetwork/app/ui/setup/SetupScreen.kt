package eu.peernetwork.app.ui.setup

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.compose.FlexBox
import eu.peernetwork.core.ui.compose.DesignContainer
import eu.peernetwork.core.ui.extension.builder
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
    val component = remember {
        provider.builder(Setup.Builder::class.java).build(context)
    }
    DesignContainer {
        SetupScaffold(
            header = {
                SetupHeader(
                    onLogin = { showRegistration(false) },
                    onRegister = { showRegistration(true) },
                    isRegistration = isRegistration
                )
            },
            footer = { SetupFooter(onPrivacy = {}) }
        ) {
            if (isRegistration) {
                RegistrationScreen(component, viewModelStoreOwner)
            } else {
                LoginScreen(component, viewModelStoreOwner)
            }
        }
    }
}

@Composable
private fun SetupScaffold(
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit,
    footer: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (headerTag, sectionTag, footerTag) = createRefs()
        createVerticalChain(headerTag, sectionTag, chainStyle = ChainStyle.Packed)
        Box(
            modifier = Modifier.constrainAs(headerTag) {
                top.linkTo(parent.top)
                start.linkTo(parent.start, margin = 24.dp)
                end.linkTo(parent.end, margin = 24.dp)
                bottom.linkTo(sectionTag.top)
            },
        ) { header() }
        FlexBox(
            minHeight = 0.45f,
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.constrainAs(sectionTag) {
                top.linkTo(headerTag.bottom)
                start.linkTo(parent.start, margin = 24.dp)
                end.linkTo(parent.end, margin = 24.dp)
                bottom.linkTo(footerTag.top)
            }
        ) { content() }
        Box(
            modifier = Modifier.constrainAs(footerTag) {
                top.linkTo(sectionTag.bottom)
                start.linkTo(parent.start, margin = 24.dp)
                end.linkTo(parent.end, margin = 24.dp)
                bottom.linkTo(parent.bottom, margin = 36.dp)
            }
        ) { footer() }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewSetupScaffold() {
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
        ) { }
    }
}
