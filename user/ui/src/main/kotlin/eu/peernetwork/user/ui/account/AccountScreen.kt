package eu.peernetwork.user.ui.account

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.model.UiAccount
import eu.peernetwork.user.ui.model.UiOverview
import eu.peernetwork.core.ui.design.compose.DesignAsyncImage
import eu.peernetwork.core.ui.design.compose.DesignTitle
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.user.ui.compose.Overview
import eu.peernetwork.user.ui.compose.ProfileScaffold

@Composable
fun AccountScreen(
    loadState: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    provider: UiComponentProvider,
    onClick: (Int) -> Unit,
    onSettings: () -> Unit,
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
    val derivedState = remember { derivedStateOf {
        when(state) {
            AccountViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
            AccountViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
            is AccountViewModel.State.Success -> {
                DesignStatefulScaffoldState.Success(
                    (state as AccountViewModel.State.Success).account
                )
            }
            is AccountViewModel.State.Error -> {
                DesignStatefulScaffoldState.Error(
                    (state as AccountViewModel.State.Error).error
                )
            }
        }
    } }
    DesignStatefulScaffold<UiAccount>(
        state = derivedState,
        onRefresh = { viewModel.initialize() },
        placeholder = { ProfileScaffold(modifier = modifier.padding(end = 8.dp)) },
        errorContent = {
            ProfileScaffold(modifier = modifier.padding(end = 8.dp)) {
                IconButton(onClick = onSettings) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_settings),
                        contentDescription = stringResource(R.string.settings_label),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    ) {
        AccountScreen(
            modifier = modifier,
            account = it,
            onSettings = onSettings,
            onClick = onClick
        )
    }
    LaunchedEffect(loadState.value) {
        if (loadState.value) {
            viewModel.refreshAccount()
            loadState.value = false
        }
    }
}

@Composable
fun AccountScreen(
    account: UiAccount,
    onSettings: () -> Unit,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit,
) {
    val emptyDescription = stringResource(eu.peernetwork.user.ui.R.string.empty_description_message)
    ProfileScaffold(
        modifier = modifier,
        avatar = { DesignAsyncImage(account.username, account.imageUrl) },
        actions = {
            IconButton(onClick = onSettings) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings),
                    contentDescription = stringResource(R.string.settings_label),
                    modifier = Modifier.size(18.dp)
                )
            }
        },
        options = {
            Overview(
                overview = account.overview,
                modifier = Modifier.fillMaxWidth(),
                onClick = onClick
            )
        }
    ) {
        DesignTitle(
            account.username,
            account.slug.toString(),
            account.bio ?: emptyDescription
        )
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
            )
        )
        AccountScreen(onSettings = { }, account = model) {}
    }
}
