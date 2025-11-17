package eu.peernetwork.user.ui.user

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignAvatar
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun UserError(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            DesignAvatar {
                Box(modifier = Modifier.size(56.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest))
            }
            Box(modifier = Modifier.padding(start = 12.dp)
                .fillMaxWidth(fraction = .75f)
                .height(42.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest))
        }
        Box(modifier = Modifier.padding(top = 14.dp)
            .fillMaxWidth(fraction = .6f)
            .height(16.dp)
            .clip(CircleShape)) {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.error
                )
            ) { updatedContent() }
        }
        Row(modifier = Modifier.padding(top = 16.dp)) {
            Box(modifier = Modifier.weight(1f)
                .height(42.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest))
            Spacer(modifier = Modifier.width(12.dp))
            Box(modifier = Modifier.weight(1f)
                .height(42.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest))
            Spacer(modifier = Modifier.width(16.dp))
            Box(modifier = Modifier.size(42.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest))
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
fun PreviewUserError() {
    DesignTheme(isDarkMode = false) {
        UserError(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(vertical = 16.dp),
        ) { Text("Error occured here...") }
    }
}
