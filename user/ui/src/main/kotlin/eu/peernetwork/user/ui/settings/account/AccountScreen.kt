package eu.peernetwork.user.ui.settings.account

import android.content.res.Configuration
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignRefreshableScaffold
import eu.peernetwork.core.ui.design.compose.DesignOutlinedButton
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignTitle
import eu.peernetwork.core.ui.design.compose.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.mapper.isPasswordRequired
import eu.peernetwork.user.ui.mapper.mapToModels
import eu.peernetwork.user.ui.model.UiAccount
import eu.peernetwork.user.ui.model.UiOverview
import eu.peernetwork.user.ui.model.UiSettings
import eu.peernetwork.user.ui.compose.LogoutSheet
import eu.peernetwork.user.ui.compose.PasswordSheet
import eu.peernetwork.user.ui.compose.ProfileScaffold

@Composable
fun AccountScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner = UiViewModel.Owner(),
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Account.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = AccountViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                AccountViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                AccountViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is AccountViewModel.State.Content -> {
                    val content = (state as AccountViewModel.State.Content)
                    DesignStatefulScaffoldState.Success(content.account)
                }
                is AccountViewModel.State.Failure -> {
                    DesignStatefulScaffoldState.Error((state as AccountViewModel.State.Failure).error)
                }
            }
        }
    }
    val content = remember { derivedStateOf { state as? AccountViewModel.State.Content? } }
    val error = remember { derivedStateOf {
        content.value?.error?.message?.let { component.resource().string(it) }
    } }
    val isLoading = remember { derivedStateOf { content.value?.processing == true } }
    var status by remember { mutableStateOf(false) }
    val message = stringResource(R.string.profile_update_message)
    DesignRefreshableScaffold<UiAccount>(
        state = derivedState,
        onRefresh = { viewModel.getAccount() },
        placeholder = {
            ProfileScaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp, horizontal = 24.dp)
            )
        }
    ) {
        AccountScreen(
            account = it,
            isLoading = isLoading,
            error = error,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            requiresPassword = { models -> it.isPasswordRequired(models) },
            onLogout = { viewModel.logout() },
            onDeactivate = { viewModel.deactivate(it) }
        ) { model, password ->
            status = true
            viewModel.update(it, model, password ?: "")
        }
    }
    DesignTitleBarHost("AccountScreen") {
        titleBar {
            DesignTitle {
                Text(stringResource(R.string.account_label))
            }
        }
    }
    LaunchedEffect(content.value) {
        if (content.value == null) {
            viewModel.initialize()
        }
        if (isLoading.value == false && status && error.value == null) {
            status = false
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
    DisposableEffect(Unit) {
        onDispose { viewModel.reset() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    account: UiAccount,
    isLoading: State<Boolean>,
    modifier: Modifier = Modifier,
    error: State<String?>,
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
    val logoutHandler by rememberUpdatedState(onLogout)
    val submitHandler by rememberUpdatedState(onSubmit)
    val deactivateHandler by rememberUpdatedState(onDeactivate)
    val passwordValidatorHandler by rememberUpdatedState(requiresPassword)
    Column(modifier = modifier) {
        AccountHeader(
            account = account,
            modifier = Modifier.padding(top = 8.dp),
            isLoading = isLoading.value,
            enabled = !isLoading.value && fields.value != account.mapToModels(),
            onChange = { image.value = it },
            onSubmit = {
                if (!passwordValidatorHandler(fields.value)) {
                    submitHandler(fields.value, null)
                } else {
                    showPassword.value = true
                }
                image.value = null
            }
        )
        AccountForm(username, bio, isLoading, error)
        Row(modifier = Modifier.padding(top = 16.dp)) {
            DesignOutlinedButton(
                onClick = { showLogout.value = true },
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 6.dp),
                textStyle = MaterialTheme.typography.bodySmall,
                content = { Text(stringResource(R.string.logout_text)) }
            )
            DesignOutlinedButton(
                onClick = { showDeactivation.value = true },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 6.dp),
                contentPadding = PaddingValues(14.dp),
                content = { Text(stringResource(R.string.deactivate_text)) },
                textStyle = MaterialTheme.typography.bodySmall,
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
            deactivateHandler(it)
        }
        LogoutSheet(showLogout) {
            showLogout.value = false
            logoutHandler()
        }
        PasswordSheet(showPassword, label = stringResource(R.string.confirmation_label)) {
            showPassword.value = false
            submitHandler(fields.value, it)
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewAccountScreen() {
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
            ),
            isfollowing = false,
            isfollowed = false
        )
        AccountScreen(
            account = model,
            isLoading = remember { mutableStateOf(false) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            error = remember { mutableStateOf("Error message...") },
            onSubmit = { model, password -> }
        )
    }
}
