package eu.peernetwork.app.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import eu.peernetwork.blog.ui.R as CoreRes
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun DailyFreebies() {
    Row(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconLabel(
            size = 48.dp,
            painter = painterResource(id = CoreRes.drawable.ic_camera_outline),
            label = stringResource(R.string.onboarding_post_count),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.width(1.dp)
            .height(64.dp)
            .background(MaterialTheme.colorScheme.onTertiaryContainer))
        IconLabel(
            size = 48.dp,
            painter = painterResource(id = R.drawable.bg_heart),
            label = stringResource(R.string.onboarding_like_count),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.width(1.dp)
            .height(64.dp)
            .background(MaterialTheme.colorScheme.onTertiaryContainer))
        IconLabel(
            size = 48.dp,
            painter = painterResource(id = CoreRes.drawable.ic_chat_out),
            label =  stringResource(R.string.onboarding_comment_count),
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun DailyFreebiesPreview() {
    PeerTheme {
        Box(modifier = Modifier.fillMaxWidth()
            .padding(24.dp)) {
            DailyFreebies()
        }
    }
}
