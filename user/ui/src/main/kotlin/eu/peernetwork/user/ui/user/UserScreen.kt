package eu.peernetwork.user.ui.user

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
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
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignZoom
import eu.peernetwork.core.ui.design.compose.DesignLead
import eu.peernetwork.core.ui.design.compose.DesignOverlay
import eu.peernetwork.core.ui.extension.toInt
import eu.peernetwork.media.core.renderer.ImageView
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.compose.Overview
import eu.peernetwork.user.ui.compose.ProfileScaffold

@Composable
fun UserScreen(
    id: String,
    lastUpdated: State<Long>,
    modifier: Modifier = Modifier,
    provider: UiComponentProvider,
    onFollow: @Composable (Pair<Boolean, Boolean>) -> Unit,
    onClick: (Int) -> Unit,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    onSettings: () -> Unit,
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
    val derivedState = remember(state) { derivedStateOf {
        when(state) {
            UserViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
            UserViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
            is UserViewModel.State.Success -> {
                DesignStatefulScaffoldState.Success(
                    (state as UserViewModel.State.Success).let {
                        Pair(it.account, it.configurable)
                    }
                )
            }
            is UserViewModel.State.Error -> {
                DesignStatefulScaffoldState.Error(
                    (state as UserViewModel.State.Error).error
                )
            }
        }
    } }
    val updatedAt = remember { mutableLongStateOf(lastUpdated.value) }
    DesignStatefulScaffold<Pair<UiAccount, Boolean>>(
        state = derivedState,
        onRefresh = { viewModel.getAccount(id) },
        placeholder = { ProfileScaffold(modifier = modifier.padding(end = 8.dp)) },
        errorContent = { ProfileScaffold(modifier = modifier.padding(end = 8.dp)) }
    ) {
        UserScreen(
            modifier = modifier,
            account = it.first,
            connection = onFollow,
            showPeers = it.second,
            onSettings = if (it.second) {
                onSettings
            } else {
                null
            },
            onClick = onClick,
            avatar = {
                DesignZoom({
                    component.imageView()(
                        Modifier,
                        ImageView.Spec(
                            it.first.imageUrl,
                            null,
                            ContentScale.Crop,
                            500f,
                        )
                    )
                }) {
                    component.imageView()(
                        Modifier,
                        ImageView.Spec(it.first.imageUrl, null)
                    )
                }
            },
            onMentionClick = onMentionClick,
            onHashtagClick = onHashtagClick,
        )
    }
    LaunchedEffect(lastUpdated.value) {
        if (updatedAt.longValue != lastUpdated.value) {
            viewModel.getAccount(id)
            updatedAt.longValue = lastUpdated.value
        }
    }
    LaunchedEffect(Unit) { viewModel.initialize() }
}

@Composable
fun UserScreen(
    account: UiAccount,
    modifier: Modifier = Modifier,
    showPeers: Boolean,
    avatar: @Composable () -> Unit = {},
    connection: @Composable (Pair<Boolean, Boolean>) -> Unit,
    onSettings: (() -> Unit)? = null,
    onClick: (Int) -> Unit,
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
) {
    val clickHandler by rememberUpdatedState(onClick)
    val settingsHandler by rememberUpdatedState(onSettings)
    val selectedImage = remember { mutableStateOf<String?>(null) }
    val updatedAvatar by rememberUpdatedState(avatar)
    val updatedConnection by rememberUpdatedState(connection)
    val emptyDescription = stringResource(R.string.empty_description_message)
    val visible = remember(selectedImage.value) { mutableStateOf(selectedImage.value != null) }
    ProfileScaffold(
        modifier = modifier,
        avatar = {
            DesignAsyncImage(
                account.username,
                account.imageUrl,
                modifier = Modifier.clickable(role = Role.Button, enabled = true) {
                    selectedImage.value = account.imageUrl
                }) },
        actions = {
            if (settingsHandler != null) {
                IconButton(onClick = { settingsHandler?.invoke() }) {
                    Icon(
                        painter = painterResource(id = eu.peernetwork.core.ui.R.drawable.ic_settings),
                        contentDescription = stringResource(eu.peernetwork.core.ui.R.string.settings_label),
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .padding(bottom = 4.dp)
                ) { updatedConnection(account.isfollowing to account.isfollowed) }
            }
        },
        options = {
            Overview(
                overview = account.overview,
                modifier = Modifier.fillMaxWidth(),
                onClick = { if (it < (2 + showPeers.toInt())) clickHandler(it) }
            )
        }
    ) {
        DesignLead(
            account.username,
            account.slug.toString(),
            account.bio ?: AnnotatedString(emptyDescription),
            onMentionClick = onMentionClick,
            onHashtagClick = onHashtagClick
        )
    }
    DesignOverlay(
        visible,
        onDismiss = {
            selectedImage.value = null
        }
    ) { updatedAvatar() }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewUserScreen() {
    PeerTheme {
        val model = UiAccount(
            id = System.currentTimeMillis().toString(),
            username = "John Doe",
            slug = 0,
            bio = AnnotatedString("Description...."),
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
        UserScreen(connection = { }, account = model, showPeers = true, onClick = {}) {}
    }
}
