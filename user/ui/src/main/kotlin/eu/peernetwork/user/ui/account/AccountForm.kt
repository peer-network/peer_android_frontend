package eu.peernetwork.user.ui.account

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.DesignTextField
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.compose.form.ErrorLabel

@Composable
fun AccountForm(
    username: TextFieldState,
    bio: TextFieldState,
    enable: State<Boolean>,
    isLoading: State<Boolean>,
    error: State<String?>,
    maxText: Int = 500,
    onSubmit: () -> Unit
) {
    val isValidLength = remember {
        derivedStateOf {
            bio.text.length <= maxText
        }
    }
    DesignTextField(
        state = username,
        enabled = !isLoading.value,
        hint = stringResource(R.string.username_label)
    )
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.padding(
            top = 12.dp,
            bottom = 8.dp,
        )
    ) {
        DesignTextField(
            state = bio,
            contentPadding = PaddingValues(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 56.dp,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            enabled = !isLoading.value,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            leading = {
                Text(
                    text = stringResource(R.string.description_label),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .align(Alignment.Top)
                )
            },
            hint = stringResource(R.string.description_placeholder)
        )
        Text(
            text = "${bio.text.length}/$maxText",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodySmall.copy(
                color = if (isValidLength.value) {
                    MaterialTheme.colorScheme.outlineVariant
                } else {
                    MaterialTheme.colorScheme.error
                }
            )
        )
    }
    ErrorLabel(
        error = error,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
    )
    DesignButton(
        onClick = onSubmit,
        enabled = !isLoading.value && enable.value,
        isLoading = isLoading.value,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
    ) { Text(stringResource(R.string.save_text)) }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewSettingsForm() {
    DesignTheme {
        val username = remember { TextFieldState() }
        val bio = remember { TextFieldState() }
        Column(modifier = Modifier.padding(24.dp)) {
            AccountForm(
                username,
                bio,
                remember { mutableStateOf(false) },
                remember { mutableStateOf(false) },
                remember { mutableStateOf("Hello, world!") },
            ) {}
        }
    }
}
