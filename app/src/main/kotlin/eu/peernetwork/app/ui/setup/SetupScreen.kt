package eu.peernetwork.app.ui.setup

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.compose.FlexBox
import eu.peernetwork.core.ui.compose.ScrollableBox
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun SetupScreen(
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
    ScrollableBox {
        SetupScaffold(
            header = {
                SetupHeader(
                    onLogin = { viewModel.toggle(false) },
                    onRegister = { viewModel.toggle(true) }
                )
            },
            footer = { SetupFooter(onPrivacy = {}) }
        ) {
            when (state) {
                is SetupViewModel.State.Login -> Text(
                    "Login",
                    color = MaterialTheme.colorScheme.onBackground,
                )
                is SetupViewModel.State.Register -> Text(
                    "Register",
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}

@Composable
fun SetupScaffold(
    header: @Composable () -> Unit,
    footer: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
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
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewSetupScaffold() {
    PeerTheme {
        SetupScaffold(
            header = {
                SetupHeader(
                    onLogin = {},
                    onRegister = {}
                )
            },
            footer = { SetupFooter(onPrivacy = {}) }
        ) { }
    }
}
