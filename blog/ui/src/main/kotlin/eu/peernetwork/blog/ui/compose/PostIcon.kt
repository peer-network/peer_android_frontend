package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.model.UiAction
import eu.peernetwork.core.ui.design.compose.DesignTextButton

@Composable
fun PostIcon(
    action: UiAction,
    value: String,
    isHorizontal: Boolean = true,
    color: Color = MaterialTheme.colorScheme.tertiary,
    onClick: (UiAction) -> Unit,
) {
    DesignTextButton(
        onClick = { onClick(action) },
        contentPadding = PaddingValues(4.dp)
    ) {
        if (isHorizontal) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = action.id),
                    contentDescription = action.label?.let { stringResource(it) },
                    tint = color,
                    modifier = Modifier.size(28.dp)
                )
                Text(value, style = MaterialTheme.typography.bodySmall.copy(
                    color = color
                ))
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(id = action.id),
                    contentDescription = action.label?.let { stringResource(it) },
                    tint = color,
                    modifier = Modifier.size(28.dp)
                )
                Text(value, style = MaterialTheme.typography.bodySmall.copy(
                    color = color
                ))
            }
        }
    }
}
