package eu.peernetwork.core.ui.design.compose

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.theme.PeerTheme

@Immutable
data class DesignToolbarTitle(
    val label: Int,
    val onClick: (() -> Unit)? = null
)

@Composable
fun DesignToolbarAction(
    title: MutableState<DesignToolbarTitle>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable(
                role = Role.Button,
                onClick = { title.value.onClick?.invoke() }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stringResource(title.value.label), Modifier.padding(start = 8.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_caret_down),
            contentDescription = null,
            modifier = Modifier.graphicsLayer {
                alpha = if (title.value.onClick != null) 1f else 0f
            }
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDesignToolbarAction() {
    PeerTheme {
        Column {
            DesignToolbarAction(title = remember { mutableStateOf(DesignToolbarTitle(R.string.loading_text)) })
            DesignToolbarAction(title = remember { mutableStateOf(DesignToolbarTitle(R.string.loading_text) {

            }) })
        }
    }
}
