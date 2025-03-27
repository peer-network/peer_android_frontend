package eu.peernetwork.user.ui.user.settings

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.compose.DesignOutlinedButton
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.model.UiAccount
import eu.peernetwork.user.ui.model.UiOverview

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
    val state by viewModel.state.collectAsStateWithLifecycle()
    Crossfade(targetState = state) {
        when (it) {
            UserSettingsViewModel.State.Initialize -> {}
            UserSettingsViewModel.State.Loading -> {  }
            is UserSettingsViewModel.State.Success -> UserSettingsContent(
                account = it.account,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
            ) { model, password -> viewModel.update(it.account, model, password ?: "") }
            is UserSettingsViewModel.State.Error -> {  }
        }
    }
}

@Composable
fun UserSettingsContent(
    account: UiAccount,
    modifier: Modifier = Modifier,
    onSubmit: (List<UserSettingsModel>, String?) -> Unit
) {
    val image = remember { mutableStateOf<Uri?>(null) }
    val username = remember { TextFieldState(account.username) }
    val bio = remember { TextFieldState(account.bio ?: "") }
    val state = remember { derivedStateOf {
        listOf(
            UserSettingsModel.Avatar(image.value),
            UserSettingsModel.Username(username.text.toString()),
            UserSettingsModel.Description(bio.text.toString()),
        )
    } }
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            UserSettingsAvatar(
                name = account.username,
                imageUrl = account.imageUrl,
                onChange = { image.value = it }
            )
            Spacer(modifier = Modifier.weight(1f))
            DesignOutlinedButton(
                onClick = {
                    if (!state.value.none { it.protected }) {
                        onSubmit(state.value, "")
                    } else {
                        onSubmit(state.value, null)
                    } },
                enabled = state.value != account.toSettings(),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp, horizontal = 32.dp),
                modifier = Modifier
                    .padding(start = 4.dp)
                    .height(28.dp),
                content = {
                    Text(
                        text = stringResource(R.string.save_text),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            )
        }
        UserSettingsForm(username, bio)
        Row(modifier = Modifier.padding(top = 16.dp)) {
            DesignOutlinedButton(
                onClick = {},
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 6.dp),
                content = { Text(stringResource(R.string.logout_text)) }
            )
            DesignOutlinedButton(
                onClick = {},
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 6.dp),
                content = { Text(stringResource(R.string.deactivate_text)) },
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.errorContainer),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.errorContainer,
                    disabledContentColor = MaterialTheme.colorScheme.errorContainer
                )
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewUserSettingsContent() {
    PeerTheme {
        val model = UiAccount(
            id = System.currentTimeMillis().toString(),
            username = "John Doe",
            slug = 0,
            bio = "Description....",
            imageUrl = "",
            overview = UiOverview(
                posts = 0,
                peers = 0,
                followers = 0,
                followed = 0
            )
        )
        UserSettingsContent(
            account = model,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            onSubmit = { model, password -> }
        )
    }
}
