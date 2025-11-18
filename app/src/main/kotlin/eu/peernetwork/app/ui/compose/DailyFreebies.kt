package eu.peernetwork.app.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.app.R
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.core.ui.theme.PeerAppRed
import eu.peernetwork.blog.ui.R as BlogRes
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun DailyFreebies() {
    Row(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceDim)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconLabel(
            size = 24.dp,
            space = 4.dp,
            painter = painterResource(id = BlogRes.drawable.ic_camera_outline),
            label = stringResource(R.string.onboarding_post_count),
            modifier = Modifier.padding(vertical = 8.dp)
                .weight(1f),
        )
        Spacer(modifier = Modifier.width(1.dp)
            .height(64.dp)
            .background(MaterialTheme.colorScheme.surfaceContainerLowest))
        IconLabel(
            size = 24.dp,
            space = 4.dp,
            painter = painterResource(id = BlogRes.drawable.ic_love),
            label = stringResource(R.string.onboarding_like_count),
            modifier = Modifier.padding(vertical = 8.dp)
                .weight(1f),
            tint = PeerAppRed
        )
        Spacer(modifier = Modifier.width(1.dp)
            .height(64.dp)
            .background(MaterialTheme.colorScheme.surfaceContainerLowest))
        IconLabel(
            size = 24.dp,
            space = 4.dp,
            painter = painterResource(id = BlogRes.drawable.ic_comment_outline),
            label =  stringResource(R.string.onboarding_comment_count),
            modifier = Modifier.padding(vertical = 8.dp)
                .weight(1f)
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun DailyFreebiesPreview() {
    DesignTheme {
        Box(modifier = Modifier.fillMaxWidth()
            .padding(24.dp)) {
            DailyFreebies()
        }
    }
}
