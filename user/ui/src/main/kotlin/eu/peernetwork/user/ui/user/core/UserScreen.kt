package eu.peernetwork.user.ui.user.core

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
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
import eu.peernetwork.core.ui.design.view.DesignErrorDetail
import eu.peernetwork.core.ui.design.view.DesignStatefulContent
import eu.peernetwork.core.ui.design.view.DesignStatefulContentState
import eu.peernetwork.user.ui.compose.UserOverview

@Composable
fun UserScreen(
    modifier: Modifier = Modifier,
    provider: UiComponentProvider,
    onEvent: (UserEvent) -> Unit,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(User.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = UserViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember { derivedStateOf {
        when(state) {
            UserViewModel.State.Empty -> DesignStatefulContentState.Empty
            UserViewModel.State.Loading -> DesignStatefulContentState.Loading
            is UserViewModel.State.Success -> {
                DesignStatefulContentState.Success(
                    (state as UserViewModel.State.Success).account
                )
            }
            is UserViewModel.State.Error -> {
                DesignStatefulContentState.Error(
                    (state as UserViewModel.State.Error).error
                )
            }
        }
    } }
    DesignStatefulContent<UiAccount>(
        state = derivedState,
        refresh = { viewModel.initialize() },
        placeholder = { UserSkeleton(modifier = modifier.padding(end = 8.dp)) },
        errorContent = {
            DesignErrorDetail(
                error = it,
                modifier = Modifier.padding(
                    horizontal = 24.dp,
                    vertical = 8.dp
                ),
                onRetry = { viewModel.getAccount() }
            )
        }
    ) {
        UserContent(
            modifier = modifier,
            account = it,
            onEvent = onEvent
        )
    }
}

@Composable
fun UserContent(
    account: UiAccount,
    onEvent: (UserEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val emptyDescription = stringResource(eu.peernetwork.user.ui.R.string.empty_description_message)
    var username = remember { derivedStateOf { account.username } }
    var slug = remember { derivedStateOf { account.slug.toString() } }
    var imageUrl = remember { derivedStateOf { account.imageUrl } }
    var biography = remember { derivedStateOf { account.bio ?: emptyDescription } }
    UserScaffold(
        modifier = modifier,
        avatar = { DesignAsyncImage(username, imageUrl) },
        actions = {
            IconButton(onClick = {
                onEvent(UserEvent.Settings)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings),
                    contentDescription = stringResource(R.string.settings_label),
                    modifier = Modifier.size(18.dp)
                )
            }
        },
        options = {
            UserOverview(
                overview = account.overview,
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { DesignTitle(username, slug, biography) }
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
        UserContent(onEvent = { }, account = model)
    }
}
