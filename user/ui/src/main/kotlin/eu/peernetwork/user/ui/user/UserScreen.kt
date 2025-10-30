package eu.peernetwork.user.ui.user

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import eu.peernetwork.user.ui.model.UiAccount
import eu.peernetwork.core.ui.design.compose.DesignStatefulScaffold
import eu.peernetwork.core.ui.design.compose.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.material.DesignOverlay
import eu.peernetwork.core.ui.design.material.DesignZoom
import eu.peernetwork.media.core.renderer.ImageView
import eu.peernetwork.user.ui.compose.account.ProfileScaffold
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
    val updatedConnection by rememberUpdatedState(connection)
    val selectedImage = remember { mutableStateOf<String?>(null) }
    val visible = remember(selectedImage.value) {
        mutableStateOf(selectedImage.value != null)
    }
    DesignStatefulScaffold<Pair<UiAccount, Boolean>>(
        state = derivedState,
        onRefresh = { viewModel.getAccount(id) },
        placeholder = { ProfileScaffold(modifier = modifier.padding(end = 8.dp)) },
        errorContent = { ProfileScaffold(modifier = modifier.padding(end = 8.dp)) }
    ) { data ->
        UserPage(
            account = data.first,
            isAdmin = data.second,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 10.dp),
            onClick = onClick
        ) {
            OptionScreen(
                isAdmin = data.second,
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner,
                onSettings = onSettings
            ) { updatedConnection(data.first.isFollowing to data.first.isFollowed) }
        }
        DesignOverlay(
            state = visible,
            onDismiss = { selectedImage.value = null }
        ) {
            DesignZoom(background = {
                component.imageView()(
                    modifier = Modifier,
                    spec = ImageView.Spec(
                        url = data.first.imageUrl,
                        ratio = null,
                        blur = 500f,
                        contentScale = ContentScale.Crop,
                    )
                )
            }) {
                component.imageView()(
                    modifier = Modifier,
                    spec = ImageView.Spec(data.first.imageUrl, null)
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
    LaunchedEffect(Unit) { viewModel.initialize() }
}
