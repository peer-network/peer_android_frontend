package eu.peernetwork.user.ui.settings

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.compose.DesignTextField
import eu.peernetwork.core.ui.design.component.DesignErrorText
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.R

@Composable
fun ColumnScope.SettingsForm(
    username: TextFieldState,
    bio: TextFieldState,
    isLoading: State<Boolean>,
    error: State<Throwable?>,
    maxText: Int = 500,
) {
    val isValidLength = remember { derivedStateOf {
        bio.text.length <= maxText
    } }
    Box(contentAlignment = Alignment.BottomEnd) {
        DesignTextField(
            bio,
            contentPadding = PaddingValues(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 36.dp,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            enabled = !isLoading.value,
            verticalAlignment = Alignment.Top,
            maxLines = 3,
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
            text = "${bio.text.length}/$maxText",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodySmall.copy(
                color = if (isValidLength.value) {
                    MaterialTheme.colorScheme.surfaceDim
                } else {
                    MaterialTheme.colorScheme.error
                }
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
                DesignErrorText(
                    it,
                    modifier = Modifier.padding(top = 8.dp, start = 16.dp)
                )
            }
        }
    ) { Text(text = stringResource(R.string.username_label)) }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewSettingsForm() {
    PeerTheme {
        val username = remember { TextFieldState() }
        val bio = remember { TextFieldState() }
        Column {
            SettingsForm(
                username,
                bio,
                remember { mutableStateOf(false) },
                remember { mutableStateOf(null) },
            )
        }
    }
}
