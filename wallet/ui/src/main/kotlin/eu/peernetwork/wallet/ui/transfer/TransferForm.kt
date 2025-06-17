package eu.peernetwork.wallet.ui.transfer

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.compose.DesignTextField
import eu.peernetwork.core.ui.extension.toFloat
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.wallet.ui.R
import java.math.BigDecimal

@Composable
fun TransferForm(
    state: TextFieldState,
    tax: Double,
    isLoading: Boolean = false,
    onSubmit: (BigDecimal) -> Unit,
) {
    val isValidated = remember { derivedStateOf { state.text.toString().toBigDecimalOrNull() != null } }
    val handleSubmission by rememberUpdatedState {
        state.text.toString().toBigDecimalOrNull()?.let {
            onSubmit(it)
        }
    }
    Row {
        Column {
            Box(contentAlignment = Alignment.CenterEnd) {
                DesignTextField(
                    state = state,
                    enabled = !isLoading,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Send
                    ),
                    onKeyboardAction = KeyboardActions(
                        onSend = { handleSubmission() }
                    ),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.surfaceDim,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.surfaceTint,
                    ),
                ) { Text(stringResource(R.string.amount_label)) }
                IconButton(
                    { handleSubmission() },
                    modifier = Modifier.padding(end = 8.dp)
                        .size(32.dp)
                        .graphicsLayer { alpha = isValidated.value.toFloat() }
                ) {
                    Icon(
                        painter = painterResource(eu.peernetwork.core.ui.R.drawable.ic_share),
                        contentDescription = stringResource(R.string.transfer_label),
                        modifier = Modifier.fillMaxSize().padding(8.dp),
                        tint = MaterialTheme.colorScheme.surfaceDim
                    )
                }
            }
            Text(
                stringResource(R.string.transfer_disclaimer, "$tax%"),
                modifier = Modifier.padding(top = 12.dp)
                    .padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.tertiary,
                ),
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewTransferForm() {
    PeerTheme {
        val state = remember { TextFieldState() }
        Box(Modifier.padding(16.dp)) {
            TransferForm(state, 4.0) {}
        }
    }
}
