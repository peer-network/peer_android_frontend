package eu.peernetwork.wallet.ui.transfer

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignTextField
import eu.peernetwork.core.ui.theme.DesignTheme
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
    Column {
        Box(contentAlignment = Alignment.CenterEnd) {
            DesignTextField(
                state = state,
                enabled = !isLoading,
                shape = RoundedCornerShape(24.dp),
                hint = stringResource(R.string.amount_label),
                maxLength = 500,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Send
                ),
                onKeyboardAction = KeyboardActions(
                    onSend = { handleSubmission() }
                ),
                contentPadding = PaddingValues(
                    top = 16.dp,
                    start = 16.dp,
                    end = 56.dp,
                    bottom = 16.dp,
                ),
            )
            TransferButton(
                enabled = isValidated.value,
                isLoading = isLoading,
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.BottomEnd),
            ) { handleSubmission() }
        }
        Text(
            stringResource(R.string.transfer_disclaimer, "3%"),
            modifier = Modifier
                .padding(top = 12.dp)
                .padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.outline,
            ),
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewTransferForm() {
    DesignTheme {
        val state = remember { TextFieldState() }
        Box(Modifier.padding(16.dp)) {
            TransferForm(state, 4.0) {}
        }
    }
}
