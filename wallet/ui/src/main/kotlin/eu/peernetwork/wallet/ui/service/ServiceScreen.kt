package eu.peernetwork.wallet.ui.service

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignStatefulScaffold
import eu.peernetwork.core.ui.design.compose.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.material.DesignAvatar
import eu.peernetwork.core.ui.design.material.DesignCard
import eu.peernetwork.core.ui.design.material.DesignOutlinedButton
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.wallet.ui.model.UiRecipient
import java.util.UUID

sealed interface ServiceState {
    data object Default : ServiceState
    data class Transfer(val recipient: UiRecipient) : ServiceState
}

@Composable
fun ServiceScreen(
    serviceState: MutableState<ServiceState>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onAccountClicked: (String) -> Unit,
    onClear: () -> Unit,
    onClick: () -> Unit,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Service.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = ServiceViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember { derivedStateOf {
        when(state) {
            ServiceViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
            ServiceViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
            is ServiceViewModel.State.Success -> {
                val data = (state as ServiceViewModel.State.Success)
                DesignStatefulScaffoldState.Success(data.tax)
            }
            is ServiceViewModel.State.Error -> {
                DesignStatefulScaffoldState.Error((state as ServiceViewModel.State.Error).error)
            }
        }
    } }
    DesignStatefulScaffold<Double>(
        derivedState,
        onRefresh = { viewModel.initialize() },
        placeholder = { ServiceScreen() },
        errorContent = { ServiceError(it, component.resource()) { viewModel.initialize() } }
    ) {
        ServiceTransfer(
            it,
            serviceState,
            component,
            viewModelStoreOwner,
            onAccountClicked,
            onClear,
            onClick
        )
    }
}

@Composable
fun ServiceScreen(
    name: String,
    state: State<ServiceState>,
    onClick: () -> Unit,
    content: @Composable (ServiceState.Transfer) -> Unit
) {
    val updateContent by rememberUpdatedState(content)
    Crossfade(state.value) { target ->
        when(target) {
            ServiceState.Default -> {
                DesignOutlinedButton(
                    onClick = onClick,
                    shape = RoundedCornerShape(8.dp),
                    textStyle = MaterialTheme.typography.bodySmall,
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 32.dp),
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .height(36.dp)
                        .fillMaxWidth(),
                    content = {
                        Text(text = name, style = MaterialTheme.typography.bodySmall)
                    }
                )
            }
            is ServiceState.Transfer -> { updateContent(target) }
        }
    }
}

@Composable
fun ServiceScreen() {
    DesignCard(
        color = MaterialTheme.colorScheme.surfaceVariant,
        contentPadding = PaddingValues(16.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        DesignAvatar {
            Box(modifier = Modifier
                .size(36.dp)
                .background(MaterialTheme.colorScheme.background),
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewServiceScreen() {
    PeerTheme {
        Column {
            val recipient = UiRecipient(
                id = UUID.randomUUID().toString(),
                slug = "1234",
                username = "johnDoe",
                imageUrl = "http://localhost"
            )
            ServiceScreen(
                "ServiceScreen",
                remember { mutableStateOf(ServiceState.Transfer(recipient)) },
                {}
            ) {
                ServiceScreen()
            }
            Spacer(modifier = Modifier.height(16.dp))
            ServiceScreen(
                "ServiceScreen",
                remember { mutableStateOf(ServiceState.Default) },
                {}
            ) {}
        }
    }
}
