package eu.peernetwork.user.ui.user

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.model.UiAccount
import eu.peernetwork.user.ui.model.UiOverview
import eu.peernetwork.core.ui.design.compose.DesignAsyncImage
import eu.peernetwork.core.ui.design.compose.DesignTitle
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignOutlinedButton
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.compose.Overview
import eu.peernetwork.user.ui.compose.ProfileScaffold

@Composable
fun UserScreen(
    id: String,
    loadState: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    provider: UiComponentProvider,
    onFollow: () -> Unit,
    onClick: (Int) -> Unit,
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
            UserViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
            UserViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
            is UserViewModel.State.Success -> {
                DesignStatefulScaffoldState.Success(
                    (state as UserViewModel.State.Success).account
                )
            }
            is UserViewModel.State.Error -> {
                DesignStatefulScaffoldState.Error(
                    (state as UserViewModel.State.Error).error
                )
            }
        }
    } }
    DesignStatefulScaffold<UiAccount>(
        state = derivedState,
        onRefresh = { viewModel.getAccount(id) },
        placeholder = { ProfileScaffold(modifier = modifier.padding(end = 8.dp)) },
        errorContent = { ProfileScaffold(modifier = modifier.padding(end = 8.dp)) }
    ) {
        UserScreen(
            modifier = modifier,
            account = it,
            onFollow = onFollow,
            onClick = onClick
        )
    }
    LaunchedEffect(loadState.value) {
        if (loadState.value) {
            viewModel.getAccount(id)
            loadState.value = false
        }
    }
}

@Composable
fun UserScreen(
    account: UiAccount,
    modifier: Modifier = Modifier,
    onFollow: () -> Unit,
    onClick: (Int) -> Unit,
) {
    val emptyDescription = stringResource(R.string.empty_description_message)
    ProfileScaffold(
        modifier = modifier,
        avatar = { DesignAsyncImage(account.username, account.imageUrl) },
        actions = {
            Box(
                modifier = Modifier.padding(vertical = 8.dp)
                    .padding(bottom = 4.dp)
            ) {
                DesignOutlinedButton(
                    onClick = onFollow,
                    isLoading = false,
                    shape = RoundedCornerShape(8.dp),
                    textStyle = MaterialTheme.typography.bodySmall,
                    enabled = false,
                    contentPadding = PaddingValues(vertical = 4.dp, horizontal = 32.dp),
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .height(28.dp),
                    content = {
                        Text(
                            text = stringResource(R.string.follow_label),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
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
fun PreviewUserScreen() {
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
        UserScreen(onFollow = { }, account = model) {}
    }
}
