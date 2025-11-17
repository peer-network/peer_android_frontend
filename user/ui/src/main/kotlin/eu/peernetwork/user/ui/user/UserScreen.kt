package eu.peernetwork.user.ui.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.design.material.DesignOverlay
import eu.peernetwork.core.ui.design.material.DesignZoom
import eu.peernetwork.media.core.renderer.ImageView
import eu.peernetwork.user.ui.option.OptionScreen

@Composable
fun UserScreen(
    id: String,
    requireUpdate: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    provider: UiComponentProvider,
    connection: @Composable (Pair<Boolean, Boolean>) -> Unit,
    onClick: (UserMetric) -> Unit,
    onSettings: () -> Unit,
    onMenuClicked: () -> Unit,
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
    val visible = remember(selectedImage.value) {
        mutableStateOf(selectedImage.value != null)
    }
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
                .padding(horizontal = 24.dp)
                .padding(top = 10.dp)
        ) {
            error.value?.let { Text(it) }
        } },
        loading = { UserSkeleton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 10.dp)
        ) }
    ) { data ->
        UserPage(
            account = data.value.first,
            isAdmin = data.value.second,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 10.dp),
            onClick = onClick
        ) {
            Box(modifier = Modifier.padding(start = 24.dp)
                .padding(end = 16.dp)) {
                OptionScreen(
                    isAdmin = data.value.second,
                    provider = component,
                    viewModelStoreOwner = viewModelStoreOwner,
                    onSettings = onSettings,
                    onMenuClicked = onMenuClicked
                ) {
                    updatedConnection(data.value.first.isFollowing
                            to data.value.first.isFollowed)
                }
            }
        }
        DesignOverlay(
            state = visible,
            onDismiss = { selectedImage.value = null }
        ) {
            DesignZoom(background = {
                component.imageView()(
                    modifier = Modifier,
                    spec = ImageView.Spec(
                        url = data.value.first.imageUrl,
                        ratio = null,
                        blur = 500f,
                        contentScale = ContentScale.Crop,
                    )
                )
            }) {
                component.imageView()(
                    modifier = Modifier,
                    spec = ImageView.Spec(data.value.first.imageUrl, null)
                )
            }
        }
    }
    LaunchedEffect(requireUpdate.value) {
        if (requireUpdate.value) {
            viewModel.getAccount(id)
            requireUpdate.value = false
        }
    }
    LaunchedEffect(Unit) {
        if (localState.value is UserViewModel.State.Empty) {
            viewModel.getAccount(id)
        }
    }
}
