package eu.peernetwork.user.ui.option

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.user.ui.R

@Composable
fun OptionItem(
    label: String,
    painter: Painter? = null,
    color: Color = MaterialTheme.colorScheme.onBackground,
    onClick: () -> Unit,
) {
    val border = MaterialTheme.colorScheme.surfaceDim
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick)
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = border,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )
            }.padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Box(modifier = Modifier
            .size(28.dp)) {
            painter?.let {
                Icon(
                    painter,
                    contentDescription = stringResource(R.string.referral_label),
                    tint = MaterialTheme.colorScheme.outline,
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
fun PreviewOptionItem() {
    DesignTheme {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {
            OptionItem(
                "Option",
                painterResource(R.drawable.ic_ads)
            ) {}
            OptionItem(
                "Close",
                color = MaterialTheme.colorScheme.error
            ) {}
        }
    }
}
