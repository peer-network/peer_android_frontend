package eu.peernetwork.user.ui.login

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.compose.DesignButton
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.R

@Composable
fun LoginScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Login.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = LoginViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    LoginScaffold(
        loading = state is LoginViewModel.State.Loading,
        error = (state as? LoginViewModel.State.Error?)?.error?.message,
    ) { email, password ->
        viewModel.login(email, password)
    }
}

@Composable
private fun LoginScaffold(
    loading: Boolean = false,
    error: String? = null,
    onSubmit: (String, String) -> Unit
) {
    val email = remember { TextFieldState("") }
    val password = remember { TextFieldState("") }
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (form, cta) = createRefs()
        LoginForm(
            email = email,
            password = password,
            modifier = Modifier.constrainAs(form) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )
        DesignButton(
            onClick = { onSubmit(email.text.toString(), password.text.toString()) },
            modifier = Modifier.constrainAs(cta) {
                top.linkTo(form.bottom, margin = 16.dp)
                start.linkTo(parent.start, margin = 24.dp)
                end.linkTo(parent.end, margin = 24.dp)
                bottom.linkTo(parent.bottom, margin = 24.dp)
                width = Dimension.fillToConstraints
            }
        ) {
            Text(
                text = stringResource(R.string.login_text),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewLoginScaffold() {
    PeerTheme {
        LoginScaffold { email, password -> }
    }
}
