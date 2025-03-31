package eu.peernetwork.user.ui.user.settings

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.compose.DesignOutlinedButton
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.mapper.isPasswordRequired
import eu.peernetwork.user.ui.mapper.mapToModels
import eu.peernetwork.user.ui.model.UiAccount
import eu.peernetwork.user.ui.model.UiOverview
import eu.peernetwork.user.ui.model.UiSettings
import eu.peernetwork.user.ui.user.compose.LogoutSheet
import eu.peernetwork.user.ui.user.compose.PasswordSheet

@Composable
fun UserSettingsScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(UserSettings.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = UserSettingsViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val content = remember { derivedStateOf { state as? UserSettingsViewModel.State.Content? } }
    val error = remember { derivedStateOf { content.value?.error } }
    val isLoading = remember { derivedStateOf { content.value?.processing == true } }
    content.value?.account?.let {
        UserSettingsContent(
            account = it,
            isLoading = isLoading,
            error = error,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            requiresPassword = { models -> it.isPasswordRequired(models) },
            onLogout = { viewModel.logout() },
            onDeactivate = { viewModel.deactivate(it) }
        ) { model, password ->
            viewModel.update(it, model, password ?: "")
        }
    }
    LaunchedEffect(Unit) { viewModel.reset() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSettingsContent(
    account: UiAccount,
    isLoading: State<Boolean>,
    modifier: Modifier = Modifier,
    error: State<Throwable?>,
    requiresPassword: (List<UiSettings>) -> Boolean = { false },
    onLogout: () -> Unit = {},
    onDeactivate: (String) -> Unit = {},
    onSubmit: (List<UiSettings>, String?) -> Unit,
) {
    val image = remember { mutableStateOf<Uri?>(null) }
    val username = remember { TextFieldState(account.username) }
    val bio = remember { TextFieldState(account.bio ?: "") }
    var showPassword = remember { mutableStateOf(false) }
    var showLogout = remember { mutableStateOf(false) }
    var showDeactivation = remember { mutableStateOf(false) }
    val fields = remember { derivedStateOf {
        listOf(
            UiSettings.Avatar(image.value),
            UiSettings.Username(username.text.trim().toString()),
            UiSettings.Description(bio.text.trim().toString()),
        )
    } }
    Column(modifier = modifier) {
        UserSettingsHeader(
            account = account,
            isLoading = isLoading.value,
            enabled = !isLoading.value && fields.value != account.mapToModels(),
            onChange = { image.value = it },
            onSubmit = {
                if (!requiresPassword(fields.value)) {
                    onSubmit(fields.value, null)
                } else {
                    showPassword.value = true
                }
                image.value = null },
        )
        UserSettingsForm(username, bio, isLoading, error)
        Row(modifier = Modifier.padding(top = 16.dp)) {
            DesignOutlinedButton(
                onClick = { showLogout.value = true },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 6.dp),
                content = { Text(stringResource(R.string.logout_text)) }
            )
            DesignOutlinedButton(
                onClick = { showDeactivation.value = true },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 6.dp),
                content = { Text(stringResource(R.string.deactivate_text)) },
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.errorContainer),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.errorContainer,
                    disabledContentColor = MaterialTheme.colorScheme.errorContainer
                )
            )
        }
        PasswordSheet(showDeactivation, label = stringResource(R.string.deactivate_text)) {
            showDeactivation.value = false
            onDeactivate(it)
        }
        LogoutSheet(showLogout) {
            showLogout.value = false
            onLogout()
        }
        PasswordSheet(showPassword, label = stringResource(R.string.confirmation_label)) {
            showPassword.value = false
            onSubmit(fields.value, it)
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewUserSettingsContent() {
    PeerTheme {
        val model = UiAccount(
            id = System.currentTimeMillis().toString(),
            username = "John Doe",
            slug = 0,
            bio = "Description....",
            imageUrl = "",
            overview = UiOverview(
                posts = 0,
                peers = 0,
                followers = 0,
                followed = 0
            )
        )
        UserSettingsContent(
            account = model,
            isLoading = remember { mutableStateOf(false) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            error = remember { mutableStateOf(RuntimeException("Error message...")) },
            onSubmit = { model, password -> }
        )
    }
}
