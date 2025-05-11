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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignOutlinedButton
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.social.ui.R
import eu.peernetwork.social.ui.member.status
import kotlinx.coroutines.flow.StateFlow

interface ConnectionController {
    operator fun invoke(id: String, value: Boolean)

    fun observe(): StateFlow<Map<String, Boolean>>
}

data class ConnectionState(
    val res: Int,
    val textColor: Color,
    val borderColor: Color,
    val useGradient: Boolean
)

enum class ConnectionStatus {
    PEER,
    FOLLOWING,
    FOLLOW
}

@Composable
fun ConnectionScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (ConnectionController) -> Unit
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
    val controller by remember { derivedStateOf {
        object : ConnectionController {
            override fun invoke(id: String, value: Boolean) {
                viewModel.connect(id, value)
            }

            override fun observe(): StateFlow<Map<String, Boolean>> {
                return viewModel.connections
            }
        }
    } }
    updatedContent(controller)
    LaunchedEffect(Unit) {
        viewModel.initialize()
    }
    LaunchedEffect(error.value) {
        error.value?.message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.reset()
        }
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
    val secondary = MaterialTheme.colorScheme.secondary
    val gradient = Brush.horizontalGradient(colors = listOf(secondary, primary))
    val clickHandler by rememberUpdatedState {
        onClick(isFollowing)
    }
    val state by remember(isFollowing, isFollowed) { derivedStateOf {
        when(Pair(isFollowing, isFollowed).status()) {
            ConnectionStatus.PEER -> ConnectionState(
                R.string.peer_label,
                onPrimary,
                secondary,
                true
            )
            ConnectionStatus.FOLLOWING -> ConnectionState(
                R.string.following_label,
                secondary,
                primary,
                false
            )
            ConnectionStatus.FOLLOW -> ConnectionState(
                R.string.follow_label,
                onPrimary,
                onPrimary,
                false
            )
        }
    } }
    DesignOutlinedButton(
        onClick = clickHandler,
        modifier = modifier.background(
            brush = if (state.useGradient) {
                gradient
            } else {
                Brush.linearGradient(
                    listOf(Color.Transparent, Color.Transparent)
                )
            },
            shape = RoundedCornerShape(28),
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
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 4.dp)
    ) { Text(stringResource(state.res)) }
}
