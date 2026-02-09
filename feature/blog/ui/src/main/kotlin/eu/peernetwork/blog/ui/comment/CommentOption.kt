package eu.peernetwork.blog.ui.comment

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.designSecondaryButtonColors
import eu.peernetwork.core.ui.design.luna.designTertiaryButtonColors
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.core.ui.theme.PeerAppDarkRed
import eu.peernetwork.feature.blog.ui.R

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun CommentOption(
    onReport: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val handleReport by rememberUpdatedState(onReport)
    Box(
        content = content,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceDim)
            .combinedClickable(
                onClick = {},
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    handleReport()
                }
            )
    )
}

@Composable
fun CommentOption(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(48.dp)
                .clip(CircleShape)
                .background(PeerAppDarkRed.copy(alpha = .1f))
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_report),
                contentDescription = stringResource(R.string.report_label),
                tint = PeerAppDarkRed,
                modifier = Modifier.padding(8.dp)
                    .fillMaxSize()
            )
        }
        Text(
            text = stringResource(R.string.comment_report_prompt),
            color = PeerAppDarkRed,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 12.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(top = 20.dp)
                .padding(bottom = 6.dp)
        ) {
            DesignButton(
                onClick = onCancel,
                minHeight = 42.dp,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                colors = designTertiaryButtonColors(),
                contentPadding = PaddingValues(14.dp),
                modifier = Modifier.weight(1f)
            ) { Text(stringResource(R.string.cancel_label)) }
            DesignButton(
                onClick = onConfirm,
                minHeight = 42.dp,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                colors = designSecondaryButtonColors(),
                contentPadding = PaddingValues(14.dp),
                modifier = Modifier.weight(1f)
            ) { Text(stringResource(R.string.report_label)) }
        }
    }
}

@Preview
@Composable
fun PreviewCommentOption() {
    DesignTheme(isDarkMode = true) {
        CommentOption(
            onCancel = {}
        ) {}
    }
}
