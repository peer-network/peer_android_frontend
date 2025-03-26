package eu.peernetwork.user.ui.user.settings

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.compose.DesignAvatar
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun UserSettingsScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(UserSettings.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = UserSettingsViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val username = remember { TextFieldState() }
    val description = remember { TextFieldState() }
    UserSettingsContent(
        username = username,
        description = description,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        avatar = {
            Box(modifier = Modifier
                .size(64.dp)
                .background(MaterialTheme.colorScheme.tertiaryContainer)
            )
        }
    )
}

@Composable
fun UserSettingsContent(
    username: TextFieldState,
    description: TextFieldState,
    modifier: Modifier = Modifier,
    avatar: @Composable (() -> Unit)? = null,
) {
    Column(modifier = modifier) {
        DesignAvatar(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = null,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.tertiaryContainer, CircleShape)
                        .border(1.dp, MaterialTheme.colorScheme.background, CircleShape)
                        .padding(6.dp)
                        .size(8.dp)
                )
            },
            modifier = Modifier.padding(end = 8.dp)
        ) { avatar?.invoke() }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewUserSettingsContent() {
    PeerTheme {
        val username = remember { TextFieldState("johnDoe") }
        val description = remember { TextFieldState("Description") }
        UserSettingsContent(
            username = username,
            description = description,
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            avatar = {
                Box(modifier = Modifier
                    .size(64.dp)
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                )
            }
        )
    }
}
