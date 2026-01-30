package eu.peernetwork.wallet.ui.transfer.v2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignTextField
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.wallet.ui.R

@Composable
fun TransferAmount(
    state: TextFieldState,
    isLoading: State<Boolean>,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null
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
            text = stringResource(R.string.amount_entry),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            DesignTextField(
                state = state,
                enabled = !isLoading.value,
                shape = CircleShape,
                hint = stringResource(R.string.min_token_placeholder),
                maxLength = 500,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                contentPadding = PaddingValues(
                    top = 16.dp,
                    start = 16.dp,
                    end = 56.dp,
                    bottom = 16.dp,
                ),
                modifier = Modifier.then(
                    if (focusRequester != null) {
                        Modifier.focusRequester(focusRequester)
                    } else {
                        Modifier
                    }
                )
            )
            Icon(
                painter = painterResource(R.drawable.ic_peer_token),
                contentDescription = stringResource(R.string.amount_entry),
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
                    .size(24.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewTransferAmount() {
    val state = remember { TextFieldState() }
    val isLoading = remember { mutableStateOf(false) }
    DesignTheme(isDarkMode = true) {
        TransferAmount(state, isLoading)
    }
}
