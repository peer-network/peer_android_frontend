package eu.peernetwork.core.ui.design.luna

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun DesignItem(
    label: String,
    painter: Painter? = null,
    size: Dp = 28.dp,
    color: Color = MaterialTheme.colorScheme.onBackground,
    tint: Color = MaterialTheme.colorScheme.outline,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    onClick: () -> Unit,
) {
    val border = MaterialTheme.colorScheme.surfaceDim
    Row(
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick)
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = border,
                    start = Offset(0f, this.size.height),
                    end = Offset(this.size.width, this.size.height),
                    strokeWidth = strokeWidth
                )
            }.padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Box(modifier = Modifier
            .size(size)) {
            painter?.let {
                Icon(
                    painter,
                    contentDescription = label,
                    tint = tint,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Text(
            label,
            color = color,
            modifier = Modifier.padding(start = 6.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
fun PreviewDesignItem() {
    DesignTheme {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {
            DesignItem(
                "Copy",
                painterResource(R.drawable.ic_copy)
            ) {}
            DesignItem(
                "Close",
                color = MaterialTheme.colorScheme.error
            ) {}
        }
    }
}
