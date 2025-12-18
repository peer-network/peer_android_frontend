package eu.peernetwork.wallet.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.material.DesignDetailLayout
import eu.peernetwork.core.ui.design.material.DesignLabel
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun ExpandableOption(
    state: MutableState<Boolean>,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    onAnimationEnd: (Boolean) -> Unit = { },
    icon: @Composable () -> Unit,
    items: @Composable ColumnScope.() -> Unit,
    content: @Composable () -> Unit,
) {
    val updatedIcon by rememberUpdatedState(icon)
    val updatedItems by rememberUpdatedState(items)
    val updatedContent by rememberUpdatedState(content)
    CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colorScheme.onBackground,
        LocalTextStyle provides style.copy(
            color = MaterialTheme.colorScheme.onBackground
        )
    ) {
        DesignLabel(
            visible = state.value,
            onAnimationEnd = onAnimationEnd,
            label = {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) { updatedItems() }
            }
        ) {
            DesignDetailLayout(
                lead = {
                    Box(modifier = Modifier
                        .clip(CircleShape)
                        .wrapContentSize()
                        .clipToBounds()
                    ) { updatedIcon() } },
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(role = Role.Button) {
                        state.value = !state.value
                    }.padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    updatedContent()
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(R.drawable.ic_plus),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                            .graphicsLayer {
                                rotationZ = if (state.value) {
                                    45f
                                } else {
                                    0f
                                }
                            },
                        tint = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewOption() {
    DesignTheme {
        val state = remember { mutableStateOf(false) }
        ExpandableOption(state, icon = { Box(modifier = Modifier
            .size(32.dp)
            .background(MaterialTheme.colorScheme.background)) },
            items = { Text("Items") }
        ) {
            Text("Content", modifier = Modifier.padding(start = 12.dp))
        }
    }
}
