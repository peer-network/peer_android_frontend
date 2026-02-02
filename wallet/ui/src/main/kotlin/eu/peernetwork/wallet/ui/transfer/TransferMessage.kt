package eu.peernetwork.wallet.ui.transfer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignTextField
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.wallet.ui.R

@Composable
fun TransferMessage(
    state: TextFieldState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.then(modifier)
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceDim)
            .padding(10.dp)
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = stringResource(R.string.transaction_message_label),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        DesignTextField(
            state = state,
            contentPadding = PaddingValues(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 56.dp,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
                .padding(top = 8.dp),
            hint = stringResource(R.string.transaction_message_placeholder),
        )
    }
}

@Composable
fun TransferMessage(
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.then(modifier)
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceDim)
            .padding(10.dp)
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = stringResource(R.string.message_title),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Box(modifier = Modifier.padding(vertical = 8.dp)
            .fillMaxWidth()
            .height(1.dp)
            .background(MaterialTheme.colorScheme.surfaceContainerLow))
        Text(
            text = message,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Preview
@Composable
fun PreviewTransferMessage() {
    val state = remember { TextFieldState() }
    DesignTheme(isDarkMode = true) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            TransferMessage(state)
            TransferMessage("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris vel egestas urna, vitae molestie neque.")
        }
    }
}
