package eu.peernetwork.user.ui.account

import android.content.res.Configuration
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignRefreshableScaffold
import eu.peernetwork.core.ui.design.compose.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.material.DesignTitle
import eu.peernetwork.core.ui.design.material.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.mapper.isPasswordRequired
import eu.peernetwork.user.ui.model.UiAccount
import eu.peernetwork.user.ui.model.UiMetric
import eu.peernetwork.user.ui.model.UiSettings
import eu.peernetwork.user.ui.compose.password.PasswordSheet
import eu.peernetwork.user.ui.compose.account.ProfileScaffold
import eu.peernetwork.user.ui.mapper.mapToModels

@Composable
fun AccountSettings(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
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
                AccountViewModel.State.Default -> DesignStatefulScaffoldState.Empty
                AccountViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is AccountViewModel.State.Content -> {
                    val content = (state as AccountViewModel.State.Content)
                    DesignStatefulScaffoldState.Success(content.account)
                }
                is AccountViewModel.State.Error -> {
                    DesignStatefulScaffoldState.Error((state as AccountViewModel.State.Error).error)
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
        onRefresh = { viewModel.get() },
        placeholder = {
            ProfileScaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp, horizontal = 24.dp)
            )
        }
    ) {
        AccountSettings(
            account = it,
            isLoading = isLoading,
            error = error,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            requiresPassword = { models -> it.isPasswordRequired(models) }
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
        if (!isLoading.value && status && error.value == null) {
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
fun AccountSettings(
    account: UiAccount,
    isLoading: State<Boolean>,
    modifier: Modifier = Modifier,
    error: State<String?>,
    requiresPassword: (List<UiSettings>) -> Boolean = { false },
    onSubmit: (List<UiSettings>, String?) -> Unit,
) {
    val image = remember { mutableStateOf<Uri?>(null) }
    val username = remember { TextFieldState(account.username) }
    val bio = remember { TextFieldState(account.bio ?: "") }
    val showPassword = remember { mutableStateOf(false) }
    val fields = remember { derivedStateOf {
        listOf(
            UiSettings.Avatar(image.value),
            UiSettings.Username(username.text.trim().toString()),
            UiSettings.Description(bio.text.trim().toString()),
        )
    } }
    val submitHandler by rememberUpdatedState(onSubmit)
    val passwordValidatorHandler by rememberUpdatedState(requiresPassword)
    val isEnabled = remember { derivedStateOf {
        !isLoading.value && fields.value != account.mapToModels()
    } }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        AccountAvatar(
            name = account.username,
            imageUrl = account.imageUrl,
            onChange = { image.value = it },
        )
        Spacer(modifier = Modifier.height(28.dp))
        AccountForm(
            username = username,
            bio = bio,
            enable = isEnabled,
            isLoading = isLoading,
            error = error
        ) {
            if (!passwordValidatorHandler(fields.value)) {
                submitHandler(fields.value, null)
            } else {
                showPassword.value = true
            }
            image.value = null
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
    DesignTheme {
        val model = UiAccount(
            id = System.currentTimeMillis().toString(),
            username = "John Doe",
            slug = 0,
            bio = "Description....",
            imageUrl = "",
            metric = UiMetric(
                posts = 0,
                peers = 0,
                followers = 0,
                followed = 0
            ),
            isFollowing = false,
            isFollowed = false
        )
        AccountSettings(
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
