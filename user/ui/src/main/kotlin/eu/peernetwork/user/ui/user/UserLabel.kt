package eu.peernetwork.user.ui.user

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.core.ui.theme.PeerAppDarkRed
import eu.peernetwork.core.ui.theme.PeerAppRed
import eu.peernetwork.user.ui.R

@Composable
fun UserLabel(
    slug: Int,
    username: String,
    reported: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val interaction = remember { MutableInteractionSource() }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = buildAnnotatedString {
                append(username)
                append(" ")
                withStyle(
                    SpanStyle(
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Normal,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        color = MaterialTheme.colorScheme.outline
                    )
                ) { append("#${slug}") }
            },
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
            ),
            color = MaterialTheme.colorScheme.onBackground,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.weight(1f))
        Crossfade(reported) { target ->
            val tooltip = remember { mutableStateOf(false) }
            if (target) {
                Icon(
                    painter = painterResource(R.drawable.ic_flag),
                    contentDescription = stringResource(R.string.report_label),
                    tint = PeerAppDarkRed,
                    modifier = Modifier.size(18.dp)
                        .clickable(
                            indication = null,
                            interactionSource = interaction
                        ) { tooltip.value = !tooltip.value }
                )
                if (tooltip.value) {
                    Popup(
                        alignment = Alignment.TopStart,
                        offset = IntOffset(x = -64.dp.value.let {
                            it * context.resources.displayMetrics.density
                        }.toInt(), y = -4.dp.value.let {
                            it * context.resources.displayMetrics.density
                        }.toInt()),
                        onDismissRequest = { tooltip.value = false }
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shadowElevation = 4.dp,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.reported_label),
                                color = PeerAppRed,
                                fontWeight = FontWeight.Normal,
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(
                                    vertical = 6.dp,
                                    horizontal = 8.dp,
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun PreviewUserLabel() {
    DesignTheme(isDarkMode = true) {
        UserLabel(
            username = "JohnDoe",
            slug = 2343,
            reported = true
        )
    }
}
