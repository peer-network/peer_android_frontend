package eu.peernetwork.wallet.ui.confirmation

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.compose.DesignOutlinedButton
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.wallet.ui.R

@Composable
fun ConfirmationScaffold(
    title: @Composable () -> Unit,
    footer: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    val updatedTitle by rememberUpdatedState(title)
    val updatedFooter by rememberUpdatedState(footer)
    val updatedContent by rememberUpdatedState(content)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 24.dp)
    ) {
        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onPrimary
        )) { updatedTitle() }
        Spacer(modifier = Modifier.height(12.dp))
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.tertiary,
            LocalTextStyle provides MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.tertiary
            )
        ) { updatedContent() }
        Spacer(modifier = Modifier.height(18.dp))
        updatedFooter()
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewConfirmationScaffold() {
    PeerTheme {
        val textColor = MaterialTheme.colorScheme.surfaceVariant
        val onPrimary = MaterialTheme.colorScheme.onPrimary
        ConfirmationScaffold({
            Text(stringResource(R.string.post_caption))
        }, footer = {
            Row {
                DesignOutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(28),
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        color = onPrimary,
                        fontWeight = FontWeight.SemiBold
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = onPrimary,
                        disabledContainerColor = Color.Transparent
                    ),
                    border = BorderStroke(1.dp, onPrimary),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                ) { Text(stringResource(R.string.cancel_label)) }
                Spacer(modifier = Modifier.width(12.dp))
                DesignOutlinedButton(
                    onClick = { },
                    modifier = Modifier
                        .background(
                            color = onPrimary,
                            shape = RoundedCornerShape(28),
                        )
                        .weight(1f),
                    shape = RoundedCornerShape(28),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = textColor,
                        fontWeight = FontWeight.SemiBold
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = onPrimary,
                        disabledContainerColor = Color.Transparent
                    ),
                    border = BorderStroke(1.dp, onPrimary),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                ) { Text(stringResource(R.string.confirm_label)) }
            }
        }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(eu.peernetwork.core.ui.R.drawable.ic_chat),
                    contentDescription = stringResource(eu.peernetwork.core.ui.R.string.chat_label),
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    stringResource(R.string.post_condition, "$5", "$10"),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.tertiary
                    ),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.weight(.3f))
            }
        }
    }
}
