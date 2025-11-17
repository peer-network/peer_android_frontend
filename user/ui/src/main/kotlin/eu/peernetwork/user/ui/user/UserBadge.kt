package eu.peernetwork.user.ui.user

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignAvatar
import eu.peernetwork.core.ui.design.luna.DesignImage
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.design.material.DesignCard
import eu.peernetwork.core.ui.design.material.DesignDetail
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.user.ui.account.AccountScreen

@Composable
fun UserBadge(
    id: String,
    modifier: Modifier = Modifier,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onClick: () -> Unit,
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
    val localState = remember { derivedStateOf {
        state[id] ?: UserViewModel.State.Empty
    } }
    val derivedState = remember(state) { derivedStateOf {
        val currentState = localState.value
        when(currentState) {
            UserViewModel.State.Empty -> DesignStreamState.Default
            UserViewModel.State.Loading -> DesignStreamState.Loading
            is UserViewModel.State.Success -> {
                DesignStreamState.Success(
                    currentState.let {
                        Pair(it.account, it.configurable)
                    }
                )
            }
            is UserViewModel.State.Error -> {
                DesignStreamState.Error(currentState.error)
            }
        }
    } }
    DesignStream(
        state = derivedState,
        modifier = modifier,
        loading = { UserSkeleton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 10.dp)
        ) }
    ) { data ->
        AccountScreen(data.value.first.username, onClick = onClick) {
            DesignAvatar {
                DesignImage(
                    label = data.value.first.username,
                    imageUrl = data.value.first.imageUrl,
                    size = 42.dp,
                    color = MaterialTheme.colorScheme.surfaceContainerLow
                )
            }
        }
    }
    LaunchedEffect(Unit) {
        if (localState.value is UserViewModel.State.Empty) {
            viewModel.getAccount(id)
        }
    }
}

@Composable
fun UserBadge(
    username: String,
    onClick: (() -> Unit)?,
    avatar: @Composable () -> Unit
) {
    val updatedAvatar by rememberUpdatedState(avatar)
    val handleOnClick by rememberUpdatedState(onClick)
    DesignCard(
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        contentPadding = PaddingValues(12.dp),
        shape = CircleShape,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Button) {
                handleOnClick?.invoke()
            }
    ) {
        DesignDetail(
            verticalAlignment = Alignment.CenterVertically,
            lead = { updatedAvatar() },
            trailing = {
                handleOnClick?.let {
                    Icon(
                        painterResource(R.drawable.ic_next),
                        contentDescription = stringResource(R.string.settings_label),
                        tint = MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.padding(horizontal = 8.dp)
                            .size(12.dp)
                    )
                }
            }
        ) {
            Text(
                text = username,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewAccountPreview() {
    DesignTheme {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp)
        ) {
            UserBadge("John Doe", {}) {
                DesignAvatar {
                    Box(modifier = Modifier
                        .size(48.dp)
                        .background(MaterialTheme.colorScheme.surfaceContainerLow)
                    )
                }
            }
        }
    }
}
