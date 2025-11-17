package eu.peernetwork.user.ui.user

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignAvatar
import eu.peernetwork.core.ui.design.luna.DesignSkeleton
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun UserSkeleton(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            DesignAvatar {
                Box(modifier = Modifier.size(56.dp)
                    .background(MaterialTheme.colorScheme.surfaceDim))
            }
            DesignSkeleton(modifier = Modifier.padding(start = 12.dp)
                .height(42.dp)
                .fillMaxWidth(fraction = .75f))
        }
        DesignSkeleton(modifier = Modifier.padding(top = 14.dp)
            .fillMaxWidth(fraction = .6f)
            .height(16.dp))
        Row(modifier = Modifier.padding(top = 16.dp)) {
            DesignSkeleton(modifier = Modifier.weight(1f)
                .height(42.dp))
            Spacer(modifier = Modifier.width(12.dp))
            DesignSkeleton(modifier = Modifier.weight(1f)
                .height(42.dp))
            Spacer(modifier = Modifier.width(12.dp))
            DesignSkeleton(modifier = Modifier.size(42.dp))
        }
    }
}

@Preview
@Composable
fun DarkPreviewUserSkeleton() {
    DesignTheme(isDarkMode = true) {
        Box(modifier = Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)) {
            UserSkeleton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(vertical = 16.dp),
            )
        }
    }
}

@Preview
@Composable
fun LightPreviewUserSkeleton() {
    DesignTheme(isDarkMode = false) {
        Box(modifier = Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)) {
            UserSkeleton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(vertical = 16.dp),
            )
        }
    }
}
