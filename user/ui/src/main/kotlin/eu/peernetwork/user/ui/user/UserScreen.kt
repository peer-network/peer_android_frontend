package eu.peernetwork.user.ui.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.user.ui.extension.route
import eu.peernetwork.user.ui.option.OptionScreen
import eu.peernetwork.user.ui.user.UserNavigator.Companion.LocalUserNavigator

@Composable
fun UserScreen(
    id: String,
    timestamp: State<Long>,
    modifier: Modifier = Modifier,
    provider: UiComponentProvider,
    connection: @Composable (Pair<Boolean, Boolean>) -> Unit,
    onClick: (UserMetric) -> Unit,
    onBlock: () -> Unit,
    onSettings: () -> Unit,
    onMenuClicked: () -> Unit,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val navigator = LocalUserNavigator.current
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
    val updatedConnection by rememberUpdatedState(connection)
    val selectedImage = remember { mutableStateOf<String?>(null) }
    val currentTimestamp = remember { derivedStateOf {
        (localState.value as? UserViewModel.State.Success?)?.timestamp
    } }
    val error = remember { derivedStateOf {
        (localState.value as? UserViewModel.State.Error?)?.error?.message?.let {
            component.resource().string(it)
        }
    } }
    DesignStream(
        state = derivedState,
        modifier = modifier,
        error = { UserError(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 10.dp)
        ) {
            error.value?.let { Text(it) }
        } },
        loading = { UserSkeleton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
                .padding(end = 16.dp)
                .padding(top = 10.dp)
        ) }
    ) { data ->
        Column {
            UserMask(
                metric = data.value.first.metric,
                status = data.value.first.status,
                isAuthor = data.value.second,
                isAccessible = data.value.first.isAccessible,
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 10.dp),
            ) {
                UserPage(
                    account = data.value.first,
                    isAdmin = data.value.second,
                    selectedImage = selectedImage,
                    modifier = Modifier,
                    onContentClick = { type, value -> navigator.navigate(type.route(value)) },
                    onClick = onClick
                )
            }
            Box(modifier = Modifier.padding(start = 20.dp)
                .padding(end = 12.dp)) {
                OptionScreen(
                    isAdmin = data.value.second,
                    provider = component,
                    viewModelStoreOwner = viewModelStoreOwner,
                    onSettings = onSettings,
                    onBlock = onBlock,
                    onMenuClicked = onMenuClicked
                ) {
                    updatedConnection(data.value.first.isFollowing
                            to data.value.first.isFollowed)
                }
            }
        }
        UserModal(
            image = selectedImage,
            component = component
        )
    }
    LaunchedEffect(timestamp.value) {
        if (timestamp.value != currentTimestamp.value) {
            viewModel.getAccount(id, timestamp.value)
        }
    }
}
