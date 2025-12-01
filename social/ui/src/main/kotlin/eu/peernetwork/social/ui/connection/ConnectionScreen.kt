package eu.peernetwork.social.ui.connection

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignOutlinedButton
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.social.ui.R
import eu.peernetwork.social.ui.connection.ConnectionInteractor.Companion.LocalConnectionInteractor
import kotlinx.coroutines.flow.StateFlow

data class ConnectionState(
    val res: Int,
    val textColor: Color,
    val borderColor: Color,
    val outline: Boolean
)

enum class ConnectionStatus {
    PEER,
    FOLLOWING,
    FOLLOWER
}

@Composable
fun ConnectionScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Connection.Builder::class.java).build(context)
    }
    val viewModel: ConnectionViewModel = viewModel(
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state = viewModel.state.collectAsState().value
    val updatedContent by rememberUpdatedState(content)
    val error = remember { derivedStateOf { (state as? ConnectionViewModel.State.Error)?.error } }
    val controller = remember { derivedStateOf {
        object : ConnectionInteractor {
            override fun invoke(id: String, value: Boolean) {
                viewModel.connect(id, value)
            }

            override fun observe(): StateFlow<Map<String, Boolean>> {
                return viewModel.connections
            }
        }
    } }
    CompositionLocalProvider(
        LocalConnectionInteractor provides controller.value
    ) {  updatedContent() }
    LaunchedEffect(Unit) {
        viewModel.initialize()
    }
    LaunchedEffect(error.value) {
        error.value?.message?.let {
            Toast.makeText(context, component.resource().string(it), Toast.LENGTH_SHORT).show()
            viewModel.reset()
        }
    }
}

fun Pair<Boolean, Boolean>.status(): ConnectionStatus {
    return if (first && second) {
        ConnectionStatus.PEER
    } else if (first) {
        ConnectionStatus.FOLLOWING
    } else {
        ConnectionStatus.FOLLOWER
    }
}

@Composable
fun ConnectionScreen(
    isFollowing: Boolean,
    isFollowed: Boolean,
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val primary = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    val clickHandler by rememberUpdatedState {
        onClick(isFollowing)
    }
    val state by remember(isFollowing, isFollowed) { derivedStateOf {
        when(Pair(isFollowing, isFollowed).status()) {
            ConnectionStatus.PEER -> ConnectionState(
                R.string.peer_label,
                onPrimary,
                primary,
                false
            )
            ConnectionStatus.FOLLOWING -> ConnectionState(
                R.string.following_label,
                primary,
                onPrimary,
                false
            )
            ConnectionStatus.FOLLOWER -> ConnectionState(
                R.string.follow_label,
                onPrimary,
                onPrimary,
                true
            )
        }
    } }
    DesignOutlinedButton(
        onClick = clickHandler,
        modifier = modifier.then(
            if (!state.outline) {
                Modifier.background(
                    color = state.borderColor,
                    shape = RoundedCornerShape(28),
                )
            } else {
                Modifier
            }
        ),
        shape = RoundedCornerShape(28),
        textStyle = MaterialTheme.typography.bodySmall.copy(
            color = state.textColor
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = state.textColor,
            disabledContainerColor = Color.Transparent
        ),
        minHeight = 32.dp,
        border = BorderStroke(1.dp, state.borderColor),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
    ) { Text(stringResource(state.res)) }
}
