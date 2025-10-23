package eu.peernetwork.wallet.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.material.DesignCard
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.wallet.ui.R

@Composable
fun ExpandableLabel(
    label: String,
    state: MutableState<Boolean>,
    onAnimationEnd: (Boolean) -> Unit = {},
    content: @Composable () -> Unit
) {
    val border = MaterialTheme.colorScheme.tertiaryContainer
    val updatedContent by rememberUpdatedState(content)
    DesignCard(
        color = MaterialTheme.colorScheme.surfaceVariant,
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        ExpandableOption(
            state = state,
            onAnimationEnd = onAnimationEnd,
            icon = {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(MaterialTheme.colorScheme.background)
                        .padding(12.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_transaction),
                        contentDescription = stringResource(R.string.transfer_label),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            },
            items = {
                Box(
                    modifier = Modifier
                        .drawBehind {
                            drawLine(
                                color = border.copy(alpha = .6f),
                                start = Offset(0f, 0f),
                                end = Offset(size.width, 0f),
                                strokeWidth = 1.dp.toPx()
                            )
                        }
                        .padding(vertical = 16.dp)
                ) { updatedContent() }
            }
        ) { Text(label, modifier = Modifier.padding(start = 12.dp)) }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewExpandableLabel() {
    PeerTheme {
        var showLabel = rememberSaveable { mutableStateOf(false) }
        ExpandableLabel("ExpandableLabel", showLabel) {
            Box(modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)) {
                Text("Content")
            }
        }
    }
}
