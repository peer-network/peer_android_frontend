package eu.peernetwork.user.ui.user.settings

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.compose.DesignTextField
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.R

@Composable
fun ColumnScope.UserSettingsForm(
    username: TextFieldState,
    bio: TextFieldState,
    isLoading: androidx.compose.runtime.State<Boolean>,
    error: androidx.compose.runtime.State<Throwable?>,
) {
    Box(contentAlignment = Alignment.BottomEnd) {
        DesignTextField(
            bio,
            contentPadding = PaddingValues(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 36.dp,
            ),
            enabled = !isLoading.value,
            verticalAlignment = Alignment.Top,
            lineLimits = TextFieldLineLimits.MultiLine(),
            modifier = Modifier.fillMaxWidth()
                .padding(top = 16.dp),
            leading = {
                Text(
                    text = stringResource(R.string.description_label),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.tertiary
                    ),
                    modifier = Modifier.padding(end = 8.dp)
                        .padding(bottom = 48.dp)
                )
            }
        ) { Text(text = stringResource(R.string.description_placeholder)) }
        Text(
            text = "${bio.text.length}/500",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.surfaceDim
            )
        )
    }
    DesignTextField(
        username,
        enabled = !isLoading.value,
        hasError = error.value != null,
        modifier = Modifier.padding(top = 12.dp),
        error = {
            error.value?.let {
                Text(
                    text = it.message ?: stringResource(R.string.unknown_error_message),
                    modifier = Modifier.padding(top = 8.dp, start = 16.dp)
                )
            }
        }
    ) { Text(text = stringResource(R.string.username_label)) }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewUserSettingsForm() {
    PeerTheme {
        val username = remember { TextFieldState() }
        val bio = remember { TextFieldState() }
        Column {
            UserSettingsForm(
                username,
                bio,
                remember { mutableStateOf(false) },
                remember { mutableStateOf(null) },
            )
        }
    }
}
