package eu.peernetwork.core.ui.design.material

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DesignPill(
    count: Int,
    enabled: Boolean,
    color: Color,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    DesignOutlinedButton(
        onClick = onClick,
        modifier = Modifier.background(
            color = MaterialTheme.colorScheme.onBackground,
            shape = RoundedCornerShape(28),
        ),
        enabled = enabled,
        shape = RoundedCornerShape(28),
        textStyle = MaterialTheme.typography.bodyMedium.copy(color = color),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = color,
            disabledContainerColor = Color.Transparent
        ),
        minHeight = 32.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            updatedContent()
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color)
            ) {
                Text(
                    "$count",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewNudgeButton() {
    DesignPill(
        count = 2,
        enabled = true,
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Text(
            "Done",
            modifier = Modifier.padding(end = 8.dp)
        )
    }
}
