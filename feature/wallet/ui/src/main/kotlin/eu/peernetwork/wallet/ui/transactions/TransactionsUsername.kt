package eu.peernetwork.wallet.ui.transactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.feature.wallet.ui.R
import eu.peernetwork.wallet.ui.model.UiStatus
import eu.peernetwork.wallet.ui.model.UiUser

@Composable
fun transactionsUsername(
    user: UiUser,
    isRecipient: Boolean,
    isVisible: MutableState<Boolean>,
): AnnotatedString {
    return buildAnnotatedString {
        if (user.status == UiStatus.ILLEGAL) {
            append(stringResource(R.string.illegal_username))
        } else if (!user.isAccessible) {
            if (isVisible.value) {
                Label(user, isRecipient)
            } else {
                append(stringResource(R.string.hidden_username))
            }
        } else {
            Label(user, isRecipient)
        }
    }
}

@Composable
private fun AnnotatedString.Builder.Label(
    user: UiUser,
    isRecipient: Boolean
) {
    append(if (isRecipient) {
        stringResource(R.string.received_from, user.username)
    } else {
        stringResource(R.string.sent_to, user.username)
    })
    append(" ")
    withStyle(SpanStyle(
        color = MaterialTheme.colorScheme.outline,
        fontWeight = FontWeight.Medium,
        fontSize = MaterialTheme.typography.labelMedium.fontSize
    )) {
        append("#${user.slug}")
    }
}

@Composable
@Preview
fun PreviewTransactionsUsername() {
    DesignTheme(isDarkMode = true) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            val isVisible = remember { mutableStateOf(false) }
            transactionsUsername(
                isVisible = isVisible,
                isRecipient = true,
                user = UiUser(
                    id = "123",
                    username = "test",
                    slug = 1234,
                    imageUrl = "http:localhost",
                    isAccessible = false,
                    status = UiStatus.ILLEGAL
                ),
            )
        }
    }
}
