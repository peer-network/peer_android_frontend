package eu.peernetwork.user.ui.settings.account

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import eu.peernetwork.core.ui.design.compose.DesignStatefulScaffold
import eu.peernetwork.core.ui.design.compose.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.luna.DesignImage
import eu.peernetwork.core.ui.design.material.DesignAvatar
import eu.peernetwork.core.ui.design.material.DesignCard
import eu.peernetwork.core.ui.design.material.DesignDetail
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.model.UiAccount

@Composable
fun AccountPreview(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
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
    DesignStatefulScaffold<UiAccount>(
        modifier = modifier,
        state = derivedState,
        onRefresh = { viewModel.getAccount() },
        placeholder = {
            AccountPreview("", onClick = null) {
                DesignAvatar {
                    Box(modifier = Modifier
                        .size(48.dp)
                        .background(MaterialTheme.colorScheme.tertiaryContainer))
                }
            }
        }
    ) {
        AccountPreview(it.username, onClick = onClick) {
            DesignAvatar {
                DesignImage(it.username, it.imageUrl, size = 48.dp)
            }
        }
    }
}

@Composable
fun AccountPreview(
    username: String,
    onClick: (() -> Unit)?,
    avatar: @Composable () -> Unit
) {
    val updatedAvatar by rememberUpdatedState(avatar)
    val handleOnClick by rememberUpdatedState(onClick)
    DesignCard(
        color = MaterialTheme.colorScheme.surfaceVariant,
        contentPadding = PaddingValues(12.dp),
        shape = RoundedCornerShape(24.dp),
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
                        tint = MaterialTheme.colorScheme.surfaceDim.copy(alpha = .5f),
                        modifier = Modifier.padding(horizontal = 8.dp)
                            .size(16.dp)
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
    PeerTheme {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp)
        ) {
            AccountPreview("John Doe", {}) {
                DesignAvatar {
                    Box(modifier = Modifier
                        .size(48.dp)
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                    )
                }
            }
        }
    }
}
