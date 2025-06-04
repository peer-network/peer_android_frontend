package eu.peernetwork.user.ui.registeration

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.compose.DesignButton
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.R

@Composable
fun RegistrationDialog(
    state: MutableState<Boolean>,
    isChecked: MutableState<Boolean>,
    onSubmit: () -> Unit
) {
    if (state.value) {
        AlertDialog(
            onDismissRequest = { state.value = false },
            title = {
                Text(
                    text = stringResource(id = R.string.age_confirmation_title),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            },
            text = {
                Column {
                    Text(
                        text = stringResource(id = R.string.age_confirmation_message),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = isChecked.value,
                            onCheckedChange = { isChecked.value = it }
                        )
                        Text(
                            text = stringResource(id = R.string.age_confirmation_checkbox),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }
                }
            },
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            confirmButton = {
                DesignButton(
                    enabled = isChecked.value,
                    onClick = onSubmit,
                    minHeight = 42.dp,
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.age_confirmation_continue),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewRegistrationDialog() {
    PeerTheme {
        val state = remember { mutableStateOf(false) }
        val isChecked = remember { mutableStateOf(false) }
        RegistrationDialog(state, isChecked) {  }
    }
}
